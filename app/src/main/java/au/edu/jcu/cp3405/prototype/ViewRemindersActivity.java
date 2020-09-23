package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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

public class ViewRemindersActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    List<Alarm> alarmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminders);

        ListView reminderView = findViewById(R.id.reminderListView);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        alarmList = getList();

        //checking if alarm is working with pendingIntent
        /*Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);//the same as up
        intent.setAction(MyBroadcastReceiver.ACTION_ALARM_RECEIVER);//the same as up
        boolean isWorking = (PendingIntent.getBroadcast(getApplicationContext(), alarmList.get(3).getId(), intent, PendingIntent.FLAG_NO_CREATE) != null);
        Log.d("ViewReminders", "alarm is " + (isWorking ? "" : "not") + " working...");*/

        ReminderAdapter reminderAdapter = new ReminderAdapter(this, alarmList);
        reminderView.setAdapter(reminderAdapter);



            /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final String item = (String) parent.getItemAtPosition(position);
                    view.animate().setDuration(2000).alpha(0)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    list.remove(item);
                                    adapter.notifyDataSetChanged();
                                    view.setAlpha(1);
                                }
                            });
                }

            });*/
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

    public void onBackPressed(View view) {
        onBackPressed();
    }
}