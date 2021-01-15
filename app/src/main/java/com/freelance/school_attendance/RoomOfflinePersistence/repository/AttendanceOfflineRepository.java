package com.freelance.school_attendance.RoomOfflinePersistence.repository;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.desmond.asyncmanager.AsyncManager;
import com.desmond.asyncmanager.TaskRunnable;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;
import com.freelance.school_attendance.RoomOfflinePersistence.entity.AttendanceRecordOffline;
import com.freelance.school_attendance.RoomOfflinePersistence.db.AttendanceOfflineDatabase;

import java.util.List;

public class AttendanceOfflineRepository {

    private String DB_NAME = "db_task";
    Context ctx;
    SharedPrefSession sp;

    private AttendanceOfflineDatabase attoffDatabase;
    private List<AttendanceRecordOffline> records;

    public AttendanceOfflineRepository(Context context, SharedPrefSession sp) {
        this.ctx = context;
        this.sp=sp;
        attoffDatabase = Room.databaseBuilder(context, AttendanceOfflineDatabase.class, DB_NAME).build();
    }


//    public void insertTask(String action,
//                           String absent,
//                           String present,
//                           String withpermission,
//                           String _class,
//                           String selected_teacher,
//                           String teacher_email,
//                           String selected_session,
//                           String selected_subject) {
//
//        insertTask(action,
//                absent,
//                present,
//                withpermission,
//                _class,
//                selected_teacher,
//                teacher_email,
//                selected_session,
//                selected_subject);
//    }

    public void insertTask(String action,
                           String absent,
                           String present,
                           String withpermission,
                           String _class,
                           String selected_teacher,
                           String teacher_email,
                           String selected_session,
                           String selected_subject) {

        AttendanceRecordOffline note = new AttendanceRecordOffline();
        note.setAction(action);
        note.setAbsent(absent);
        note.setPresent(present);
        note.setWithpermission(withpermission);
        note.set_class(_class);
        note.setSelected_teacher(selected_teacher);
        note.setSelected_session(selected_subject);
        note.setSelected_subject(selected_subject);
        note.setTeacher_email(teacher_email);

        //Toast.makeText(ctx, "Repo initiated", Toast.LENGTH_SHORT).show();
        insertTask(note);
    }

    public void insertTask(final AttendanceRecordOffline note) {

        AsyncManager.runBackgroundTask(new TaskRunnable<Void, String, Void>() {
            @Override
            public String doLongOperation(Void params) throws InterruptedException {
                // checkForThreadInterruption();
                // Your long operation
                attoffDatabase.daoAccess().insertTask(note);
                Log.d("Offline repo", note.getAction());
                //  Toast.makeText(ctx, "Repo saved", Toast.LENGTH_SHORT).show();
                return "Success";
            }

            // Override this callback if you need to handle the result on the UI thread
            @Override
            public void callback(String result) {
                // Handle the result from doLongOperation()
                sp.set_last_att_synced_status(false);
            }
        });

//        new AsyncTaskLoader<AttendanceRecordOffline>(ctx) {
//            @Override
//            protected void onStartLoading() {
//              //  super.onStartLoading();
//                forceLoad();
//            }
//
//            @Nullable
//            @Override
//            public AttendanceRecordOffline loadInBackground() {
//
//                return null;
//            }
//
//            @Override
//            protected void onStopLoading() {
//                super.onStopLoading();
//                cancelLoad();
//            }
//        };
    }

//    public void updateTask(final ContactsContract.CommonDataKinds.Note note) {
//        note.setModifiedAt(AppUtils.getCurrentDateTime());
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                noteDatabase.daoAccess().updateTask(note);
//                return null;
//            }
//        }.execute();
//    }

//    public void deleteTask(final int id) {
//        final List<AttendanceRecordOffline> task = getTask(id);
//        if (task != null) {
//            new AsyncTaskLoader<AttendanceRecordOffline>(ctx) {
//
//                @Nullable
//                @Override
//                public AttendanceRecordOffline loadInBackground() {
//                    attoffDatabase.daoAccess().deleteTask(task.getValue());
//                    return null;
//                }
//            };
//
//        }
//    }

    public void deleteTasks(final AttendanceRecordOffline offlinerecord) {

        AsyncManager.runBackgroundTask(new TaskRunnable<Void, String, Void>() {
            @Override
            public String doLongOperation(Void params) throws InterruptedException {
                // checkForThreadInterruption();
                // Your long operation
                attoffDatabase.daoAccess().deleteTask(offlinerecord);
                // Log.d("Offline repo", note.getAction());
                //  Toast.makeText(ctx, "Repo saved", Toast.LENGTH_SHORT).show();
                return "Success";
            }

            // Override this callback if you need to handle the result on the UI thread
            @Override
            public void callback(String result) {
                // Handle the result from doLongOperation()
            }
        });

//        new AsyncTaskLoader<AttendanceRecordOffline>(ctx) {
//            @Nullable
//            @Override
//            public AttendanceRecordOffline loadInBackground() {
//
//                return null;
//            }
//        };
    }


    public List<AttendanceRecordOffline> getTask(int id) {
        return attoffDatabase.daoAccess().getTask(id);
    }


    public List<AttendanceRecordOffline> getTasks() {
        AsyncManager.runBackgroundTask(new TaskRunnable<Void, List<AttendanceRecordOffline>, Void>() {
            @Override
            public List<AttendanceRecordOffline> doLongOperation(Void params) throws InterruptedException {
                // checkForThreadInterruption();
                // Your long operation
                List<AttendanceRecordOffline> recordsdata = attoffDatabase.daoAccess().fetchAllTasks();
                // Log.d("Offline repo", note.getAction());
                //  Toast.makeText(ctx, "Repo saved", Toast.LENGTH_SHORT).show();
                return recordsdata;
            }

            // Override this callback if you need to handle the result on the UI thread
            @Override
            public void callback(List<AttendanceRecordOffline> result) {
                // Handle the result from doLongOperation()
                records = result;
               // return records;
            }
        });

        return records;

    }

    public List<AttendanceRecordOffline> getRecords() {
        return records;
    }

}