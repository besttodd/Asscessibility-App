package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;

public class ClockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onBackPressed();
    }*/

    public void viewAlarmsClicked(View view) {
        //OPEN EXISTING CLOCK
        Intent openClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        this.startActivityForResult(openClockIntent, 222);
    }

    public void addNewClicked(View view) {
        Intent intent = new Intent(this, NewReminderActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}