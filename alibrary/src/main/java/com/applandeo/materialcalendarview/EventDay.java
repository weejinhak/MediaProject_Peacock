package com.applandeo.materialcalendarview;

import com.applandeo.materialcalendarview.utils.DateUtils;

import java.util.Calendar;

/**
 * This class represents an event of a day. An instance of this class is returned when user click
 * a day cell. This class can be overridden to make calendar more functional. A list of instances of
 * this class can be passed to CalendarView object using setEvents() method.
 * <p>
 * Created by Mateusz Kornakiewicz on 23.05.2017.
 */

public class EventDay {
    private Calendar mDay;
    private int mImageResource;
    private String mtext;

    /**
     * @param day Calendar object which represents a date of the event
     */
    public EventDay(Calendar day) {
        mDay = day;
    }

    /**
     * @param day           Calendar object which represents a date of the event
     *
     */
    public EventDay(Calendar day, String text1) {
        DateUtils.setMidnight(day);
        mDay = day;
        mtext=text1;
    }


    /**
     * @return An image resource which will be displayed in the day row
     */
    public int getImageResource() {
        return mImageResource;
    }

    /**
     * @return Calendar object which represents a date of current event
     */
    public Calendar getCalendar() {
        return mDay;
    }
}
