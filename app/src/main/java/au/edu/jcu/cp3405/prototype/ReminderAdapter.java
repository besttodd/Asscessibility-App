package au.edu.jcu.cp3405.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReminderAdapter extends BaseAdapter {
    private Context context;
    private List<Alarm> alarms;

    ReminderAdapter(Context context, List<Alarm> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    public int getCount() {
        return alarms.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;
        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            child = layoutInflater.inflate(R.layout.reminder_item, parent, false);

            holder = new Holder();
            holder.textviewlabel = child.findViewById(R.id.textViewLabel);
            holder.textviewtime = child.findViewById(R.id.textViewTime);
            holder.textviewdays = child.findViewById(R.id.textViewDays);
            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
        }

        holder.textviewlabel.setText(alarms.get(position).getLabel());
        holder.textviewtime.setText(getTime(alarms.get(position)));
        holder.textviewdays.setText(alarms.get(position).getDay());

        return child;
    }

    private String getTime(Alarm alarm) {
        return alarm.getHour() + ":" + alarm.getMin();
    }

    public static class Holder {
        TextView textviewlabel;
        TextView textviewtime;
        TextView textviewdays;
    }
}
