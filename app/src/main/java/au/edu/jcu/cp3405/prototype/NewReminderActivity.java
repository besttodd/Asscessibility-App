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
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewReminderActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyAlarms" ;
    SoundManager soundManager;
    SharedPreferences sharedpreferences;
    Calendar calSet;
    List<Reminder> reminderList = new ArrayList<>();
    EditText previousField;
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
    CustomKeyboard keyboard;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        soundManager = (SoundManager) getApplicationContext();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            Toast.makeText(this, "Your phone's software is out of date, this function may not run properly", Toast.LENGTH_LONG).show();
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        textInput = findViewById(R.id.alarmLabel);
        hourSpinner = findViewById(R.id.hoursSpinner);
        minText = findViewById(R.id.minutesEditText);
        hourSpinner.setSelection(9);
        keyboard = findViewById(R.id.keyboard);
        setFocus(textInput);

        Calendar calNow = Calendar.getInstance();
        calSet = (Calendar) calNow.clone();
        checkDOTW();

        reminderList = getList();
        if(reminderList == null || reminderList.isEmpty()){
            reminderList = new ArrayList<>();
        }

        // Make the custom keyboard appear
        textInput.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
            }
        });
        minText.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setNumKeyboard();
                if (minText.getText().toString().equals("00")) {
                    minText.setText("");
                }
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
            }
        });

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = textInput.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
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
        soundManager.playSound(SoundManager.CONFIRM);
        String label = textInput.getText().toString();
        int hour = hourSpinner.getSelectedItemPosition();
        int min = Integer.parseInt(minText.getText().toString());

        KeyListener keyListener = DigitsKeyListener.getInstance("1234567890");
        minText.setKeyListener(keyListener);
        if(min > 59) {
            minText.setText(0);
            Toast toast = Toast.makeText(this, "Invalid minutes", Toast.LENGTH_LONG);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(30);
            toast.show();
        }

        RadioButton pmRadio = findViewById(R.id.pmRadio);
        if (pmRadio.isChecked()) { hour = hour + 12;}

        Reminder reminder;
        if(reminderList.isEmpty()) {
            reminder = new Reminder(0, hour, min, 0, label);
        }else {
            reminder = new Reminder(reminderList.size(), hour, min, 0, label);
        }
        reminderList.add(reminder);
        int numAlarms = reminderList.size();

        if (chkMon.isChecked())
        {
            reminder.setDay(0);
            setAlarm(reminderList.get(numAlarms-1));
        }

        if (chkTue.isChecked())
        {
            reminder.setDay(1);
            setAlarm(reminderList.get(numAlarms-1));
        }

        if (chkWed.isChecked())
        {
            reminder.setDay(2);
            setAlarm(reminderList.get(numAlarms-1));
        }

        if (chkThur.isChecked())
        {
            reminder.setDay(3);
            setAlarm(reminderList.get(numAlarms-1));
        }

        if (chkFri.isChecked())
        {
            reminder.setDay(4);
            setAlarm(reminderList.get(numAlarms-1));
        }

        if (chkSat.isChecked())
        {
            reminder.setDay(5);
            setAlarm(reminderList.get(numAlarms-1));
        }

        if (chkSun.isChecked())
        {
            reminder.setDay(6);
            setAlarm(reminderList.get(numAlarms-1));
        }

        saveList("ReminderList", reminderList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm(Reminder reminder)
    {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        calSet.set(Calendar.MINUTE, reminder.getMin());
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        Log.d("NewReminder", reminder.getHour()+":"+ reminder.getMin()+"==================================================================");

        if(calSet.compareTo(Calendar.getInstance()) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
            Log.d("NewReminder", "Time already passed=========================================================");
        }

        Intent myIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        myIntent.setAction(MyBroadcastReceiver.ACTION_ALARM_RECEIVER);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), reminder.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager myAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //myAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), myPendingIntent);
        assert myAlarmManager != null;
        myAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), 24*60*60*1000, myPendingIntent);
        //check
        //set("AlarmLabel", reminder.getLabel());
        set("AlarmId", String.valueOf(reminder.getId()));
        Toast toast = Toast.makeText(this, "Reminder set", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(30);
        toast.show();
        Log.d("NewReminder", "ALARM: "+reminder.getId()+" -SET======================================================================");
        onBackPressed();
    }

    public void saveList(String key, List<Reminder> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public List<Reminder> getList(){
        List<Reminder> arrayItems = null;
        String serializedObject = sharedpreferences.getString("ReminderList", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Reminder>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        if( isCustomKeyboardVisible() ) hideCustomKeyboard(); else this.finish();
        onBackPressed();
    }

    /*public void hideDefaultKeyboard(View view) {
        // this will give us the view which is currently focus in this layout
        //View view = this.getCurrentFocus();
        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // now assign the system service to InputMethodManager
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/

    public void setFocus(EditText view) {
        view.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style));
        if (previousField != null && previousField != view) { previousField.setBackground(getResources().getDrawable(R.drawable.edittext_style)); }
        previousField = view;
    }

    public void showCustomKeyboard(EditText editText) {
        keyboard.setVisibility(View.VISIBLE);
        keyboard.setEnabled(true);
        InputConnection ic = editText.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    public void hideCustomKeyboard() {
        keyboard.setVisibility(View.GONE);
        keyboard.setEnabled(false);
    }
    public boolean isCustomKeyboardVisible() {
        return keyboard.getVisibility() == View.VISIBLE;
    }
}