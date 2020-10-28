package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ViewContactsActivity extends AppCompatActivity implements StateListener {
    SoundManager soundManager;
    ArrayList<Contact> arrayOfContacts;
    ListView listView;
    LinearLayout confirmDelete;
    LinearLayout greyScreen;
    ConstraintLayout mainView;
    ContactsAdvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        soundManager = (SoundManager) getApplicationContext();
        listView = findViewById(R.id.contactListView);
        confirmDelete = findViewById(R.id.deleteConfirm);
        greyScreen = findViewById(R.id.greyScreen);
        mainView = findViewById(R.id.mainLayout);
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
                contactSelected((View) view);
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
                Collections.sort(arrayOfContacts, new SortBasedOnName());
            }
            adapter = new ContactsAdvAdapter(this, arrayOfContacts);
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

    public void contactSelected(View view) {
        View parent = (View) view.getParent().getParent();
        TextView itemName = parent.findViewById(R.id.nameItem);
        TextView itemNumber = parent.findViewById(R.id.numberItem);
        if (getIntent().getExtras() == null) {
            Intent intent = new Intent(this, ContactViewActivity.class);
            intent.putExtra("Name", itemName.getText());
            intent.putExtra("Number", itemNumber.getText());
            startActivity(intent);
        } else {
            //String requestingIntent = getIntent().getExtras().getString("RequestingIntent");
            Intent intent = new Intent();
            intent.putExtra("Name", itemName.getText());
            intent.putExtra("Number", itemNumber.getText());
            setResult(RESULT_OK, intent);
            finish();
            Log.d("ViewContacts", itemName.getText() + "=================================================");
        }
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        onBackPressed();
    }

    /*public void cancelDelete(View view) {
        confirmDelete.setVisibility(View.INVISIBLE);
        greyScreen.setVisibility(View.INVISIBLE);
        enableDisableView(mainView, true);
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }*/
}