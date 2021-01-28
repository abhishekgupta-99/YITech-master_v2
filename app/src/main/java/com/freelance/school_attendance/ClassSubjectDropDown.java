package com.freelance.school_attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;
import com.freelance.school_attendance.HelperClass.Student_Item_Card;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ClassSubjectDropDown extends AppCompatActivity {

    private SmartMaterialSpinner subjectspinner;
    TextView sc_name;
    private SmartMaterialSpinner classspinner, teacherspinner;
    private List<String> provinceList;
    FetchDetailsFromMasterGSheet info;
    String selected_class, selected_teacher, selected_subject, selected_session;
    ArrayList<String> teacherlist = new ArrayList<String>();
    ArrayList<String> subjectlist = new ArrayList<String>();
    ArrayList<String> classlist = new ArrayList<String>();
    boolean loginAs;
    SharedPrefSession sp;
    private String master_url;
     ArrayList<String> sessionlist=new ArrayList<String>();
     SmartMaterialSpinner sessionspinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_down_menus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        sp=new SharedPrefSession(getApplicationContext());

        sc_name = findViewById(R.id.ed_scname);
        CircularProgressIndicator indicator = findViewById(R.id.indicator);


        //  info= new FetchDetailsSheet(this,indicator);
        //info.getItems();
        getIntentExtrasData();
        initSpinner();

        //  getItems();

    }

    private void getIntentExtrasData() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String j = (String) b.get("School name");
            teacherlist = (ArrayList<String>) getIntent().getSerializableExtra("Teacherlist");
            subjectlist = (ArrayList<String>) getIntent().getSerializableExtra("Subjectlist");
            classlist = (ArrayList<String>) getIntent().getSerializableExtra("Classlist");
            sessionlist=(ArrayList<String>) getIntent().getSerializableExtra("Sessionlist");
            loginAs = b.getBoolean("LoginAs");
            master_url=b.getString("master_url");

            sc_name.setText(j);
        }
    }


    private void initSpinner() {
        subjectspinner = findViewById(R.id.subjectspinner);
        classspinner = findViewById(R.id.classspinner);
        sessionspinner= findViewById(R.id.sessionspinner);
       // teacherspinner = findViewById(R.id.teacherspinner);


        provinceList = new ArrayList<>();

       // if((subjectlist==null && subjectlist.isEmpty())||(teacherlist==null && teacherlist.isEmpty())||(classlist==null && classlist.isEmpty()))
       if(!sp.get_dropdowndata_offline().equals(""))
        {


           ProgressDialog loading = ProgressDialog.show(this, "Loading", "Fetching Credentials", false, true);
            loading.setCanceledOnTouchOutside(false);
            loading.setCancelable(false);

           FetchDetailsFromMasterGSheet info = new FetchDetailsFromMasterGSheet(this, loading, sp.get_prev_master_dialog_url_entered());

                info.parseItemsOffline(sp.get_dropdowndata_offline());
            subjectlist=info.subjectlist;
            classlist=info.classlist;
            teacherlist=info.teacherlist;
            sessionlist=info.sessionlist;

        }
            else if((subjectlist==null || subjectlist.isEmpty()) && checkInternetConnectivity())
            {
                info.getItems();

                subjectlist=info.subjectlist;
                classlist=info.classlist;
                teacherlist=info.teacherlist;
                sessionlist=info.sessionlist;

//               ProgressDialog loading_studentsdata = ProgressDialog.show(this, "Loading", "Updating App", false, true);
//                loading_studentsdata.setCanceledOnTouchOutside(false);
//                loading_studentsdata.setCancelable(false);
//                GetAllClassesData getdata= new GetAllClassesData(this, classlist, sp, loading);
//                getdata.getStudentsDataForOneClass(0);
            }




       try {




           subjectspinner.setItem(subjectlist);
           classspinner.setItem(classlist);
           sessionspinner.setItem(sessionlist);

       }
       catch (Exception e)
       {
           Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
       }


       // teacherspinner.setItem(teacherlist);


        //indicator.setVisibility(View.GONE);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.VISIBLE);


    }

    private boolean checkInternetConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    private void getItems() {
//
//
//        final RequestQueue queue = Volley.newRequestQueue(this);
//        //  final String url = "https://script.google.com/macros/s/AKfycbxueYt0iOuJN6iPKJKG35CSKDegfuvQ3ls3yENsaefg2qVqGiS_/exec?action=getItems";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbz938aPu6B_i3RootSMc4JpFKgA09uMhEyUMsK6F9BT0ijJJAAT/exec?action=getItems",
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        parseItems(response);
//
//
//                        Log.d("ANSRESPONSE", response);
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//
//                    }
//                }
//        ) {
//            @Override
//            public void deliverError(VolleyError error) {
//                if (error instanceof NoConnectionError) {
//                    Cache.Entry entry = this.getCacheEntry();
//                    if (entry != null) {
//                        Response<String> response = parseNetworkResponse(new NetworkResponse(HttpURLConnection.HTTP_OK,
//                                entry.data, false, 0, entry.allResponseHeaders));
//                        Log.d("FAILED CACHE", response.toString());
//                        deliverResponse(response.result);
//                        return;
//                    }
//                } else
//                    super.deliverError(error);
//            }
//        };
//        queue.add(stringRequest);
//
//    }


    private void parseItems(String jsonResposnce) {
        ArrayList<Student_Item_Card> list = new ArrayList<Student_Item_Card>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            Log.d("jsonobj", jobj.toString());
            JSONArray teacherarray = jobj.getJSONArray("teachers_name");
            JSONArray classarray = jobj.getJSONArray("classes");
            JSONArray subjectarray = jobj.getJSONArray("subjects");
            JSONArray sessionarray = jobj.getJSONArray("sessions");

//            create_ArrayList(teacherarray,teacherlist);
//            create_ArrayList(classarray,classlist);
//            create_ArrayList(subjectarray,subjectlist);

            initSpinner();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void create_ArrayList(JSONArray array, ArrayList<String> list) throws JSONException {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }

        }

    }

    public void markAttendance() {

        if(selected_class.equals("null") || selected_subject.equals("null")||selected_session.equals("null"))
        {
            Toast.makeText(ClassSubjectDropDown.this, "Please Select From the Drop Down", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent i = new Intent(this, FetchStudentsAttendanceFromSlaveGsheet.class);
            i.putExtra("Teacher", sp.getTeacherName());
            i.putExtra("Class", selected_class);
            i.putExtra("Subject",selected_subject);
            i.putExtra("Session", selected_session);
            i.putExtra("LoginAs", loginAs);
            // i.putExtra("UserEnterUrl", url);
            startActivity(i);

        }

       // dialogboxlink();

      //  confirm_dropdown_selected();

    }

    public void confirm_dropdown_selected(View view) {

        subjectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // selected_subject=info.subjectlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ClassSubjectDropDown.this, "Please Select From the Drop Down", Toast.LENGTH_SHORT).show();
            }
        });

        classspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //  selected_class=info.classlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ClassSubjectDropDown.this, "Please Select From the Drop Down", Toast.LENGTH_SHORT).show();
            }
        });


        sessionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //  selected_class=info.classlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ClassSubjectDropDown.this, "Please Select From the Drop Down", Toast.LENGTH_SHORT).show();
            }
        });


        // Log.d("SELECTEEED",teacherspinner.getSelectedItem()+"");


