package com.applandeo.materialcalendarview.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.R;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.applandeo.materialcalendarview.utils.DayColorsUtils;
import com.applandeo.materialcalendarview.utils.Detail;
import com.applandeo.materialcalendarview.utils.ImageUtils;
import com.applandeo.materialcalendarview.utils.SelectedDay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * This class is responsible for loading a one day cell.
 * <p>
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

class CalendarDayAdapter extends ArrayAdapter<Date> {
    private CalendarPageAdapter mCalendarPageAdapter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mMonth;
    private Calendar mToday = DateUtils.getCalendar();
    private ArrayList<Detail> detaillist;
    private CalendarProperties mCalendarProperties;

    private String uid;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private TextView daystring1;
    private TextView daystring2;

    private SimpleDateFormat a;
    private String newDate;

    CalendarDayAdapter(CalendarPageAdapter calendarPageAdapter, Context context, CalendarProperties calendarProperties,
                       ArrayList<Date> dates, int month) {
        super(context, calendarProperties.getItemLayoutResource(), dates);
        mCalendarPageAdapter = calendarPageAdapter;
        mContext = context;
        mCalendarProperties = calendarProperties;
        mMonth = month < 0 ? 11 : month;
        mLayoutInflater = LayoutInflater.from(context);
        //firebase Auth정보 get


    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = mLayoutInflater.inflate(mCalendarProperties.getItemLayoutResource(), parent, false);
        }
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        TextView dayLabel = view.findViewById(R.id.dayLabel);

        daystring2 = view.findViewById(R.id.daystring1);
        daystring1 = view.findViewById(R.id.daystring2);

        // Fri Dec 01 00:00:00 GMT+00:00 2017
        a = new SimpleDateFormat("yyyyMMdd");
        newDate = a.format(getItem(position));

        callDatabase();

        Calendar day = new GregorianCalendar();
        day.setTime(getItem(position));
        //Log.i("a",String.valueOf(getItem(position)));
        // Loading an image of the event

        if (isSelectedDay(day)) {
            // Set view for all SelectedDays
            Stream.of(mCalendarPageAdapter.getSelectedDays())
                    .filter(selectedDay -> selectedDay.getCalendar().equals(day))
                    .findFirst().ifPresent(selectedDay -> selectedDay.setView(dayLabel));

            DayColorsUtils.setSelectedDayColors(mContext, dayLabel, mCalendarProperties.getSelectionColor());
        } else if (isCurrentMonthDay(day) && isActiveDay(day)) { // Setting current month day color
            DayColorsUtils.setCurrentMonthDayColors(mContext, day, mToday, dayLabel, mCalendarProperties.getTodayLabelColor());

        } else { // Setting not current month day color
            DayColorsUtils.setDayColors(dayLabel, ContextCompat.getColor(mContext,
                    R.color.nextMonthDayColor), Typeface.NORMAL, R.drawable.background_transparent);
        }

        dateInsertMethod(newDate, daystring1, daystring2);

        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
        //equals.등록날짜

