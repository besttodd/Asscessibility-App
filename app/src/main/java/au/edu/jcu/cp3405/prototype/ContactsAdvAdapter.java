package au.edu.jcu.cp3405.prototype;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

public class ContactsAdvAdapter extends ArrayAdapter<Contact> {
    Context context;
    StateListener listener;

    public ContactsAdvAdapter(Context context, ArrayList<Contact> users) {
        super(context, 0, users);
        this.context = context;
        listener = (StateListener) context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item_adv, parent, false);
        }
        TextView contactName = convertView.findViewById(R.id.nameItem);
        TextView contactNumber = convertView.findViewById(R.id.numberItem);
        Button selectButton = convertView.findViewById(R.id.callButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        assert contact != null;
        contactName.setText(contact.name);
        contactNumber.setText(contact.mobileNum);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdate(State.SELECT_CONTACT, v);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteContact(getContext(), contact.mobileNum, contact.name)) {
                    Toast toast = Toast.makeText(context, "Contact deleted.", Toast.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView messageTextView = (TextView) group.getChildAt(0);
                    messageTextView.setTextSize(30);
                    toast.show();
                    Log.d("ContactsAdapter", "CONTACT DELETED");
                    listener.onUpdate(State.UPDATE_CONTACTS, v);
                } else {
                    Toast toast = Toast.makeText(context, "Contact not found.", Toast.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView messageTextView = (TextView) group.getChildAt(0);
                    messageTextView.setTextSize(30);
                    toast.show();
                    Log.d("ContactsAdapter", "CONTACT NOT FOUND");
                }
            }
        });

        convertView.setClickable(true);
        return convertView;
    }

    public static boolean deleteContact(Context ctx, String phone, String name) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur != null && cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        return true;
                    }

                } while (cur.moveToNext());
                cur.close();
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return false;
    }
}
