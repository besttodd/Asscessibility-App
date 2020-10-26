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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static au.edu.jcu.cp3405.prototype.NewReminderActivity.MyPREFERENCES;

public class ReminderAlertActivity extends AppCompatActivity {

    private static final int SNOOZE_REMINDER_ID = 222;
    private static final int SNOOZE_DELAY = 15;
    SoundManager soundManager;
    SharedPreferences sharedpreferences;
    List<Reminder> reminderList = new ArrayList<>();
    Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_alert);

        soundManager = (SoundManager) getApplicationContext();

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = 0;
        if (sharedpreferences.getString("AlarmId", null) != null) {
            id = Integer.parseInt(Objects.requireNonNull(sharedpreferences.getString("AlarmId", null)));
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        reminderList = getList();

        reminder = reminderList.get(id);
        String message = "Remember\n" + reminder.getLabel();

        TextView reminderLabel = findViewById(R.id.reminderAlertView);
        reminderLabel.setText(message);

        //Delete alarm?
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
        onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSnoozePressed(View view) {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        //calSet.set(Calendar.DAY_OF_WEEK, reminder.getDay());
        calSet.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        calSet.set(Calendar.MINUTE, reminder.getMin()+SNOOZE_DELAY);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        //Set new alarm for 15mins later
        Intent myIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        myIntent.setAction(MyBroadcastReceiver.ACTION_ALARM_RECEIVER);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), SNOOZE_REMINDER_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager myAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert myAlarmManager != null;
        myAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), 24 * 60 * 60 * 1000, myPendingIntent);
        //check
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("AlarmId", String.valueOf(reminder.getId()));
        editor.apply();
        Toast toast = Toast.makeText(this, "Reminder Snoozed", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(30);
        toast.show();
        Log.d("NewReminder", "ALARM: " + reminder.getId() + " -SET===");
        onBackPressed();
    }
}