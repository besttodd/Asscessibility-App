package au.edu.jcu.cp3405.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewContactActivity extends AppCompatActivity {
    Context context;
    SoundManager soundManager;
    CustomKeyboard keyboard;
    EditText previousField;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contacts);
        context = this;
        soundManager = (SoundManager) getApplicationContext();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        EditText contactName = findViewById(R.id.contactName);
        EditText contactNumber = findViewById(R.id.contactPhone);
        EditText contactEmail = findViewById(R.id.contactEmail);
        EditText contactAddress = findViewById(R.id.contactAddress);
        scrollView = findViewById(R.id.scroller);
        keyboard = findViewById(R.id.keyboard);
        setFocus(contactName);

        // Make the custom keyboard appear
        contactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideDefaultKeyboard((EditText) v);
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
            }
        });
        contactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideDefaultKeyboard((EditText) v);
                keyboard.setNumKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
                scrollView.smoothScrollTo(0, 500);
            }
        });
        contactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideDefaultKeyboard((EditText) v);
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
                scrollView.smoothScrollTo(0, 800);
            }
        });
        contactAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideDefaultKeyboard((EditText) v);
                keyboard.setLettersKeyboard();
                showCustomKeyboard((EditText) v);
                setFocus((EditText) v);
                scrollView.smoothScrollTo(0, 800);
            }
        });

        // pass the InputConnection from the EditText to the keyboard
        InputConnection ic = contactName.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    public void saveContactClicked(View view) {
        //Todo: Check input
        EditText input = findViewById(R.id.contactName);
        String name = input.getText().toString();
        //Todo: manipulate for first and last name
        input = findViewById(R.id.contactPhone);
        //int phoneNum = Integer.parseInt(input.getText().toString());
        String phoneNum = input.getText().toString();
        input = findViewById(R.id.contactEmail);
        String email = input.getText().toString();
        addContact(name, phoneNum, email);
        soundManager.playSound(SoundManager.CONFIRM);
    }

    private void addContact(String name, String phoneNum, String email) {
        ArrayList<ContentProviderOperation> contact = new ArrayList<>();
        contact.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // first and last names
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                //.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, given_name)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, name)
                .build());

        // Contact No Mobile
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNum)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        // Email
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, contact);
            Toast toast = Toast.makeText(context, "Contact added", Toast.LENGTH_LONG);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(30);
            toast.show();
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideCustomKeyboard() {
        keyboard.setVisibility(View.GONE);
        keyboard.setEnabled(false);
    }

    /*public void hideDefaultKeyboard(EditText view) {
        // assign the system service to InputMethodManager
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }*/

    public void showCustomKeyboard(EditText editText) {
        keyboard.setVisibility(View.VISIBLE);
        keyboard.setEnabled(true);
        InputConnection ic = editText.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
    }

    public void setFocus(EditText view) {
        view.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style));
        if (previousField != null && previousField != view) {
            previousField.setBackground(getResources().getDrawable(R.drawable.edittext_style));
        }
        previousField = view;
    }

    public boolean isCustomKeyboardVisible() {
        return keyboard.getVisibility() == View.VISIBLE;
    }

    public void onBackPressed(View view) {
        soundManager.playSound(SoundManager.CANCEL);
        if( isCustomKeyboardVisible() ) hideCustomKeyboard(); else this.finish();
        onBackPressed();
    }
}