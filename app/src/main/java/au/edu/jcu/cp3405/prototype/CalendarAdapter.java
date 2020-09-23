package au.edu.jcu.cp3405.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<CalendarEvent> events;

    CalendarAdapter(Context context, List<CalendarEvent> events) {
        this.context = context;
        this.events = events;
    }

    public int getCount() {
        return events.size();
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
            child = layoutInflater.inflate(R.layout.calendar_event, parent, false);

            holder = new Holder();
            holder.textviewevent = child.findViewById(R.id.textViewEventTitle);
            holder.textviewtime = child.findViewById(R.id.textViewTime);
            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
        }

        holder.textviewevent.setText(events.get(position).getTitle());
        holder.textviewtime.setText(simpleTime(events.get(position).getBegin().toString()));

        return child;
    }

    private String simpleTime(String detailedTime) {
        String[] separated = detailedTime.split(" ");
        String[] time = separated[3].split(":");
        String newTime = time[0] + ":" + time[1];
        return separated[0] + " " + separated[1] + " " + separated[2] + " at " + newTime;
    }

    public static class Holder {
        TextView textviewevent;
        TextView textviewtime;
    }
}
