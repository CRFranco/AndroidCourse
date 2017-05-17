package br.com.senior.tp51;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

public class HardwareActivity extends Activity {
    private boolean flashLightOn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);
    }

    public void escolherOpcao(View view){
        if(view.getId() == R.id.camera){
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.mediaplayer){
            Intent intent = new Intent(this, MediaPlayerActivity.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.vibrate){

            Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
        }

        if(view.getId() == R.id.flashLight){
            Camera cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            if(!flashLightOn) {
                flashLightOn=true;
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            } else{
                cam.stopPreview();
                cam.release();
                flashLightOn=false;
            }
        }
    }
}