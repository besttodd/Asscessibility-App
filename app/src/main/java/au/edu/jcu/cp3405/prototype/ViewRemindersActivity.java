package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static au.edu.jcu.cp3405.prototype.NewReminderActivity.MyPREFERENCES;

public class ViewRemindersActivity extends AppCompatActivity implements StateListener {
    SoundManager soundManager;
    SharedPreferences sharedpreferences;
    List<Reminder> reminderList = new ArrayList<>();
    ReminderAdapter reminderAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminders);

        soundManager = (SoundManager) getApplicationContext();

        ListView reminderView = findViewById(R.id.reminderListView);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        reminderList = getList();

        if (reminderList != null) {
            //checking if alarm is working with pendingIntent
            for (int i = 0; i < reminderList.size(); i++) {
                Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);//the same as up
                intent.setAction(MyBroadcastReceiver.ACTION_ALARM_RECEIVER);//the same as up
                if (PendingIntent.getBroadcast(getApplicationContext(), reminderList.get(i).getId(), intent, PendingIntent.FLAG_NO_CREATE) != null) {
                    System.out.println("list id: " + reminderList.get(i).getId() + " is active========================================");
                    //Log.d("ViewReminders", "alarm is " + (isWorking ? "" : "not") + " working...");
                }
            }

            reminderAdapter = new ReminderAdapter(this, reminderList);
            reminderView.setAdapter(reminderAdapter);
            /*reminderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    view.animate().setDuration(2000).alpha(0)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    deleteReminder(position);
                                    reminderAdapter.notifyDataSetChanged();
                                    view.setAlpha(1);
                                }
                            });
                }

            });*/
        }
    }

    @Override
    public void onUpdate(State state, Object position) {
        if (state == State.UPDATE_REMINDERS) {
            deleteReminder((int) position);
            getList();
            reminderAdapter.notifyDataSetChanged();
        }
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

    private void deleteReminder(int position) {
        soundManager.playSound(SoundManager.CANCEL);
        //cancel actual alarm
        Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);//the same as up
        intent.setAction(MyBroadcastReceiver.ACTION_ALARM_RECEIVER);//the same as up
        AlarmManager myAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminderList.get(position).getId(), intent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent !=null ) {
            assert myAlarmManager != null;
            myAlarmManager.cancel(pendingIntent);
        }

        reminderList.remove(position);
        saveList("ReminderList", reminderList);
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

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        onBackPressed();
    }

    public void clearReminders(View view) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        sharedpreferences.edit().remove("ReminderList").apply();
        Log.d("ViewReminders", "Reminders Cleared=======================================");
    }
}