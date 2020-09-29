package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static au.edu.jcu.cp3405.prototype.NewReminderActivity.MyPREFERENCES;

public class ReminderAlertActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_alert);

        soundManager = (SoundManager) getApplicationContext();

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String label = "Remember: " + sharedpreferences.getString("AlarmLabel", null);

        TextView reminderLabel = findViewById(R.id.reminderAlertView);
        reminderLabel.setText(label);

        //Delete alarm?
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        onBackPressed();
    }
}