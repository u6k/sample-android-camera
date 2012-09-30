
package jp.gr.java_conf.u6k.sample_android_camera_preview;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

// カメラプレビューは、常に横画面として表示される。
// このため、ActivityをAndroidManifest.xmlで横固定とする。
// 横固定にしないと、プレビューが変な方向で表示される。

// カメラを使用する場合、以下のパーミッションをAndroidManifest.xmlに記述する。
// <uses-permission android:name="android.permission.CAMERA" />
// <uses-feature android:name="android.hardware.camera" />
public class CameraPreviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全画面でカメラプレビューを表示するための設定。
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // カメラプレビューのViewを作成し、ContentViewとして登録する。
        CameraPreview cp = new CameraPreview(this);
        setContentView(cp);
    }

    private class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder _holder;

        private Camera _camera;

        CameraPreview(Context ctx) {
            super(ctx);

            // お約束の設定。
            _holder = getHolder();
            _holder.addCallback(this);
            _holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // サーフェイスが変更されたら、カメラプレビューを一度停止してから開始する。
            // Webのサンプルでは、この時にパラメータを設定しているモノもあったが、実機で試したらクラッシュした。
            // よって、以下のコードはこれでいいのか不明。要、多機種の実機検証。
            _camera.stopPreview();
            _camera.startPreview();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // カメラを開く。
            _camera = Camera.open();
            try {
                _camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // カメラは精密機器であるため、正常に開けるとは限らない。
                // 開けなかった場合はIOException例外がスローされるため、ユーザーにメッセージを表示して画面を終了する動作が望ましい。
                Toast.makeText(CameraPreviewActivity.this, R.string.message_camera_preview_fail, Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // サーフェイスが破棄される時、カメラプレビューを停止してカメラを破棄する。
            // カメラの破棄を忘れると、以降のカメラの動作が不安定になる。
            _camera.setPreviewCallback(null);
            _camera.stopPreview();
            _camera.release();
        }

    }

}
