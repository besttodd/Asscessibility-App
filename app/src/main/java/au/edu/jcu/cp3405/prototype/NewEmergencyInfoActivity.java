package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import static au.edu.jcu.cp3405.prototype.NewReminderActivity.MyPREFERENCES;

public class NewEmergencyInfoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 444;
    Context context;
    SoundManager soundManager;
    CustomKeyboard keyboard;
    EditText previousField;
    EditText conditions;
    EditText allergies;
    EditText medications;
    EditText bloodType;
    TextView eContactClicked;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_emergency_info);
        context = this;
        soundManager = (SoundManager) getApplicationContext();

        conditions = findViewById(R.id.conditions);
        allergies = findViewById(R.id.allergies);
        medications = findViewById(R.id.medications);
        bloodType = findViewById(R.id.bloodType);
        scrollView = findViewById(R.id.scroller);
        keyboard = findViewById(R.id.keyboard);
        setFocus(conditions);

        //Make custom
        conditions.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                setFocus((EditText) v);
                showCustomKeyboard((EditText) v);
            }
        });
        allergies.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setNumKeyboard();
                setFocus((EditText) v);
                showCustomKeyboard((EditText) v);
            }
        });
        medications.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                setFocus((EditText) v);
                showCustomKeyboard((EditText) v);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        bloodType.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                setFocus((EditText) v);
                showCustomKeyboard((EditText) v);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = conditions.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                String newName = data.getStringExtra("Name");
                String newNumber = data.getStringExtra("Number");
                //TODO save details to sharedpreferences
                eContactClicked.setText(newName);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void setFocus(EditText view) {
        view.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style));
        if (previousField != null) { previousField.setBackground(getResources().getDrawable(R.drawable.edittext_style)); }
        previousField = view;
    }

    public void showCustomKeyboard(EditText editText) {
        keyboard.setVisibility(View.VISIBLE);
        keyboard.setEnabled(true);
        keyboard.setPressed(false);
        InputConnection ic = editText.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        //if( isCustomKeyboardVisible() ) hideCustomKeyboard(); else this.finish();
        onBackPressed();
    }

    public void saveClicked(View view) {
        soundManager.playSound(SoundManager.CONFIRM);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("conditions", String.valueOf(conditions.getText()));
        editor.putString("allergies", String.valueOf(allergies.getText()));
        editor.putString("medications", String.valueOf(medications.getText()));
        editor.putString("blood type", String.valueOf(bloodType.getText()));
        //TODO get emergency contact preferences and save to sharedpreferences
        editor.apply();
        Toast.makeText(this, "Details Saved", Toast.LENGTH_LONG).show();
    }

    public void findContactClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, ViewContactsActivity.class);
        eContactClicked = (TextView) view;
        startActivityForResult(intent, REQUEST_CODE);
    }
}