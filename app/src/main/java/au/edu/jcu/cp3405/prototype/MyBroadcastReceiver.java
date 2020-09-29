package au.edu.jcu.cp3405.prototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static au.edu.jcu.cp3405.prototype.NewReminderActivity.MyPREFERENCES;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "Reminders";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent pendingIntent) {

        //Context context=arg0.getApplicationContext();
        Intent intent = new Intent(context, ReminderAlertActivity.class);
        //This flag is required for starting an activity outside of an activity.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ACTIVITY_STARTED_FROM_BROADCAST_RECEIVER",true);
        context.startActivity(intent);

        //Light Up phone
        Toast.makeText(context, "Don't FORGET", Toast.LENGTH_LONG).show();
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Log.d("Receiver", "ALERT====================================================================" + date);

        //Play sound
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayer player = MediaPlayer.create(context.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)); //getRingtone(Context context, Uri ringtoneUri)
        try {
            if (audio != null) {
                float volume = audio.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                player.setVolume(volume / (float) 7.0, volume / (float) 7.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.start();

        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        //Delete alarm
    }
}
