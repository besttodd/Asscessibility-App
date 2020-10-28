package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    SoundManager soundManager;
    LinearLayout emergencyConfirm;
    LinearLayout greyScreen;
    ScrollView scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundManager = (SoundManager) getApplicationContext();
        emergencyConfirm = findViewById(R.id.emergencyConfirm);
        greyScreen = findViewById(R.id.greyScreen);
        scroller = findViewById(R.id.scroller);
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

    public void flashlightClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, FlashlightActivity.class);
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

    public void messagingClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, MessagingActivity.class);
        startActivity(intent);
    }

    public void emergencyClicked(View view) {
        emergencyConfirm.setVisibility(View.VISIBLE);
        greyScreen.setVisibility(View.VISIBLE);
        enableDisableView(scroller, false);
    }

    public void emiClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, EmergencyInfoActivity.class);
        startActivity(intent);
    }

    public void cancelEmergency(View view) {
        emergencyConfirm.setVisibility(View.INVISIBLE);
        greyScreen.setVisibility(View.INVISIBLE);
        enableDisableView(scroller, true);
        //Open medical info for additional help
        emiClicked(view);
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
}