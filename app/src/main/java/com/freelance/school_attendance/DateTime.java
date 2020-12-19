package com.freelance.school_attendance;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class DateTime extends AppCompatActivity {
    static final int TIME_DIALOG_ID = 1111;

    final Calendar c = Calendar.getInstance();
    int hr = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);
    TextView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        view = findViewById(R.id.time);


    }

    public void TimePickerDialog(View view) {

        createdDialog().show();
    }


    protected Dialog createdDialog() {

        return new TimePickerDialog(DateTime.this, timePickerListener, hr, min, false);
    }

    public TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
// TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            //  updateTime(hr, min);
        }
    };

    private static String utilTime(int value) {
        if (value < 10) return "0" + value;
        else return String.valueOf(value);
    }


    public void updateTime(int hours, int mins, TextView view) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        view.setText(aTime);
    }
}

