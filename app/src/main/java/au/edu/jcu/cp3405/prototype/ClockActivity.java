package au.edu.jcu.cp3405.prototype;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ClockActivity extends AppCompatActivity {

    SoundManager soundManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        soundManager = (SoundManager) getApplicationContext();
        setTimeDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimeDate();
    }

    @SuppressLint("SimpleDateFormat")
    private void setTimeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nHH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        String finalDisplayText = dayOfTheWeek + "\n" + currentDateandTime;

        TextView current = findViewById(R.id.currentTimeDate);
        current.setText(finalDisplayText);
    }

    public void viewAlarmsClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, ViewRemindersActivity.class);
        startActivity(intent);
        /*Intent openClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        this.startActivityForResult(openClockIntent, 222);*/
    }

    public void addNewClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, NewReminderActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        onBackPressed();
    }
}