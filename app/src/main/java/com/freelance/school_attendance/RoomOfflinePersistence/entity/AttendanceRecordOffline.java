package com.freelance.school_attendance.RoomOfflinePersistence.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.freelance.school_attendance.RoomOfflinePersistence.util.TimeStampConverter;
import java.io.Serializable;
import java.util.Date;

@Entity
public class AttendanceRecordOffline implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String action;
    private String absent;
    private String present;
    private String withpermission;
    private String _class;
    private String selected_teacher;
    private String teacher_email;
    private String selected_session;
    private String selected_subject;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getWithpermission() {
        return withpermission;
    }

    public void setWithpermission(String withpermission) {
        this.withpermission = withpermission;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getSelected_teacher() {
        return selected_teacher;
    }

    public void setSelected_teacher(String selected_teacher) {
        this.selected_teacher = selected_teacher;
    }

    public String getTeacher_email() {
        return teacher_email;
    }

    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }

    public String getSelected_session() {
        return selected_session;
    }

    public void setSelected_session(String selected_session) {
        this.selected_session = selected_session;
    }

    public String getSelected_subject() {
        return selected_subject;
    }

    public void setSelected_subject(String selected_subject) {
        this.selected_subject = selected_subject;
    }

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimeStampConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimeStampConverter.class})
    private Date modifiedAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

}
