package com.freelance.school_attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.freelance.school_attendance.HelperClass.CacheRequest;
import com.freelance.school_attendance.HelperClass.CheckInternetConnectivity;
import com.freelance.school_attendance.HelperClass.RecyclerViewAdapter;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;
import com.freelance.school_attendance.HelperClass.Student_Item_Card;
import com.freelance.school_attendance.model.UpdateAttendaceToSheetModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FetchStudentsAttendanceFromSlaveGsheet extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    ProgressDialog loading;
    private static FetchStudentsAttendanceFromSlaveGsheet mInstance;
    private RequestQueue mRequestQueue;
    SharedPrefSession sp;

    private DatabaseHelper db;
    EditText studentname;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Student_Item_Card> student_list;
    String absent_roll_nos="";
    String present_roll_nos="";
    String wp_roll_nos = "";
    TextView student,status,mark;
    TextView teacher, class_div, subject;
    String t, c, s, class_gs,sess;
    boolean loginAs;
    Button bt_save;
    private String userEnterUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
        sp=new SharedPrefSession(getApplicationContext());

        db = new DatabaseHelper(this);
        student = findViewById(R.id.student);
        teacher = findViewById(R.id.tv_teacher);
        class_div = findViewById(R.id.tv_class);
        subject = findViewById(R.id.tv_sub);
        bt_save= findViewById(R.id.save);
        status = findViewById(R.id.statusmain);
        mark = findViewById(R.id.markmain);
        // Button fab = findViewById(R.id.fab)
        getextraIntentData();
        updateUIRole();
        mRecyclerview();
        //updateUI();
        loading = ProgressDialog.show(this, "Loading", "Updating App", false, true);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(false);

        if(CheckInternetConnectivity.checkInternetConnectivity(this))
        {
            if(!sp.get_last_att_synced_status())
            {
                BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
                        .setTitle("Sync Last Attendance")
                        .setMessage("Please sync the last attendance before you proceed.")
                        .setCancelable(false)
                        .setAnimation(R.raw.uploading)
                        .setPositiveButton("Upload Last Attendance", new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                update_absent_students_GOOGLESHEET(sp.get_last_att_synced_status(),true);
                               // Update_Google_Sheet();
                            }
                        })
//                        .setNegativeButton("Cancel", new BottomSheetMaterialDialog.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int which) {
//                                //Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
//                                dialogInterface.dismiss();
//                            }
//                        })
                        .build();

                // Show Dialog
                mBottomSheetDialog.show();
            }
            else {
                getItems();
            }
        }
        else
        {
            parseItems(sp.get_studentsdata_offline(class_gs));

        }


//call this function for hardcoded set of values
        //student_list = dataset();


