package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class ContactViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        String name = Objects.requireNonNull(getIntent().getExtras()).getString("Name");
        String number = getIntent().getExtras().getString("Number");
        TextView nameTextView = findViewById(R.id.contactName);
        TextView numberTextView = findViewById(R.id.contactNumber);

        nameTextView.setText(name);
        numberTextView.setText(number);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}