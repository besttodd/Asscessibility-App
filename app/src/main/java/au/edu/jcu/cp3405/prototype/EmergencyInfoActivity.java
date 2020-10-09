package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static au.edu.jcu.cp3405.prototype.NewReminderActivity.MyPREFERENCES;

public class EmergencyInfoActivity extends AppCompatActivity {
    SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_info);
        soundManager = (SoundManager) getApplicationContext();
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TextView conditions = findViewById(R.id.conditions);
        TextView allergies = findViewById(R.id.allergies);
        TextView medications = findViewById(R.id.medications);
        TextView bloodType = findViewById(R.id.bloodType);

        conditions.setText(sharedpreferences.getString("conditions", null));
        allergies.setText(sharedpreferences.getString("allergies", null));
        medications.setText(sharedpreferences.getString("medications", null));
        bloodType.setText(sharedpreferences.getString("blood type", null));
        //TODO econtact names from sharedpreferences and update Buttons
    }

    public void editEmergencyContact(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, NewEmergencyInfoActivity.class);
        startActivity(intent);
    }

    public void callClicked(View view) {
        //TODO get number from sharedpreferences and open confirm box
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        onBackPressed();
    }
}