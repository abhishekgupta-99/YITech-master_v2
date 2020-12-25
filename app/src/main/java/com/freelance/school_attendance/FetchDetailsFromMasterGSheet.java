package com.freelance.school_attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;
import com.freelance.school_attendance.HelperClass.Student_Item_Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class FetchDetailsFromMasterGSheet {

    ArrayList<String> teacherlist = new ArrayList<String>();
    ArrayList<String> subjectlist = new ArrayList<String>();
    ArrayList<String> classlist = new ArrayList<String>();
    ArrayList<String> sessionlist = new ArrayList<String>();
    String sc_name, sc_SIN, sc_pw;
    Context ctx;
    String Master_url;
    ProgressDialog indicator;


    public FetchDetailsFromMasterGSheet(Context ctx, ProgressDialog indicator, String master_url) {
        this.Master_url=master_url;
        this.ctx = ctx;
        this.indicator = indicator;
    }


    //This method is an important funtion to fetch all the data from the google sheet.
    // Please change the variable Master_url with your custom Master School Gsheet Url in the StringRequest line GET Method.
    public void getItems() {


        final RequestQueue queue = Volley.newRequestQueue(ctx);
        //  final String url = "https://script.google.com/macros/s/AKfycbxueYt0iOuJN6iPKJKG35CSKDegfuvQ3ls3yENsaefg2qVqGiS_/exec?action=getItems";
     //   StringRequest stringRequest = new StringRequest(Request.Method.GET, ctx.getString(R.string.Master_gs_url) +"?action=getItems",
              //  StringRequest stringRequest = new StringRequest(Request.Method.GET, Master_url +"?action=getItems",
          StringRequest stringRequest = new StringRequest(Request.Method.GET, Master_url +"?action=getSchool",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        parseItems(response);


                        Log.d("ANSRESPONSE", response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        SharedPrefSession sp;
                        sp=new SharedPrefSession(ctx);
                        Toast.makeText(ctx, "Error fetching Details !", Toast.LENGTH_LONG).show();
                        sp.set_master_dialog_url_status(false, Master_url);
                        error.printStackTrace();

                    }
                }
        )  {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        if(response!=null)
                        { SharedPrefSession sp;
                            sp=new SharedPrefSession(ctx);

                            if(response.statusCode== HttpURLConnection.HTTP_OK)
                            {

                                sp.set_master_dialog_url_status(true, Master_url);
                            }
                            else
                            {
                                sp.set_master_dialog_url_status(false, Master_url);
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
                        Log.d("FAILED CACHE", response.toString());
                        deliverResponse(response.result);
                        return;
                    }
                } else
                    super.deliverError(error);
            }
        };
        queue.add(stringRequest);

    }

// This method takes the response from the getItems() method and breaks the json response into our required variables as shown in the function.
    public void parseItems(String jsonResposnce) {
        ArrayList<Student_Item_Card> list = new ArrayList<Student_Item_Card>();


        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            Log.d("jsonobj", jobj.toString());
            JSONArray teacherarray = jobj.getJSONArray("teachers_name");
            JSONArray classarray = jobj.getJSONArray("classes");
            JSONArray subjectarray = jobj.getJSONArray("subjects");
            JSONArray schoolcredentials = jobj.getJSONArray("school_details");
            JSONArray sessionarray = jobj.getJSONArray("sessions");
            school_idpw(schoolcredentials);
            create_ArrayList(teacherarray, teacherlist);
            create_ArrayList(classarray, classlist);
            create_ArrayList(sessionarray, sessionlist);
            create_ArrayList(subjectarray, subjectlist);

           // indicator.setVisibility(View.GONE);
            indicator.dismiss();

            // initSpinner();

        } catch (JSONException e) {
            Toast.makeText(ctx, "You entered a wrong url ! ", Toast.LENGTH_LONG).show();
            SharedPrefSession sp;
            sp=new SharedPrefSession(ctx);
            sp.set_master_dialog_url_status(false, Master_url);
            e.printStackTrace();
        }


    }

    private void school_idpw(JSONArray schoolcredentials) throws JSONException {

        if (schoolcredentials != null) {
            for (int i = 0; i < schoolcredentials.length(); i++) {
                // list.add(array.getString(i));
                JSONObject obj = schoolcredentials.getJSONObject(i);
                sc_name = obj.getString("School Name");
                sc_SIN = obj.getString("SIN");
                sc_pw = obj.getString("School Password");


            }

        }

    }

    public void create_ArrayList(JSONArray array, ArrayList<String> list) throws JSONException {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }

        }

    }
}