//        teacherspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                // selected_teacher=info.teacherlist.get(position);
//                // Toast.makeText(ClassSubjectDropDown.this, info.teacherlist.get(position), Toast.LENGTH_SHORT).show();
//                //  Log.d("SELECTEDD", selected_teacher);
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(ClassSubjectDropDown.this, "Please Select From the Drop Down", Toast.LENGTH_SHORT).show();
//            }
//        });


      //  selected_teacher = teacherspinner.getSelectedItem() + "";


        selected_class = classspinner.getSelectedItem() + "";
        selected_subject = subjectspinner.getSelectedItem() + "";
        selected_session = sessionspinner.getSelectedItem() + "";

        markAttendance();


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, "Cannot navigate back! You are already signed In.", Toast.LENGTH_SHORT).show();
    }

    public void sync(View view) {

        ProgressDialog loading = ProgressDialog.show(this, "Loading", "Fetching Credentials", false, true);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(false);

        FetchDetailsFromMasterGSheet info = new FetchDetailsFromMasterGSheet(this, loading, sp.get_prev_master_dialog_url_entered());
        info.getItems();

        subjectlist=info.subjectlist;
        classlist=info.classlist;
        teacherlist=info.teacherlist;
        sessionlist=info.sessionlist;

        subjectspinner.setItem(subjectlist);
        classspinner.setItem(classlist);
        sessionspinner.setItem(sessionlist);


        //onBackPressed();
    }

    public void exit(View view) {
        //sp.logoutUser();
        this.finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        }
        finishAffinity();
       // addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
       // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.exit(0);
    }

//    public void first_dialogboxlink(View view)
//    {
//       // confirm_dropdown_selected();
//
//        SharedPrefSession sp;
//        sp=new SharedPrefSession(getApplicationContext());
//
//        if(sp.get_slave_dialog_url_status())
//        {
//            markAttendance(sp.get_prev_slave_dialog_url_entered());
//        }
//        else
//        {
//            final FlatDialog flatDialog = new FlatDialog(this);
//            flatDialog.setTitle("Gsheet Slave link")
//                    .setSubtitle("Please paste the Google Sheet Link here ! ")
//                    .setFirstTextFieldHint("Link")
//                    // .setSecondTextFieldHint("password")
//                    .setFirstButtonText("CONNECT")
//                    .setSecondButtonText("CANCEL")
//                    .setFirstButtonColor(R.color.md_blue_200)
//                    .setSecondButtonColor(ResourcesCompat.getColor(getResources(), R.color.white, null))
//                    .setSecondButtonTextColor(R.color.md_blue_200)
//                    .setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.md_blue_200, null))
//
//                    .withFirstButtonListner(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            String url = flatDialog.getFirstTextField();
//                            if (URLUtil.isValidUrl(url)) {
//                                markAttendance(url);
//                                flatDialog.dismiss();
//
//                                //Toast.makeText(ClassSubjectDropDown.this, "Success", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(ClassSubjectDropDown.this, "Please Enter A Valid Url", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                    })
//                    .withSecondButtonListner(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            flatDialog.dismiss();
//                        }
//                    })
//                    .show();
//        }
//    }
}