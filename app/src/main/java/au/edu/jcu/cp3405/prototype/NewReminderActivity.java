package au.edu.jcu.cp3405.prototype;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewReminderActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyAlarms" ;
    SharedPreferences sharedpreferences;
    Calendar calSet;
    List<Alarm> alarmList = new ArrayList<>();

    EditText textInput;
    Spinner hourSpinner;
    EditText minText;
    CheckBox chkMon;
    CheckBox chkTue;
    CheckBox chkWed;
    CheckBox chkThur;
    CheckBox chkFri;
    CheckBox chkSat;
    CheckBox chkSun;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            Toast.makeText(this, "Your phone's software is out of date, this function may not run properly", Toast.LENGTH_LONG).show();
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        textInput = findViewById(R.id.alarmLabel);
        hourSpinner = findViewById(R.id.hoursSpinner);
        minText = findViewById(R.id.minutesEditText);
        hourSpinner.setSelection(9);

        Calendar calNow = Calendar.getInstance();
        calSet = (Calendar) calNow.clone();
        checkDOTW();

        alarmList = getList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkDOTW() {
        chkMon = findViewById(R.id.monCheckBox);
        chkTue = findViewById(R.id.tueCheckBox);
        chkWed = findViewById(R.id.wedCheckBox);
        chkThur = findViewById(R.id.thursCheckBox);
        chkFri = findViewById(R.id.friCheckBox);
        chkSat = findViewById(R.id.satCheckBox);
        chkSun = findViewById(R.id.sunCheckBox);

        Calendar calNow = Calendar.getInstance();
        int day = calNow.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                chkMon.setChecked(true);
                break;
            case Calendar.TUESDAY:
                chkTue.setChecked(true);
                break;
            case Calendar.WEDNESDAY:
                chkWed.setChecked(true);
                break;
            case Calendar.THURSDAY:
                chkThur.setChecked(true);
                break;
            case Calendar.FRIDAY:
                chkFri.setChecked(true);
                break;
            case Calendar.SATURDAY:
                chkSat.setChecked(true);
                break;
            case Calendar.SUNDAY:
                chkSun.setChecked(true);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveAlarm(View view) {
        String label = textInput.getText().toString();
        int hour = hourSpinner.getSelectedItemPosition();
        int min = Integer.parseInt(minText.getText().toString());

        KeyListener keyListener = DigitsKeyListener.getInstance("1234567890");
        minText.setKeyListener(keyListener);
        if(min > 59) {
            minText.setText("00");
            Toast.makeText(this, "Invalid minutes", Toast.LENGTH_LONG).show();
        }

        RadioButton pmRadio = findViewById(R.id.pmRadio);
        if (pmRadio.isChecked()) { hour = hour + 12;}

        Alarm alarm = new Alarm(alarmList.size(), hour, min, 0, label);
        alarmList.add(alarm);
        int numAlarms = alarmList.size();

        if (chkMon.isChecked())
        {
            alarm.setDay(0);
            setAlarm(alarmList.get(numAlarms-1));
        }

        if (chkTue.isChecked())
        {
            alarm.setDay(1);
            setAlarm(alarmList.get(numAlarms-1));
        }

        if (chkWed.isChecked())
        {
            alarm.setDay(2);
            setAlarm(alarmList.get(numAlarms-1));
        }

        if (chkThur.isChecked())
        {
            alarm.setDay(3);
            setAlarm(alarmList.get(numAlarms-1));
        }

        if (chkFri.isChecked())
        {
            alarm.setDay(4);
            setAlarm(alarmList.get(numAlarms-1));
        }

        if (chkSat.isChecked())
        {
            alarm.setDay(5);
            setAlarm(alarmList.get(numAlarms-1));
        }

        if (chkSun.isChecked())
        {
            alarm.setDay(6);
            setAlarm(alarmList.get(numAlarms-1));
        }

        saveList("ReminderList", alarmList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm(Alarm alarm)
    {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calSet.set(Calendar.MINUTE, alarm.getMin());
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        Log.d("NewReminder",alarm.getHour()+":"+alarm.getMin()+"==================================================================");

        if(calSet.compareTo(Calendar.getInstance()) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
            Log.d("NewReminder", "Time already passed=========================================================");
        }

        Intent myIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        //myIntent.setAction(MyBroadcastReceiver.ACTION_ALARM_RECEIVER);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), alarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
        AlarmManager myAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //myAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), myPendingIntent);
        myAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), 24*60*60*1000, myPendingIntent);
        //check
        set("AlarmLabel", alarm.getLabel());
        Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
        Log.d("NewReminder", "ALARM SET======================================================================");
        onBackPressed();
    }

    public void saveList(String key, List<Alarm> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public List<Alarm> getList(){
        List<Alarm> arrayItems = null;
        String serializedObject = sharedpreferences.getString("ReminderList", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Alarm>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}