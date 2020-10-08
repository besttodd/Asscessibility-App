package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ScrollView;

public class NewEmergencyInfoActivity extends AppCompatActivity {

    Context context;
    SoundManager soundManager;
    CustomKeyboard keyboard;
    EditText previousField;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_emergency_info);
        context = this;
        soundManager = (SoundManager) getApplicationContext();

        EditText conditions = findViewById(R.id.conditions);
        EditText allergies = findViewById(R.id.allergies);
        EditText medications = findViewById(R.id.medications);
        EditText bloodType = findViewById(R.id.bloodType);
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

    public void setFocus(EditText view) {
        view.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style));
        if (previousField != null) { previousField.setBackground(getResources().getDrawable(R.drawable.edittext_style)); }
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