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
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private static final int REQUEST_ACCESS_CALENDAR = 222;
    private static final String TAG = "CalendarActivity";
    private static final String GOOGLE_USERNAME = "sebastianwilde22@gmail.com";
    Context context;
    SoundManager soundManager;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        context = this;
        soundManager = (SoundManager) getApplicationContext();

        boolean hasPermissionContacts = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionContacts) {
            ActivityCompat.requestPermissions(CalendarActivity.this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, REQUEST_ACCESS_CALENDAR);
        }

        CalendarView mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView CalendarView, int year, int month, int dayOfMonth) {
                soundManager.playSound(SoundManager.NEUTRAL);
                date = year + "/" + month + "/" + dayOfMonth;
                Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + date);
                /*Intent intent = new Intent(CalendarActivity.this, CalendarCompleteActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);*/
                try {
                    readEvents(context);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            readEvents(context);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
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

    public void readEvents(Context context) throws ParseException, java.text.ParseException {
        ListView eventView = findViewById(R.id.eventListView);

        ContentResolver contentResolver = context.getContentResolver();

        long gCalendar = findCalendar();
        Log.d("Calendar", "Cal_ID: " + gCalendar + "============================================");

        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/events"),
                new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation" }, "calendar_id =" + gCalendar, null, null);

        // Create a builder to define the time span
        Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
        long now = new Date().getTime();

        // create the time span based on the inputs
        ContentUris.appendId(builder, now - (DateUtils.DAY_IN_MILLIS * 1) - (DateUtils.HOUR_IN_MILLIS * 24));
        ContentUris.appendId(builder, now + (DateUtils.DAY_IN_MILLIS * 1) + (DateUtils.HOUR_IN_MILLIS * 24));

        // Create an event cursor to find all events in the calendar
        Cursor eventCursor = contentResolver.query(builder.build(),
                new String[]  { "title", "begin", "end", "allDay"}, null,
                null, "startDay ASC, startMinute ASC");

        System.out.println("eventCursor count="+eventCursor.getCount());

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

            System.out.println(ce.toString());

            // While there are more events in the current calendar, move to the next instance
            while (eventCursor.moveToNext())
            {
                // Adds the object to the list of events
                ce = loadEvent(eventCursor);
                eventList.add(ce);

                System.out.println(ce.toString());
            }

            Collections.sort(eventList);
            //eventMap.put(g, eventList);

            //System.out.println(eventMap.keySet().size() + " " + eventMap.values());

            CalendarAdapter calAdapter = new CalendarAdapter(context, eventList);
            eventView.setAdapter(calAdapter);
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
}