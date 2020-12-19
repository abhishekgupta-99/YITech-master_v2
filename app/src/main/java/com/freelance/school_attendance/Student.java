package com.freelance.school_attendance;

public class Student {

    public static final String TABLE_NAME = "student";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ROLL_NO = "roll_no";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_Last_Attendance = "last_attendance";
    public static final String COLUMN_percent_Attendance = "percent_attendance";
    private int id;
    private String note;
    private String timestamp;
    private String Last_Attendance;
    private String percent;


    // Create table SQL query test
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    //+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ROLL_NO + " INTEGER PRIMARY KEY ,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_Last_Attendance + " TEXT,"
                    + COLUMN_percent_Attendance + " REAL"
                    + ")";

    public Student() {
    }

    public Student(int id, String note, String timestamp, String Last_Attendance, String percent) {
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
        this.Last_Attendance = Last_Attendance;
        this.percent = percent;
    }

    public int getId() {
        return id;
    }

    public String getStudent() {
        return note;
    }

    public void setStudent(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
}
