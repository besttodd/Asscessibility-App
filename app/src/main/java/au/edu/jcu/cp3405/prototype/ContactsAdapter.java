package au.edu.jcu.cp3405.prototype;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    Context context;
    ArrayList<Contact> contacts;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);
        }
        TextView contactName = convertView.findViewById(R.id.nameItem);
        TextView contactNumber = convertView.findViewById(R.id.numberItem);
        assert contact != null;
        contactName.setText(contact.name);
        contactNumber.setText(contact.mobileNum);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AdapterClick", "CLICKED========================================");
            }
        });
        return convertView;
    }
}
