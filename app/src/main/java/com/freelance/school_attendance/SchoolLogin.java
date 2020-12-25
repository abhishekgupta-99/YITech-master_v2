package com.freelance.school_attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;
import com.google.android.material.textfield.TextInputEditText;

public class SchoolLogin extends AppCompatActivity {
    FetchDetailsFromMasterGSheet info;
    TextInputEditText SIN, pw;
    boolean loginAs;
    ProgressDialog loading;
    SharedPrefSession sp;
    String master_sheet_url="";
    private String master_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        SIN = findViewById(R.id.ed_sin);
        pw = findViewById(R.id.ed_pw);

        getBundleExtraData();
        first_dialogboxlink();
        sp=new SharedPrefSession(getApplicationContext());
       // CircularProgressIndicator indicator = findViewById(R.id.indicator);

        // initSpinner();

    }


    public void first_dialogboxlink() {

        SharedPrefSession sp;
        sp = new SharedPrefSession(getApplicationContext());

        if (sp.get_master_dialog_url_status()) {
            master_sheet_url = sp.get_prev_master_dialog_url_entered();
        } else {
            final FlatDialog flatDialog = new FlatDialog(this);
            flatDialog.setTitle("Gsheet Master link")
                    .setSubtitle("Please paste the Google Sheet Link here ! ")
                    .setFirstTextFieldHint("Link")
                    // .setSecondTextFieldHint("password")
                    .setFirstButtonText("CONNECT")
                    .setSecondButtonText("CANCEL")
                    .setFirstButtonColor(R.color.md_blue_200)
                    .setSecondButtonColor(ResourcesCompat.getColor(getResources(), R.color.white, null))
                    .setSecondButtonTextColor(R.color.md_blue_200)
                    .setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.md_blue_200, null))
                    .withFirstButtonListner(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String url = flatDialog.getFirstTextField();
                            if (URLUtil.isValidUrl(url)) {
                                master_sheet_url = url;
                                flatDialog.dismiss();

                                loading = ProgressDialog.show(SchoolLogin.this, "Loading", "Fetching Credentials", false, true);
                                loading.setCanceledOnTouchOutside(false);
                                loading.setCancelable(false);

                                info = new FetchDetailsFromMasterGSheet(SchoolLogin.this, loading,master_sheet_url);
                                info.getItems();


                                //Toast.makeText(ClassSubjectDropDown.this, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SchoolLogin.this, "Please Enter A Valid Url", Toast.LENGTH_SHORT).show();
                            }


                        }
                    })
                    .withSecondButtonListner(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            flatDialog.dismiss();
                        }
                    })
                    .show();
        }
    }
    private void getBundleExtraData() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            loginAs = b.getBoolean("LoginAs");
           // master_url=b.getString("master_url");

        }


    }

    public void goToDropDown(View view) {
        checkvalidSINpw();

    }

    private void checkvalidSINpw() {

        String sin = SIN.getText().toString().trim();
        String passw = pw.getText().toString().trim();
        if (sin.equals(info.sc_SIN) && passw.equals(info.sc_pw)) {
            Intent i = new Intent(this, SelectTeacher.class);
            i.putExtra("School name", info.sc_name + "");
            i.putExtra("Teacherlist", info.teacherlist);
            i.putExtra("Classlist", info.classlist);
            i.putExtra("Sessionlist",info.sessionlist);
            i.putExtra("Subjectlist", info.subjectlist);
            i.putExtra("LoginAs", loginAs);
            i.putExtra("master_url",master_url);
            sp.createLoginSession(loginAs);
            startActivity(i);
        } else {
            Toast.makeText(this, "Wrong Credentials ! Please Try Again", Toast.LENGTH_SHORT).show();

        }
    }

    public void back(View view) {
        onBackPressed();
    }
}