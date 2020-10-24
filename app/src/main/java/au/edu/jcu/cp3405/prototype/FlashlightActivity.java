package au.edu.jcu.cp3405.prototype;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FlashlightActivity extends AppCompatActivity {
    ImageView flashlightImg;
    SoundManager soundManager;
    CameraManager camManager;
    String cameraId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
        soundManager = (SoundManager) getApplicationContext();
        flashlightImg = findViewById(R.id.flashlightImg);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Your phone does not have a flashlight, this function will not work.", Toast.LENGTH_LONG).show();
            disableButton((Button) findViewById(R.id.onButton));
            disableButton((Button) findViewById(R.id.offButton));
        }
    }

    private void disableButton(Button button) {
        button.setClickable(false);
        button.setBackground(getResources().getDrawable(R.drawable.button_aqua_disabled));
        button.setTextColor(getResources().getColor(R.color.grey));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void flashlightOn(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        flashlightImg.setImageDrawable(getResources().getDrawable(R.drawable.torch_on));

        //Turn camera flash on
        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            assert camManager != null;
            cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void flashLightOff(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        //TODO turn camera flash off
        flashlightImg.setImageDrawable(getResources().getDrawable(R.drawable.torch));

        //Turn camera flash off
        try {
            camManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}