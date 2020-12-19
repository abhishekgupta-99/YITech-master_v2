package com.freelance.school_attendance.HelperClass;

import java.util.ArrayList;

public class Student_Item_Card {
    private String student_name;
    private String roll_no;
    private boolean isSelected;
    private int rg_id;
    private ArrayList<String> triggered_alarms;
    private String Last_Attendance;
    private String percent;

    public String getTotallecs() {
        return totallecs;
    }

    public void setTotallecs(String totallecs) {
        this.totallecs = totallecs;
    }

    private String totallecs;

    public String getPresent_lecs() {
        return present_lecs;
    }

    public void setPresent_lecs(String present_lecs) {
        this.present_lecs = present_lecs;
    }

    private String present_lecs;


    public Student_Item_Card(String student_name, String roll_no, String Last_Attendance, String percent) {
        this.student_name = student_name;
        this.roll_no = roll_no;
        this.Last_Attendance = Last_Attendance;
        this.percent = percent;

        //    this.triggered_alarms=alarms_triggered;
    }

//    public ArrayList<String> getTriggered_alarms() {
//        return triggered_alarms;
//    }
//
//    public void setTriggered_alarms(ArrayList<String> triggered_alarms) {
//        this.triggered_alarms = triggered_alarms;
//    }

    public Student_Item_Card() {

    }

    public String get_roll_no() {
        return roll_no;
    }

    public void set_roll_no(String roll_no) {
        this.roll_no = roll_no;
    }


    public String get_student_name() {
        return student_name;
    }

    public void set_student_name(String student_name) {
        this.student_name = student_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLast_Attendance() {
        return Last_Attendance;
    }

    public void setLast_Attendance(String last_Attendance) {
        Last_Attendance = last_Attendance;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public void setSelectedRadioId(int i) {
        rg_id=i;

    }

    public int getSelectedRadioId()
    {
        return rg_id;
    }
}
