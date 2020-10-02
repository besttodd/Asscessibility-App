package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundManager = (SoundManager) getApplicationContext();
    }

    public void calendarClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void contactsClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

    public void clockClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, ClockActivity.class);
        startActivity(intent);
    }

    public void callClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, PhoneActivity.class);
        startActivity(intent);
    }
}