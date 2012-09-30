
package jp.gr.java_conf.u6k.sample_android_camera_preview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openCameraPreviewButton = (Button) findViewById(R.id.open_camera_preview_button);
        openCameraPreviewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_camera_preview_button:
                Intent intent = new Intent(this, CameraPreviewActivity.class);
                startActivity(intent);
                break;

        }
    }

}
