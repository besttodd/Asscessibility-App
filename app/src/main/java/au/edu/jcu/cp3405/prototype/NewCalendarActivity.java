package au.edu.jcu.cp3405.prototype;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ScrollView;

public class NewCalendarActivity extends AppCompatActivity {
    Context context;
    SoundManager soundManager;
    CustomKeyboard keyboard;
    EditText previousField;
    ScrollView scrollView;
    //private Button btngocalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calendar);
        context = this;
        soundManager = (SoundManager) getApplicationContext();

        EditText eTitle = findViewById(R.id.eventTitle);
        EditText eDateTime = findViewById(R.id.eventDateTime);
        EditText eNotes = findViewById(R.id.eventNotes);
        //Switch eNotifications = findViewById(R.id.eventNotifications);
        scrollView = findViewById(R.id.scroller);
        keyboard = findViewById(R.id.keyboard);
        setFocus(eTitle);
        //btngocalendar = findViewById(R.id.btngocalendar);

        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        eDateTime.setText(date);

        /*btngocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewCalendarActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });*/

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Make the custom keyboard appear
        eTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
            }
        });
        eDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setNumKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
                scrollView.smoothScrollTo(0, 500);
            }
        });
        eNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
                scrollView.smoothScrollTo(0, 800);
            }
        });

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = eTitle.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveEvent(View view) {
        soundManager.playSound(SoundManager.CONFIRM);
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);
    }

    public void setFocus(EditText view) {
        view.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style));
        if (previousField != null && previousField != view) {
            previousField.setBackground(getResources().getDrawable(R.drawable.edittext_style));
        }
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
        onBackPressed();
    }
}