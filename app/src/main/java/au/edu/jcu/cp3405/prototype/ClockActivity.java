package au.edu.jcu.cp3405.prototype;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

public class ClockActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nHH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        String finalDisplayText = dayOfTheWeek + "\n" + currentDateandTime;

        TextView current = findViewById(R.id.currentTimeDate);
        current.setText(finalDisplayText);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onBackPressed();
    }*/

    public void viewAlarmsClicked(View view) {
        Intent intent = new Intent(this, ViewRemindersActivity.class);
        startActivity(intent);
        /*Intent openClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        this.startActivityForResult(openClockIntent, 222);*/
    }

    public void addNewClicked(View view) {
        Intent intent = new Intent(this, NewReminderActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}