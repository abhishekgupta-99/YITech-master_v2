package com.freelance.school_attendance;

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

public class ChooseRole extends AppCompatActivity {

    String master_sheet_url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        first_dialogboxlink();

    }

    public void openTeacherLogin(View v) {
      //  dialogboxlink();
        Intent i = new Intent(ChooseRole.this, SchoolLogin.class);
        i.putExtra("LoginAs", false);
        i.putExtra("master_url",master_sheet_url);
        startActivity(i);


    }

    public void openSchoolLogin(View view) {
        Intent i = new Intent(this, SchoolLogin.class);
        i.putExtra("master_url",master_sheet_url);
        i.putExtra("LoginAs", true);
        startActivity(i);

    }


    public void first_dialogboxlink()
    {

        SharedPrefSession sp;
        sp=new SharedPrefSession(getApplicationContext());

        if(sp.get_master_dialog_url_status())
        {
            master_sheet_url= sp.get_prev_master_dialog_url_entered();
        }
        else
        {
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
                                master_sheet_url=url;
                                flatDialog.dismiss();

                                //Toast.makeText(ClassSubjectDropDown.this, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChooseRole.this, "Please Enter A Valid Url", Toast.LENGTH_SHORT).show();
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


}