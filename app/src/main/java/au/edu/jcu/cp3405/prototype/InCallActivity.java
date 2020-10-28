package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class InCallActivity extends AppCompatActivity {
    SoundManager soundManager;
    boolean muted = false;
    boolean speaker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_call);
        soundManager = (SoundManager) getApplicationContext();

        TextView title = findViewById(R.id.titleTextView);
        String numberCalled = Objects.requireNonNull(getIntent().getExtras()).getString("NumberCalled");
        boolean existingContact = getIntent().getBooleanExtra("ExistingContact", false);
        if (!existingContact) {
            assert numberCalled != null;
            title.setText(setTitle(numberCalled));
        }
    }

    public String setTitle(String numberCalled) {
        return "-----------------------\n"
                + "In Call - " + numberCalled.replace(" ", "")
                + "\n---------00:34--------";
    }

    public void endCall(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        Intent intent = new Intent(this, PhoneActivity.class);
        startActivity(intent);
    }

    public void muteClicked(View view) {
        Button muteButton = (Button) view;
        if (muted) {
            muteButton.setTextColor(getResources().getColor(R.color.white));
            muted = false;
        } else {
            muteButton.setTextColor(getResources().getColor(R.color.colorLightGreen));
            muted = true;
        }
    }

    public void speakerClicked(View view) {
        Button speakerButton = (Button) view;
        if (speaker) {
            speakerButton.setTextColor(getResources().getColor(R.color.white));
            speaker = false;
        } else {
            speakerButton.setTextColor(getResources().getColor(R.color.colorLightGreen));
            speaker = true;
        }
    }
}