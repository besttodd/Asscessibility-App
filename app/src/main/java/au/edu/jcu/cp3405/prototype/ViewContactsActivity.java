package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewContactsActivity extends AppCompatActivity implements StateListener {
    SoundManager soundManager;
    ArrayList<Contact> arrayOfContacts;
    ListView listView;
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        soundManager = (SoundManager) getApplicationContext();
        listView = findViewById(R.id.contactListView);
        getAllContacts();
    }

    @Override
    public void onUpdate(State state, Object view) {
        switch (state) {
            case UPDATE_CONTACTS:
                adapter.notifyDataSetChanged();
                getAllContacts();
                break;
            case SELECT_CONTACT:
                listItemClicked((View) view);
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
            adapter = new ContactsAdapter(this, arrayOfContacts);
            listView.setAdapter(adapter);
            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact contact = (Contact) arrayOfContacts.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("Name",  contact.getName());
                    intent.putExtra("Number",  contact.getNumber());
                    setResult(RESULT_OK, intent);
                    finish();
                    Log.d("ViewContacts", contact.getName()+"=================================================");
                }
            });*/
        }
        if (cur != null) {
            cur.close();
        }
    }

    public void listItemClicked(View view) {
        View parent = (View) view.getParent().getParent();
        TextView itemName = (TextView) parent.findViewById(R.id.nameItem);
        TextView itemNumber = (TextView) parent.findViewById(R.id.numberItem);
        Intent intent = new Intent();
        intent.putExtra("Name", itemName.getText());
        intent.putExtra("Number", itemNumber.getText());
        setResult(RESULT_OK, intent);
        finish();
        Log.d("ViewContacts", itemName.getText() + "=================================================");
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        onBackPressed();
    }
}