        return view;
    }

    private boolean isSelectedDay(Calendar day) {
        return mCalendarProperties.getCalendarType() != CalendarView.CLASSIC && day.get(Calendar.MONTH) == mMonth
                && mCalendarPageAdapter.getSelectedDays().contains(new SelectedDay(day));
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mMonth;
    }

    private boolean isActiveDay(Calendar day) {
        return !((mCalendarProperties.getMinimumDate() != null && day.before(mCalendarProperties.getMinimumDate()))
                || (mCalendarProperties.getMaximumDate() != null && day.after(mCalendarProperties.getMaximumDate())));
    }

    private void loadIcon(TextView text, Calendar day) {
        if (mCalendarProperties.getEventDays() == null || mCalendarProperties.getCalendarType() != CalendarView.CLASSIC) {
            text.setVisibility(View.GONE);
            return;
        }

        Stream.of(mCalendarProperties.getEventDays()).filter(eventDate ->
                eventDate.getCalendar().equals(day)).findFirst().executeIfPresent(eventDay -> {

            //ImageUtils.loadResource(dayIcon, eventDay.getImageResource());

            // If a day doesn't belong to current month then image is transparent
            // if (!isCurrentMonthDay(day) || !isActiveDay(day)) {
            //    dayIcon.setAlpha(0.2f);
            //  }

        });
    }

    void callDatabase() {
        //getDB
        DatabaseReference databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                detaillist = new ArrayList<>();

                for (DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("ledger").getChildren()) {
                    String id = (String) fileSnapshot.child("date").getValue();
                    String imgString = fileSnapshot.child("cateImageString").getValue(String.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //날짜 때려박는 함수
    void dateInsertMethod(String date, TextView textView, TextView textView1) {

        //10월 16일부터 31일까지
        if (newDate.equals("20171016")) {
            daystring1.setText("+4,000");
            daystring2.setText("-103,514");
        }
        if (newDate.equals("20171017")) {
            daystring2.setText("-30,200");
        }
        if (newDate.equals("20171018")) {
            daystring2.setText("-30,700");
        }
        if (newDate.equals("20171019")) {
            daystring2.setText("-6,900");
        }
        if (newDate.equals("20171020")) {
            daystring2.setText("-7,000");
        }
        if (newDate.equals("20171021")) {
            daystring2.setText("-59,976");
        }
        if (newDate.equals("20171022")) {
            daystring2.setText("-16,000");
        }
        if (newDate.equals("20171023")) {
            daystring2.setText("-37,900");
        }
        if (newDate.equals("20171024")) {
            daystring2.setText("-6,900");
        }
        if (newDate.equals("20171025")) {
            daystring2.setText("-16,800");
        }
        if (newDate.equals("20171026")) {
            daystring1.setText("+4,100");
            daystring2.setText("-18,200");
        }
        if (newDate.equals("20171027")) {
            daystring2.setText("-28,400");
        }
        if (newDate.equals("20171030")) {
            daystring1.setText("+415,140");
            daystring2.setText("-58,900");
        }
        if (newDate.equals("20171031")) {
            daystring2.setText("-14,500");
        }

        //11월 1일부터 26일 까지
        if (newDate.equals("20171101")) {
            daystring2.setText("-46,000");
        }
        if (newDate.equals("20171102")) {
            daystring1.setText("+75,750");
            daystring2.setText("-29,900");
        }
        if (newDate.equals("20171103")) {
            daystring2.setText("-48,350");
        }
        if (newDate.equals("20171104")) {
            daystring1.setText("+4,900");
            daystring2.setText("-9,900");
        }
        if (newDate.equals("20171105")) {
            daystring2.setText("-5,000");
        }
        if (newDate.equals("20171106")) {
            daystring2.setText("-32,000");
        }
        if (newDate.equals("20171107")) {
            daystring1.setText("+23,393");
            daystring2.setText("-80,000");
        }
        if (newDate.equals("20171108")) {
            daystring2.setText("-40,200");
        }
        if (newDate.equals("20171109")) {
            daystring1.setText("+12,666");
            daystring2.setText("-9,600");
        }
        if (newDate.equals("20171110")) {
            daystring1.setText("+380,250");
            daystring2.setText("-7,000");
        }
        if (newDate.equals("20171111")) {
            daystring1.setText("+26,800");
        }
        if (newDate.equals("20171113")) {
            daystring1.setText("+5,000");
            daystring2.setText("-121,233");
        }
        if (newDate.equals("20171114")) {
            daystring1.setText("+31,832");
            daystring2.setText("-42,300");
        }
        if (newDate.equals("20171115")) {
            daystring2.setText("-36,500");
        }
        if (newDate.equals("20171116")) {
            daystring1.setText("+15,766");
            daystring2.setText("-24,300");
        }
        if (newDate.equals("20171117")) {
            daystring2.setText("-14,750");
        }
        if (newDate.equals("20171118")) {
            daystring2.setText("-21,946");
        }
        if (newDate.equals("20171119")) {
            daystring2.setText("-23,900");
        }
        if (newDate.equals("20171120")) {
            daystring1.setText("+92,195");
            daystring2.setText("-105,665");
        }
        if (newDate.equals("20171121")) {
            daystring1.setText("+10,000");
            daystring2.setText("-4,300");
        }
        if (newDate.equals("20171123")) {
            daystring2.setText("-17,600");
        }
        if (newDate.equals("20171124")) {
            daystring2.setText("-9,700");
        }
        if (newDate.equals("20171125")) {
            daystring2.setText("-19,500");
        }
        if (newDate.equals("20171126")) {
            daystring1.setText("+10,500");
            daystring2.setText("-20,000");
        }
    }
}
