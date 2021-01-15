package com.freelance.school_attendance.RoomOfflinePersistence.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.freelance.school_attendance.RoomOfflinePersistence.entity.AttendanceRecordOffline;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertTask(AttendanceRecordOffline att);


    @Query("SELECT * FROM AttendanceRecordOffline")
    List<AttendanceRecordOffline> fetchAllTasks();


    @Query("SELECT * FROM AttendanceRecordOffline WHERE id =:taskId")
    List<AttendanceRecordOffline> getTask(int taskId);


    @Update
    void updateTask(AttendanceRecordOffline att);


    @Delete
    void deleteTask(AttendanceRecordOffline att);
}
