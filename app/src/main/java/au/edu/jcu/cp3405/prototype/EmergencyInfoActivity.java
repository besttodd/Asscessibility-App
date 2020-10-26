package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static au.edu.jcu.cp3405.prototype.NewReminderActivity.MyPREFERENCES;

public class EmergencyInfoActivity extends AppCompatActivity {
    SoundManager soundManager;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_info);
        soundManager = (SoundManager) getApplicationContext();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        getSavedInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSavedInfo();
    }

    private void getSavedInfo() {
        TextView conditions = findViewById(R.id.conditions);
        TextView allergies = findViewById(R.id.allergies);
        TextView medications = findViewById(R.id.medications);
        TextView bloodType = findViewById(R.id.bloodType);
        TextView eContact1Name = findViewById(R.id.eContactName1);
        TextView eContact2Name = findViewById(R.id.eContactName2);

        conditions.setText(sharedpreferences.getString("conditions", null));
        allergies.setText(sharedpreferences.getString("allergies", null));
        medications.setText(sharedpreferences.getString("medications", null));
        bloodType.setText(sharedpreferences.getString("blood type", null));
        eContact1Name.setText(sharedpreferences.getString("eContact1Name", null));
        eContact2Name.setText(sharedpreferences.getString("eContact2Name", null));
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