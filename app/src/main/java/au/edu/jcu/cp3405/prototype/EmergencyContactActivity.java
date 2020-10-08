package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ScrollView;

public class EmergencyContactActivity extends AppCompatActivity {

    Context context;
    SoundManager soundManager;
    CustomKeyboard keyboard;
    EditText previousField;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        context = this;
        soundManager = (SoundManager) getApplicationContext();

        EditText eContactName = findViewById(R.id.eContactName);
        EditText eContactNumber = findViewById(R.id.eContactPhone);
        EditText eRequirements = findViewById(R.id.eRequirements);
        scrollView = findViewById(R.id.scroller);
        keyboard = findViewById(R.id.keyboard);
        setFocus(eContactName);

        //Make custom
        eContactName.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                setFocus((EditText) v);
                showCustomKeyboard((EditText) v);
            }
        });
        eContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setNumKeyboard();
                setFocus((EditText) v);
                showCustomKeyboard((EditText) v);
            }
        });
        eRequirements.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                keyboard.setLettersKeyboard();
                setFocus((EditText) v);
                showCustomKeyboard((EditText) v);
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = eContactName.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    public void setFocus(EditText view) {
        view.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
        if (previousField != null) { previousField.setBackgroundColor(getResources().getColor(R.color.colorBG)); }
        previousField = view;
    }

    public void showCustomKeyboard(EditText editText) {
        keyboard.setVisibility(View.VISIBLE);
        keyboard.setEnabled(true);
        InputConnection ic = editText.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        //if( isCustomKeyboardVisible() ) hideCustomKeyboard(); else this.finish();
        onBackPressed();
    }
}