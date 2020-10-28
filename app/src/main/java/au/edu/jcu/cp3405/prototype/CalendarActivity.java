package au.edu.jcu.cp3405.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private static final int REQUEST_ACCESS_CALENDAR = 222;
    private static final String TAG = "CalendarActivity";
    private static final String GOOGLE_USERNAME = "sebastianwilde22@gmail.com";
    Context context;
    SoundManager soundManager;
    long now;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        context = this;
        soundManager = (SoundManager) getApplicationContext();
        now = new Date().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        String nowString = dateFormat.format(calendar.getTime());
        Date selectedDate = null;
        try {
            selectedDate = dateFormat.parse(nowString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        long millis = selectedDate.getTime();

        boolean hasPermissionContacts = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionContacts) {
            ActivityCompat.requestPermissions(CalendarActivity.this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, REQUEST_ACCESS_CALENDAR);
        } else {
            try {
                readEvents(context, millis);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        CalendarView mCalendarView = findViewById(R.id.calendarView);
        //Change Month Text Size
        /*ViewGroup vg = (ViewGroup) mCalendarView.getChildAt(0);
        View child = vg.getChildAt(0);

        if(child instanceof TextView) {
            ((TextView)child).setTextSize(24);
            Log.d("CalendarView", "Change Text Size==========================================");
        }*/
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView CalendarView, int year, int month, int dayOfMonth) {
                soundManager.playSound(SoundManager.NEUTRAL);
                String dateString = dayOfMonth + "/" + (month + 1) + "/" + year;
                Date selectedDate = null;
                try {
                    selectedDate = dateFormat.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                long millis = selectedDate.getTime();
                Log.d("OnSelectedDateChange", "Day" + selectedDate.getDay() + "=====================================");
                /*Intent intent = new Intent(CalendarActivity.this, CalendarCompleteActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);*/
                try {
                    readEvents(context, millis);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_CALENDAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                //reload activity with permission granted
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "The app was not allowed access to your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addNewEventClicked(View view) {
        soundManager.playSound(SoundManager.NEUTRAL);
        Intent intent = new Intent(this, NewCalendarActivity.class);
        startActivity(intent);
    }

    public void readEvents(Context context, long selectedDate) throws ParseException, java.text.ParseException {
        ListView eventView = findViewById(R.id.eventListView);

        ContentResolver contentResolver = context.getContentResolver();

        long gCalendar = findCalendar();

        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/events"),
                new String[]{"calendar_id", "title", "description", "dtstart", "dtend", "eventLocation"}, "calendar_id =" + gCalendar, null, null);

        // Create a builder to define the time span
        Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate);
        Log.d("OnSelectedDateChange","Day: "+dateFormat.format(calendar.getTime())+"=====================================");*/
        // create the time span based on the inputs
        ContentUris.appendId(builder, selectedDate); //Same day
        ContentUris.appendId(builder, selectedDate + (DateUtils.HOUR_IN_MILLIS * 48)); //Next day

        // Create an event cursor to find all events in the calendar
        Cursor eventCursor = contentResolver.query(builder.build(),
                new String[]{"title", "begin", "end", "allDay"}, null,
                null, "startDay ASC, startMinute ASC");

        System.out.println("eventCursor count=" + eventCursor.getCount());
        builder.clearQuery();

        // If there are actual events in the current calendar, the count will exceed zero
        if(eventCursor.getCount()>0)
        {
            // Create a list of calendar events for the specific calendar
            List<CalendarEvent> eventList = new ArrayList<>();

            // Move to the first object
            eventCursor.moveToFirst();

            // Create an object of CalendarEvent which contains the title, when the event begins and ends,
            // and if it is a full day event or not
            CalendarEvent ce = loadEvent(eventCursor);

            // Adds the first object to the list of events
            eventList.add(ce);

            // While there are more events in the current calendar, move to the next instance
            while (eventCursor.moveToNext())
            {
                // Adds the object to the list of events
                ce = loadEvent(eventCursor);
                eventList.add(ce);
                Date importantDate = ce.getBegin();
            }

            Collections.sort(eventList);
            //eventMap.put(g, eventList);

            //System.out.println(eventMap.keySet().size() + " " + eventMap.values());

            CalendarAdapter calAdapter = new CalendarAdapter(context, eventList);
            eventView.setAdapter(calAdapter);
            calAdapter.notifyDataSetChanged();
            Log.d("readEvents", "Update events: " + Arrays.toString(eventList.toArray()) + "=============================================");
        }
        cursor.close();
    }

    private long findCalendar() {
        String[] projection = new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        @SuppressLint("MissingPermission") Cursor calCursor = getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI, projection,CalendarContract.Calendars.VISIBLE + " = 1",
                                null,CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                Log.d("Find Calendar", "Calendar name: " + displayName + id + "===================================================");
                if (displayName.equals(GOOGLE_USERNAME)) {
                    calCursor.close();
                    return id;
                }
            } while (calCursor.moveToNext());
        }
        calCursor.close();
        return -1;
    }

    /*private long getCalendarId() {
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection = CalendarContract.Calendars.ACCOUNT_NAME +
                        " = ? AND " +
                        CalendarContract.Calendars.ACCOUNT_TYPE +
                        " = ? ";
        // use the same values as above:
        String[] selArgs = new String[]{ MY_ACCOUNT_NAME};
        @SuppressLint("MissingPermission") Cursor cursor = getContentResolver().
                        query( CalendarContract.Calendars.CONTENT_URI,
                                projection, selection, selArgs, null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }*/

    // Returns a new instance of the calendar object
    private static CalendarEvent loadEvent(Cursor csr) {
        return new CalendarEvent(csr.getString(0),
                new Date(csr.getLong(1)),
                new Date(csr.getLong(2)),
                !csr.getString(3).equals("0"));
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}