//Uncomment this for fav action click
//
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Add_Student_Dialog();

            }
        });*/

    }

    private void updateUIRole() {
        TextView stat = findViewById(R.id.statusmain);
        TextView radiogroup_tv = findViewById(R.id.markmain);
        if(!loginAs)
        {
            stat.setVisibility(View.GONE);
            //radiogroup_tv.setVisibility(View.GONE);
        }
        else
        {
            radiogroup_tv.setVisibility(View.INVISIBLE);
            bt_save.setVisibility(View.GONE);
        }
    }

    private void getextraIntentData() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();


        if (b != null) {
            t= b.getString("Teacher");
            s= b.getString("Subject");
            sess= b.getString("Session");
            String tv_t = "Teacher : " + b.getString("Teacher");
            class_gs = b.getString("Class");
            String tv_s = "Subject : " + b.getString("Subject");
            String tv_c = "Class : " + b.getString("Class") + "    Session : " + b.getString("Session");

            loginAs = b.getBoolean("LoginAs");
            userEnterUrl=b.getString("UserEnterUrl");
            teacher.setText(tv_t);
            class_div.setText(tv_c);
            subject.setText(tv_s);
        }

    }

    private void getItems() {


////////////////////////////////////////////////////////////////////////////////////////////////

        RequestQueue queue = Volley.newRequestQueue(this);
        // String url = "http://192.168.0.100/apitest";

        CacheRequest cacheRequest = new CacheRequest(0, getApplicationContext(), userEnterUrl, sp.get_prev_master_dialog_url_entered()+"?action=getStudents&class=" + class_gs, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                  //  JSONObject jsonObject = new JSONObject(jsonString);
                    sp.set_studentsdata_offline(class_gs,jsonString);

                    parseItems(jsonString);

                    Toast.makeText(FetchStudentsAttendanceFromSlaveGsheet.this, "onResponse:\n\n" + jsonString, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FetchStudentsAttendanceFromSlaveGsheet.this, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);
    }
////////////////////////////////////////////////////////////////////////////Working////////////

/*
        final RequestQueue q = Volley.newRequestQueue(this);
        // original     final String url = "https://script.google.com/macros/s/AKfycbxueYt0iOuJN6iPKJKG35CSKDegfuvQ3ls3yENsaefg2qVqGiS_/exec?action=getItems";
        // original  StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbxueYt0iOuJN6iPKJKG35CSKDegfuvQ3ls3yENsaefg2qVqGiS_/exec?action=getItems",


        // StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.Slave_gs_url) + "?action=getItems&class=" + class_gs,
        //StringRequest stringRequest = new StringRequest(Request.Method.GET,userEnterUrl + "?action=getItems&class=" + class_gs,
        StringRequest stringRequest = new StringRequest(Request.Method.GET, sp.get_prev_master_dialog_url_entered() + "?action=getStudents&class=" + class_gs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        SharedPrefSession sp;
                        sp = new SharedPrefSession(getApplicationContext());
                        sp.set_master_dialog_url_status(false, "");
                        Toast.makeText(FetchStudentsAttendanceFromSlaveGsheet.this, "Could not fetch details ! ", Toast.LENGTH_LONG).show();

                    }
                }
        ) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.d("RESPONSE HEADER", response.headers.toString());
                Log.d("RESPONSE data", response.data.toString());

                if (response != null) {
                    SharedPrefSession sp;
                    sp = new SharedPrefSession(getApplicationContext());

                    if (response.statusCode == HttpURLConnection.HTTP_OK) {

                        sp.set_slave_dialog_url_status(true, userEnterUrl);
                    } else {
                        sp.set_slave_dialog_url_status(false, userEnterUrl);
                    }

                }


                return super.parseNetworkResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Cache.Entry entry = this.getCacheEntry();
                    if (entry != null) {
                        Response<String> response = parseNetworkResponse(new NetworkResponse(HttpURLConnection.HTTP_OK,
                                entry.data, false, 0, entry.allResponseHeaders));
                        deliverResponse(response.result);
                        return;
                    }
                } else
                    super.deliverError(error);
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        q.add(stringRequest);

    }
*/

    private void parseItems(String jsonResposnce) {
        ArrayList<Student_Item_Card> list = new ArrayList<Student_Item_Card>();
        //  Log.d("jsonResponse",jsonResposnce);

        //   ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            Log.d("jsonobj", jobj.toString());
            JSONArray jarray = jobj.getJSONArray("items");
            Integer total_lecs = jobj.getInt("total_lecs");
            Log.d("totallecs", total_lecs + "");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                Student_Item_Card obj = new Student_Item_Card();
                String json_studentName = jo.getString("StudentsName");
                String json_rollno = jo.getString("Roll No");
                String json_percent_Attend = jo.getString("percentAttend");
                String json_lecs_attended = jo.getString("PresentLecs");

//                Log.d("name",json_studentName);
                //  String price = jo.getString("price");

                obj.set_student_name(json_studentName);
                obj.set_roll_no(json_rollno);
                obj.setPercent(json_percent_Attend + "");
                obj.setLast_Attendance("P");
                obj.setTotallecs(total_lecs + "");
                obj.setPresent_lecs(json_lecs_attended);
                // obj.setLast_Attendance(cursor.getString(3));
                //obj.setPercent(cursor.getString(4));


//                HashMap<String, String> item = new HashMap<>();
//                item.put("itemName", json_studentName);
//                item.put("brand", json_rollno);
                //  item.put("price",price);
                // Log.d("OBJECTT",json_percent_Attend.toString());

                list.add(obj);


            }
        } catch (JSONException e) {

            Toast.makeText(getApplicationContext(), "You entered a wrong url ! ", Toast.LENGTH_LONG).show();
            SharedPrefSession sp;
            sp=new SharedPrefSession(getApplicationContext());
            sp.set_master_dialog_url_status(false, "");
            e.printStackTrace();
            e.printStackTrace();
        }

        mAdapter = new RecyclerViewAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoginAs(loginAs);
        mAdapter.empty_Students_arraylist();
        loading.dismiss();


    }

    private void mRecyclerview() {

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    public static synchronized FetchStudentsAttendanceFromSlaveGsheet getInstance() {
        return mInstance;
    }

    private void updateUI() {

        DatabaseHelper dbh = new DatabaseHelper(FetchStudentsAttendanceFromSlaveGsheet.this);
        student_list = dbh.getAllElements();
        // alarm_listcopy=new ArrayList<>(alarm_list);
        mAdapter = new RecyclerViewAdapter(student_list);
        mRecyclerView.setAdapter(mAdapter);

    }





//    private ArrayList<Student_Item_Card> dataset() {
//
//        ArrayList<Student_Item_Card> each_student_data=new ArrayList<>();
//
//        each_student_data.add(new Student_Item_Card("Abhishek Gupta","17"));
//        each_student_data.add(new Student_Item_Card("Abhishek Gupta","17"));
//        each_student_data.add(new Student_Item_Card("Abhishek Gupta","17"));
//        each_student_data.add(new Student_Item_Card("Abhishek Gupta","17"));
//        each_student_data.add(new Student_Item_Card("Abhishek Gupta","17"));
//        each_student_data.add(new Student_Item_Card("Abhishek Gupta","17"));
//
//        return each_student_data;
//    }


    public void update_absent_students_GOOGLESHEET(final boolean syncstatus, final boolean isinternetavailable) {

        //https://script.google.com/macros/s/AKfycbzpTPG7TKiURwb017csK3aoBKakNUgmSf7utYSQuqixIqVLz3Q/exec
        final ProgressDialog loading = ProgressDialog.show(this, "Adding Details to Google Sheet", "Please wait");
        //original url    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxueYt0iOuJN6iPKJKG35CSKDegfuvQ3ls3yENsaefg2qVqGiS_/exec",
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sp.get_prev_master_dialog_url_entered(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sp.set_last_att_synced_status(true);

                        try {
                            mAdapter.empty_Students_arraylist();
                        }
                        catch (Exception e)
                        {
                            
                        }

                        //  Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        absent_roll_nos = "";
                        present_roll_nos="";
                        wp_roll_nos="";
                        loading.dismiss();
                       // getItems();
                        Intent intent = new Intent(getApplicationContext(),ClassSubjectDropDown.class);
                        intent.putExtra("LoginAs",loginAs);
                        startActivity(intent);

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
                Gson gson = new Gson();

                if(!syncstatus && isinternetavailable)
                {

                    String att_json_data= sp.get_lastattendance_json_offline();
                //  UpdateAttendaceToSheetModel model= gson.fromJson(sp.get_lastattendance_json_offline(), UpdateAttendaceToSheetModel.class);
                     hm = new Gson().fromJson(att_json_data, new TypeToken<HashMap<String, String>>(){}.getType());
                }

                if(syncstatus && isinternetavailable)
                {
                    hm=hashmapbuildertopostatt();
//                    String jsonString = gson.toJson(hm);
//                    sp.set_lastattendance_json_offline(jsonString);

                }



                return hm;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);

    }

    private Map<String, String> hashmapbuildertopostatt() {
        Map<String, String> parmas = new HashMap<>();
        parmas.put("action", "addAtt");
        Log.d("absentrolls", absent_roll_nos + "");
        parmas.put("absent", absent_roll_nos);
        parmas.put("present",present_roll_nos);
        parmas.put("withpermission",wp_roll_nos);
        parmas.put("class", class_gs);
        parmas.put("selected_teacher",t);
        parmas.put("teacher_email",sp.get_email_id_loggedin());
        parmas.put("selected_session",sess);
        parmas.put("selected_subject",s);

        return parmas;
    }

    public void Update_Google_Sheet() {

        //confirm_material_dialogbox();

        ArrayList<Student_Item_Card> absent_students = mAdapter.absentstudents();
        ArrayList<Student_Item_Card> present_students = mAdapter.getPresentStudents();
        ArrayList<Student_Item_Card> with_permission_students = mAdapter.getWithPermission();

        for (int i = 0; i < absent_students.size(); i++) {
            if(!absent_students.get(i).get_roll_no().equals("null")) {
                String absent_rollno = absent_students.get(i).get_roll_no();
                absent_roll_nos = absent_roll_nos + "," + absent_rollno;
            }
        }

        for (int i = 0; i < present_students.size(); i++) {
            if(!present_students.get(i).get_roll_no().equals("null")) {
                String present_rollno = present_students.get(i).get_roll_no();
                present_roll_nos = present_roll_nos + "," + present_rollno;
            }
        }

        for (int i = 0; i < with_permission_students.size(); i++) {
            if(!with_permission_students.get(i).get_roll_no().equals("null"))
            {
                String wp_rollno = with_permission_students.get(i).get_roll_no();
                wp_roll_nos = wp_roll_nos + "," + wp_rollno;
            }

        }



        Log.d("PPPPPPPPP", present_roll_nos);
        Log.d("AAAA",absent_roll_nos);
        Log.d("wwwwwwwwppp",wp_roll_nos);

        if(CheckInternetConnectivity.checkInternetConnectivity(this))
        {
            update_absent_students_GOOGLESHEET(true, true);
        }
        else
        {
            Map<String, String> hm = hashmapbuildertopostatt();
            Gson gson = new Gson();
            String jsonString = gson.toJson(hm);
            sp.set_lastattendance_json_offline(jsonString);
            sp.set_last_att_synced_status(false);
            loading.dismiss();
            Intent i=new Intent(this, ClassSubjectDropDown.class);
            i.putExtra("LoginAs",loginAs);
            startActivity(i);
        }


    }

    public void confirm_material_dialogbox(View view) {
        boolean internetAvailable= CheckInternetConnectivity.checkInternetConnectivity(this);

        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
                .setTitle(internetAvailable? "Upload?":"No Internet")
                .setMessage(internetAvailable?"Are you sure want to update this attendance to Google Sheet?":"You are offline. The attendance will be updated next time when you connect to Internet")
                .setCancelable(false)
                .setAnimation(internetAvailable? R.raw.uploading: R.raw.nointernet)
                .setPositiveButton(internetAvailable?"Upload":"Store Offline", new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                        Update_Google_Sheet();
                    }
                })
                .setNegativeButton("Cancel", new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mBottomSheetDialog.show();
    }

    public void markallpresent(View view) {
        present_roll_nos=",";
        absent_roll_nos="";
        wp_roll_nos="";
        for(int i=1;i<=mAdapter.getItemCount();i++)
        {
            present_roll_nos=present_roll_nos+i+",";
        }
        confirm_material_dialogbox(view);

    }
}
