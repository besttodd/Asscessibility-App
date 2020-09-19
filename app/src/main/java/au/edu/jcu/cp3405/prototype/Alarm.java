package au.edu.jcu.cp3405.prototype;

import java.util.Calendar;

public class Alarm {
    int id;
    Calendar calendar;

    public Alarm(int id, Calendar cal) {
        this.id = id;
        calendar = cal;
    }
}
