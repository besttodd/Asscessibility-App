package au.edu.jcu.cp3405.prototype;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ReminderAdapter extends BaseAdapter {
    private Context context;
    private StateListener listener;
    private List<Reminder> reminders;

    ReminderAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
        listener = (StateListener) context;
    }

    public int getCount() {
        return reminders.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View child, ViewGroup parent) {
        Holder holder;
        LayoutInflater layoutInflater;
        final Button deleteButton;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            child = layoutInflater.inflate(R.layout.reminder_item, parent, false);
            deleteButton = child.findViewById(R.id.deleteButton);

            holder = new Holder();
            holder.textviewlabel = child.findViewById(R.id.textViewLabel);
            holder.textviewtime = child.findViewById(R.id.textViewTime);
            holder.textviewdays = child.findViewById(R.id.textViewDays);
            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
            deleteButton = child.findViewById(R.id.deleteButton);
        }

        holder.textviewlabel.setText(reminders.get(position).getLabel());
        holder.textviewtime.setText(getTime(reminders.get(position)));
        holder.textviewdays.setText(reminders.get(position).getDay());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast toast = Toast.makeText(context, "Reminder deleted.", Toast.LENGTH_LONG);
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(30);
                toast.show();
                Log.d("ReminderAdapter", "REMINDER DELETED===");
                listener.onUpdate(State.UPDATE_REMINDERS, position);
            }
        });

        return child;
    }

    private String getTime(Reminder reminder) {
        String mins;
        if (reminder.getMin() < 10) {
            mins = "0" + reminder.getMin();
        }else {
            mins = String.valueOf(reminder.getMin());
        }
        return reminder.getHour() + ":" + mins;
    }

    public static class Holder {
        TextView textviewlabel;
        TextView textviewtime;
        TextView textviewdays;
    }
}
