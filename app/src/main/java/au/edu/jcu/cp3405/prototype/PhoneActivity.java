package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PhoneActivity extends AppCompatActivity {
    SoundManager soundManager;
    boolean existingContact = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        soundManager = (SoundManager) getApplicationContext();
    }

    public void buttonClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Button button = (Button) view;
        TextView textView = findViewById(R.id.numberDisplay);
        String entering;

        String display = textView.getText().toString();
        String numberPressed = button.getText().toString();
        switch (display.length()) {
            case 2:
            case 7:
                entering = display + " " + numberPressed;
                break;
            default:
                entering = display + numberPressed;
        }
        textView.setText(entering);
    }

    public void deleteButton(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        TextView textView = findViewById(R.id.numberDisplay);
        String currentDisplay = textView.getText().toString();
        String newDisplay = "";
        if (currentDisplay.length() > 0) {
            newDisplay = currentDisplay.substring(0, currentDisplay.length() - 1);
        }
        textView.setText(newDisplay);
    }

    public void callClicked(View view) {
        soundManager.playSound(SoundManager.CONFIRM);
        TextView numberView = findViewById(R.id.numberDisplay);
        String number = numberView.getText().toString();
        Intent intent = new Intent(this, InCallActivity.class);
        intent.putExtra("NumberCalled", number);
        intent.putExtra("ExistingContact", existingContact);
        startActivity(intent);
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}