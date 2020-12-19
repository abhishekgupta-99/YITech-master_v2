package com.freelance.school_attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.freelance.school_attendance.HelperClass.SharedPrefSession;

import java.util.ArrayList;
import java.util.List;

public class SelectTeacher extends AppCompatActivity {
    private SmartMaterialSpinner teacherspinner;
    String selected_class, selected_teacher, selected_subject;
    ArrayList<String> teacherlist = new ArrayList<String>();
    ArrayList<String> subjectlist = new ArrayList<String>();
    ArrayList<String> classlist = new ArrayList<String>();
    boolean loginAs;
    SharedPrefSession sp;
     String sc_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teacher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
        sp=new SharedPrefSession(getApplicationContext());



        getIntentExtrasData();
    }

    public void proceed(View view) {

        teacherspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // selected_teacher=info.teacherlist.get(position);
                // Toast.makeText(ClassSubjectDropDown.this, info.teacherlist.get(position), Toast.LENGTH_SHORT).show();
                //  Log.d("SELECTEDD", selected_teacher);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SelectTeacher.this, "Please Select From the Drop Down", Toast.LENGTH_SHORT).show();
            }
        });

        selected_teacher = teacherspinner.getSelectedItem() + "";
        sp.set_teacher_name(selected_teacher);

        Intent i = new Intent(this, ClassSubjectDropDown.class);
        i.putExtra("School name", sc_name + "");
        i.putExtra("Teacherlist", teacherlist);
        i.putExtra("Classlist", classlist);
        i.putExtra("Subjectlist", subjectlist);
        i.putExtra("LoginAs", loginAs);
      //  sp.createLoginSession(loginAs);
        startActivity(i);

    }

    public void back(View view) {
    }


    private void getIntentExtrasData() {

        teacherspinner=findViewById(R.id.teacherspinner);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            sc_name = (String) b.get("School name");
            teacherlist = (ArrayList<String>) getIntent().getSerializableExtra("Teacherlist");
            subjectlist = (ArrayList<String>) getIntent().getSerializableExtra("Subjectlist");
            classlist = (ArrayList<String>) getIntent().getSerializableExtra("Classlist");
            loginAs = b.getBoolean("LoginAs");
        }
        teacherspinner.setItem(teacherlist);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.VISIBLE);

    }


}