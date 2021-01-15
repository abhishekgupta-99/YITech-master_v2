package com.freelance.school_attendance.RoomOfflinePersistence.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.freelance.school_attendance.RoomOfflinePersistence.entity.AttendanceRecordOffline;
import com.freelance.school_attendance.RoomOfflinePersistence.Dao.DaoAccess;

@Database(entities = {AttendanceRecordOffline.class}, version = 1, exportSchema = false)
public abstract class AttendanceOfflineDatabase extends RoomDatabase {
    private static final String LOG_TAG = AttendanceOfflineDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "db_task";
    private static AttendanceOfflineDatabase sInstance;


    public static AttendanceOfflineDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AttendanceOfflineDatabase.class, AttendanceOfflineDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract DaoAccess daoAccess();
}