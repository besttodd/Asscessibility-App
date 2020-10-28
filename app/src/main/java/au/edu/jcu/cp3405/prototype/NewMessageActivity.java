package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class NewMessageActivity extends AppCompatActivity {
    Context context;
    SoundManager soundManager;
    ArrayList<Contact> arrayOfContacts;
    ListView listView;
    ContactsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        context = this;
        soundManager = (SoundManager) getApplicationContext();
        listView = findViewById(R.id.contactListView);
        getAllContacts();
    }

    private void getAllContacts() {
        arrayOfContacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                Contact contact = new Contact();
                contact.setName(name);
                contact.setId(id);
                String phoneNo;

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    assert pCur != null;
                    while (pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phoneNo.length() > 0)
                            contact.setPhone(phoneNo);
                    }
                    pCur.close();
                }
                arrayOfContacts.add(contact);
                Collections.sort(arrayOfContacts, new SortBasedOnName());
            }
            adapter = new ContactsAdapter(this, arrayOfContacts);
            listView.setAdapter(adapter);

            //Doesn't work
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("ItemClick", "CLICKED========================================");
                }
            });
        }
        if (cur != null) {
            cur.close();
        }
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        onBackPressed();
    }
}