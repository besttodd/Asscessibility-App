package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class NewReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
    }

    public void saveAlarm(View view) {
        Spinner hourSpinner = findViewById(R.id.hoursSpinner);
        int hour = hourSpinner.getSelectedItemPosition();

        RadioButton pmRadio = findViewById(R.id.pmRadio);
        if (pmRadio.isChecked()) { hour = hour + 12;}

        EditText minText = findViewById(R.id.minutesEditText);
        int min = Integer.parseInt(minText.getText().toString());

        /*Spinner minSpinner = findViewById(R.id.minutesSpinner);
        String minText = String.valueOf(minSpinner.getSelectedItem());
        int min = Integer.parseInt(minText);*/

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, min);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        Log.d("NewReminder",hour+":"+min+"==================================================================");

        if(calSet.compareTo(calNow) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        List<Calendar> alarmList = null;
        alarmList.add(calSet);
        setAlarm(calSet, alarmList.size());

        /*if (chkMonday.isChecked())
        {
            calSet.set(Calendar.DAY_OF_WEEK, 0);
            setAlarm(calSet);
        }

        if (chkTuesday.isChecked())
        {
            calSet.set(Calendar.DAY_OF_WEEK, 1);
            setAlarm(calSet);
        }

        if (chkWednesday.isChecked())
        {
            calSet.set(Calendar.DAY_OF_WEEK, 2);
            setAlarm(calSet);
        }

        if (chkThrusday.isChecked())
        {
            calSet.set(Calendar.DAY_OF_WEEK, 3);
            setAlarm(calSet);
        }

        if (chkFriday.isChecked())
        {
            calSet.set(Calendar.DAY_OF_WEEK, 4);
            setAlarm(calSet);
        }

        if (chkSaturday.isChecked())
        {
            calSet.set(Calendar.DAY_OF_WEEK, 5);
            setAlarm(calSet);
        }

        if (chkSunday.isChecked())
        {
            calSet.set(Calendar.DAY_OF_WEEK, 6);
            setAlarm(calSet);
        }*/
    }

    /*private Calendar createNewAlarm(Calendar calSet) {
        //setAlarmID
        //setAlarmCal
        return alarm.cal();
    }*/

    private void setAlarm(Calendar cal, int id)
    {
        Intent myIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
        AlarmManager myAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        myAlarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), myPendingIntent);
        //delete if single alarm
        //_myAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24*60*60*1000, _myPendingIntent);
        Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
        Log.d("NewReminder", "ALARM SET======================================================================");
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}