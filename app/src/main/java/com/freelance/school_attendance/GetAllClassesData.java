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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.freelance.school_attendance.HelperClass.CacheRequest;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class GetAllClassesData {
    Context ctx;
    ArrayList<String> classlist = new ArrayList<String>();
    SharedPrefSession sp;
    final int total_class;
    ProgressDialog loading;
    RequestQueue queue;

    public GetAllClassesData(Context ctx, ArrayList<String> classlist, SharedPrefSession sp, ProgressDialog loading) {
        this.ctx= ctx;
        this.classlist=classlist;
        this.sp=sp;
        this.loading=loading;
        total_class=classlist.size();
        //Toast.makeText(ctx, total_class+"", Toast.LENGTH_SHORT).show();

         queue = Volley.newRequestQueue(ctx);


        Log.d("Total class",total_class+"");
    }

    public void getStudentsDataForOneClass(final int i) {

        final String current_class= classlist.get(i);

        RequestQueue queue = Volley.newRequestQueue(ctx);
        // String url = "http://192.168.0.100/apitest";

        CacheRequest cacheRequest = new CacheRequest(0, ctx, sp.get_prev_master_dialog_url_entered()+"?action=getStudents&class=" + current_class, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    //  JSONObject jsonObject = new JSONObject(jsonString);
                    sp.set_studentsdata_offline(current_class+"",jsonString);

                    // parseItems(jsonString);

                    int temp_i=i;
                    int toast_i=i+1;
                   // Toast.makeText(ctx, "Class "+ current_class+ " Students Details Updated", Toast.LENGTH_SHORT).show();
                   // Log.d("int valueee", temp_i+"");

                    if(toast_i==total_class)
                    {
                        loading.dismiss();
                        Toast.makeText(ctx, "All Class Students Details Updated", Toast.LENGTH_SHORT).show();
                    }
                    if(++temp_i< total_class)
                    {
                        getStudentsDataForOneClass(temp_i);
                    }
//

                    // Toast.makeText(FetchStudentsAttendanceFromSlaveGsheet.this, "onResponse:\n\n" + jsonString, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(FetchStudentsAttendanceFromSlaveGsheet.this, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);
    }


    public void getItems(final int i) {

        final String current_class= classlist.get(i);

        //  final String url = "https://script.google.com/macros/s/AKfycbxueYt0iOuJN6iPKJKG35CSKDegfuvQ3ls3yENsaefg2qVqGiS_/exec?action=getItems";
        //   StringRequest stringRequest = new StringRequest(Request.Method.GET, ctx.getString(R.string.Master_gs_url) +"?action=getItems",
        //  StringRequest stringRequest = new StringRequest(Request.Method.GET, Master_url +"?action=getItems",
        StringRequest stringRequest = new StringRequest(0, sp.get_prev_master_dialog_url_entered()+"?action=getStudents&class=" + current_class,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        SharedPrefSession sp=new SharedPrefSession(ctx);
                        //sp.set_dropdowndata_offline(response);

                        sp.set_studentsdata_offline(current_class+"",response);
                        int temp_i=i;
                        int toast_i=i+1;
                        // Toast.makeText(ctx, "Class "+ current_class+ " Students Details Updated", Toast.LENGTH_SHORT).show();
                        // Log.d("int valueee", temp_i+"");

                        if(toast_i==total_class)
                        {
                            loading.dismiss();
                            Toast.makeText(ctx, "All Class Students Details Updated", Toast.LENGTH_SHORT).show();
                        }
                        if(++temp_i< total_class)
                        {
                            getItems(temp_i);
                        }


                       // Log.d("ANSRESPONSE", response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        SharedPrefSession sp;
                        sp=new SharedPrefSession(ctx);
                        Toast.makeText(ctx, "Error fetching Details !", Toast.LENGTH_LONG).show();
                       // indicator.dismiss();
                        //sp.set_master_dialog_url_status(false, Master_url);
                        error.printStackTrace();

                    }
                }
        )  {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if(response!=null)
                { SharedPrefSession sp;
                    sp=new SharedPrefSession(ctx);


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
}
