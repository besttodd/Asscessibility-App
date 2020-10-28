package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MessagingActivity extends AppCompatActivity {
    SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        soundManager = (SoundManager) getApplicationContext();
    }

    public void newMessageClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, NewMessageActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        onBackPressed();
    }
}