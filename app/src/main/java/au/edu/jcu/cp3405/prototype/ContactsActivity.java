package au.edu.jcu.cp3405.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity {
    private static final int REQUEST_ACCESS_CONTACTS = 111;
    SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        soundManager = (SoundManager) getApplicationContext();

        boolean hasPermissionContacts = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionContacts) {
            ActivityCompat.requestPermissions(ContactsActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_ACCESS_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                //reload activity with permission granted
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "The app was not allowed access to your contacts. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void addNewClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivity(intent);
    }

    public void viewContactsClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, ViewContactsActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        onBackPressed();
    }
}