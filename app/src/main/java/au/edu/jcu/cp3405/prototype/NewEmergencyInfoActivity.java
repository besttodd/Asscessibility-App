package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    SharedPreferences sharedpreferences;
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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        conditions = findViewById(R.id.conditions);
        allergies = findViewById(R.id.allergies);
        medications = findViewById(R.id.medications);
        bloodType = findViewById(R.id.bloodType);
        scrollView = findViewById(R.id.scroller);
        keyboard = findViewById(R.id.keyboard);
        //getSavedInfo();
        //setFocus(conditions);

        //Setup custom keyboard
        conditions.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
            }
        });
        allergies.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
            }
        });
        medications.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
                scrollView.smoothScrollTo(0, 500);
            }
        });
        bloodType.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
                scrollView.smoothScrollTo(0, 500);
            }
        });

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = conditions.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Save selected emergency contacts
        String keyName = "eContact1Name";
        String keyNumber = "eContact1Number";
        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                String newName = data.getStringExtra("Name");
                String newNumber = data.getStringExtra("Number");
                eContactClicked.setText(newName);
                if (eContactClicked == findViewById(R.id.eContact2)) {
                    keyName = "eContact2Name";
                    keyNumber = "eContact2Number";
                }
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(keyName, newName);
                editor.putString(keyNumber, newNumber);
                //put Image path in shared preferences
                editor.apply();
            }
            //Code for no result
        }
    }

    //Populate fields with existing data
    /*private void getSavedInfo() {
        TextView conditions = findViewById(R.id.conditions);
        TextView allergies = findViewById(R.id.allergies);
        TextView medications = findViewById(R.id.medications);
        TextView bloodType = findViewById(R.id.bloodType);
        TextView eContact1Name = findViewById(R.id.eContact1);
        TextView eContact2Name = findViewById(R.id.eContact2);

        conditions.setText(sharedpreferences.getString("conditions", null));
        allergies.setText(sharedpreferences.getString("allergies", null));
        medications.setText(sharedpreferences.getString("medications", null));
        bloodType.setText(sharedpreferences.getString("blood type", null));
        eContact1Name.setText(sharedpreferences.getString("eContact1Name", null));
        eContact2Name.setText(sharedpreferences.getString("eContact2Name", null));
    }*/

    public void setFocus(EditText view) {
        view.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style));
        //view.setText("");
        if (previousField != null && previousField != view) {
            previousField.setBackground(getResources().getDrawable(R.drawable.edittext_style));
        }
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
        //TODO check input
        soundManager.playSound(SoundManager.CONFIRM);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("conditions", String.valueOf(conditions.getText()));
        editor.putString("allergies", String.valueOf(allergies.getText()));
        editor.putString("medications", String.valueOf(medications.getText()));
        editor.putString("blood type", String.valueOf(bloodType.getText()));
        editor.apply();
        Toast toast = Toast.makeText(this, "Details Saved", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(30);
        toast.show();
        onBackPressed();
    }

    public void findContactClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, ViewContactsActivity.class);
        eContactClicked = (TextView) view;
        intent.putExtra("RequestingActivity", "EmergencyContact");
        startActivityForResult(intent, REQUEST_CODE);
    }
}