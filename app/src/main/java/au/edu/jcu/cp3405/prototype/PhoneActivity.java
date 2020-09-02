package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
    }

    public void buttonClicked(View view) {
        Button button = (Button) view;
        TextView textView = findViewById(R.id.numberDisplay);
        String entering;

        String display = textView.getText().toString();
        String numberPressed = button.getText().toString();
        switch (display.length()) {
            case 5:
            case 10:
                entering = display + " " + numberPressed;
                break;
            default:
                entering = display + numberPressed;
        }
        textView.setText(entering);
    }

    public void deleteButton(View view) {
        TextView textView = findViewById(R.id.numberDisplay);
        String currentDisplay = textView.getText().toString();
        String newDisplay = "";
        if (currentDisplay.length() > 0) {
            newDisplay = currentDisplay.substring(0, currentDisplay.length() - 1);
        }
        textView.setText(newDisplay);
    }
}