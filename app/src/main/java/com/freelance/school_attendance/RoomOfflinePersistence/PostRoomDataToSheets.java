package com.freelance.school_attendance.RoomOfflinePersistence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.freelance.school_attendance.ClassSubjectDropDown;
import com.freelance.school_attendance.HelperClass.RecyclerViewAdapter;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;
import com.freelance.school_attendance.RoomOfflinePersistence.db.AttendanceOfflineDatabase;
import com.freelance.school_attendance.RoomOfflinePersistence.entity.AttendanceRecordOffline;
import com.freelance.school_attendance.RoomOfflinePersistence.repository.AttendanceOfflineRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRoomDataToSheets {
    SharedPrefSession sp;
    Context ctx;
    AttendanceRecordOffline record;
    String class_gs;
    RecyclerViewAdapter mAdapter;
    ProgressDialog loading;
    AttendanceOfflineDatabase attoff;

    public PostRoomDataToSheets(Context ctx, SharedPrefSession sp, String class_gs, RecyclerViewAdapter mAdapter, ProgressDialog loading, AttendanceOfflineDatabase attoffDatabase) {
        this.ctx=ctx;
        this.sp=sp;
        this.record=record;
        this.class_gs=class_gs;
        this.mAdapter=mAdapter;
        this.loading= loading;
        this.attoff =attoffDatabase;

    }

    public void post_roomdata_to_Gsheets(final List<AttendanceRecordOffline> att, final int i) {
        final AttendanceOfflineRepository repository = new AttendanceOfflineRepository(ctx, sp);
        final int total_att=att.size();

        //https://script.google.com/macros/s/AKfycbzpTPG7TKiURwb017csK3aoBKakNUgmSf7utYSQuqixIqVLz3Q/exec
       // final ProgressDialog loading = ProgressDialog.show(ctx, "Adding Offline Attendance to Google Sheet", "Please wait");
        //original url    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxueYt0iOuJN6iPKJKG35CSKDegfuvQ3ls3yENsaefg2qVqGiS_/exec",
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sp.get_prev_master_dialog_url_entered(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        repository.deleteTasks(att.get(i));
                       // attoff.daoAccess().deleteTask(att.get(i));
                        int temp_i=i;
                        int toast_i=i+1;
                        Toast.makeText(ctx, "Last "+ toast_i+"/"+total_att+ " Updated", Toast.LENGTH_SHORT).show();
                        Log.d("int valueee", temp_i+"");
                        if(++temp_i< total_att)
                        {
                            post_roomdata_to_Gsheets(att, temp_i);
                        }
                        else
                        {
                            loading.dismiss();
//                            if(total_att>0)
//                            {
//                                sp.set_last_att_synced_status(false);
//                            }
//                            else
//                            {
                                sp.set_last_att_synced_status(true);
//                            }

                            Intent intent = new Intent(ctx, ClassSubjectDropDown.class);
                            intent.putExtra("LoginAs",0);
                            ctx.startActivity(intent);
                        }



//                        try {
//                            mAdapter.empty_Students_arraylist();
//                        }
//                        catch (Exception e)
//                        {
//
//                        }

                      //  loading.dismiss();
                        // getItems();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> hm=new HashMap<String, String>();

                hm= hashmapbuildertopostatt(att.get(i));
                return hm;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(ctx);

        queue.add(stringRequest);

    }

    private Map<String, String> hashmapbuildertopostatt(AttendanceRecordOffline att) {
        Map<String, String> parmas = new HashMap<>();

        System.out.println("-----------------------");
        System.out.println(att.getAbsent());
        System.out.println(att.getSelected_session());
        System.out.println(att.getSelected_subject());
        System.out.println(att.getSelected_teacher());

        parmas.put("action", "addAtt");
        Log.d("absentrolls", att.getAbsent());
        parmas.put("absent", att.getAbsent());
        parmas.put("present",att.getPresent());
        parmas.put("withpermission",att.getWithpermission());
        parmas.put("class", att.get_class());
        parmas.put("selected_teacher",att.getSelected_teacher());
        parmas.put("teacher_email",sp.get_email_id_loggedin());
        parmas.put("selected_session",att.getSelected_session());
        parmas.put("selected_subject",att.getSelected_subject());

        return parmas;
    }
}
