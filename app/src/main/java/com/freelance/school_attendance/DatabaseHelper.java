package com.freelance.school_attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.freelance.school_attendance.HelperClass.Student_Item_Card;

import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    Student stu = new Student();

    // Database Name
    private static final String DATABASE_NAME = "student";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Students table
        db.execSQL(Student.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertStudent(String StudentName, String rno) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Student.COLUMN_NOTE, StudentName);
        values.put(Student.COLUMN_ROLL_NO, Integer.parseInt(rno));
        values.put(Student.COLUMN_Last_Attendance, NULL);
        values.put(Student.COLUMN_percent_Attendance, 20);

        // insert row
        long id = db.insert(Student.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Student getStudent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ROLL_NO, Student.COLUMN_NOTE, Student.COLUMN_TIMESTAMP},
                Student.COLUMN_ROLL_NO + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare Student object
        Student Student = new Student(
                cursor.getInt(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_Last_Attendance)),
                cursor.getString(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_percent_Attendance))
        );

        // close the db connection
        cursor.close();

        return Student;
    }

    public List<Student> getAllStudents() {
        List<Student> Students = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " ORDER BY " +
                Student.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student Student = new Student();
                Student.setId(cursor.getInt(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_ID)));
                Student.setStudent(cursor.getString(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_NOTE)));
                Student.setTimestamp(cursor.getString(cursor.getColumnIndex(com.freelance.school_attendance.Student.COLUMN_TIMESTAMP)));

                Students.add(Student);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Students list
        return Students;
    }

    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateStudent(Student Student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(com.freelance.school_attendance.Student.COLUMN_NOTE, Student.getStudent());

        // updating row
        return db.update(com.freelance.school_attendance.Student.TABLE_NAME, values, com.freelance.school_attendance.Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(Student.getId())});
    }

    public void deleteStudent(Student Student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(com.freelance.school_attendance.Student.TABLE_NAME, com.freelance.school_attendance.Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(stu.getId())});
        db.close();
    }


    public ArrayList<Student_Item_Card> getAllElements() {

        ArrayList<Student_Item_Card> list = new ArrayList<Student_Item_Card>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Student_Item_Card obj = new Student_Item_Card();
                        //only one column
                        obj.set_student_name(cursor.getString(1));
                        obj.set_roll_no(cursor.getString(0));
                        obj.setLast_Attendance(cursor.getString(3));
                        obj.setPercent(cursor.getString(4));

                        //you could add additional columns here..

                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }
}
