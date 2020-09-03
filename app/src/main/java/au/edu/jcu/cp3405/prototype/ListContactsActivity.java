package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ListContactsActivity extends AppCompatActivity implements StateListener {
    ArrayList<Contact> arrayOfContacts;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        listView = findViewById(R.id.contactListView);
        getAllContacts();
    }

    @Override
    public void onUpdate(State state) {
        switch (state) {
            case UPDATE_CONTACTS:
                getAllContacts();
                break;
        }
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
                        Log.e("phone", phoneNo);
                        if (phoneNo.length() > 0)
                            contact.setPhone(phoneNo);
                    }
                    pCur.close();
                }
                arrayOfContacts.add(contact);
            }
            ContactsAdapter adapter = new ContactsAdapter(this, arrayOfContacts);
            listView.setAdapter(adapter);
        }
        if (cur != null) {
            cur.close();
        }
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}