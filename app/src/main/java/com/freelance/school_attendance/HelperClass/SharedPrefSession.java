package com.freelance.school_attendance.HelperClass;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.freelance.school_attendance.ChooseRole;
import com.freelance.school_attendance.ClassSubjectDropDown;
import com.freelance.school_attendance.R;
import com.freelance.school_attendance.SchoolLogin;

import java.util.HashMap;

public class SharedPrefSession {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AttendancePref";

    private static final String IS_LOGIN_AS= "Role";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";



    private static final String TEACHER_NAME = "teacher";

    private static final String IS_SLAVE_DIALOGBOX_USER_ENTERED_URL_CORRECT = "IsSlaveDialogUrlCorrect";

    private static final String SLAVE_DIALOGBOX_USER_ENTERED_URL= "SlaveDialogGUrl";


    private static final String IS_MASTER_DIALOGBOX_USER_ENTERED_URL_CORRECT = "IsMasterDialogUrlCorrect";

    private static final String MASTER_DIALOGBOX_USER_ENTERED_URL= "MasterDialogGUrl";




    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SharedPrefSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(boolean role){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putBoolean(IS_LOGIN_AS, role);

        // Storing email in pref
       // editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLoginandRedirect(){
        // Check login status
        if(get_master_dialog_url_status()) {
//
//            if (this.isSchool()) {
//                // user is not logged in redirect him to Login Activity
//                Intent i = new Intent(_context, ClassSubjectDropDown.class);
//                i.putExtra("LoginAs", true);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                // Add new Flag to start new Activity
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                _context.startActivity(i);
//            } else
//                {
                Intent i = new Intent(_context, ClassSubjectDropDown.class);
                i.putExtra("LoginAs", false);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);

            }
 //       }
        else {
            Intent i = new Intent(_context, SchoolLogin.class);
         //   i.putExtra("LoginAs", false);
            i.putExtra("LoginAs",false);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);

        }

    }

    public boolean isSchool() {
        return pref.getBoolean(IS_LOGIN_AS, false);

    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
//        // Closing all the Activities
        Intent i = new Intent(_context, ChooseRole.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
}

public void set_slave_dialog_url_status(boolean url_correct, String url)
{
    editor.putBoolean(IS_SLAVE_DIALOGBOX_USER_ENTERED_URL_CORRECT,url_correct);
    editor.putString(SLAVE_DIALOGBOX_USER_ENTERED_URL,url);
    editor.commit();
}

public void set_master_dialog_url_status(boolean url_correct, String url)
    {
        editor.putBoolean(IS_MASTER_DIALOGBOX_USER_ENTERED_URL_CORRECT,url_correct);
        editor.putString(MASTER_DIALOGBOX_USER_ENTERED_URL,url);
        editor.commit();
    }

public void set_teacher_name(String teacher)
{
        editor.putString(TEACHER_NAME,teacher);
        editor.commit();
    }

public boolean get_slave_dialog_url_status()
{
   return pref.getBoolean(IS_SLAVE_DIALOGBOX_USER_ENTERED_URL_CORRECT,false);

}

public String get_prev_slave_dialog_url_entered()
{
    return pref.getString(SLAVE_DIALOGBOX_USER_ENTERED_URL,_context.getString(R.string.Slave_gs_url));
}


    public boolean get_master_dialog_url_status()
    {
        return pref.getBoolean(IS_MASTER_DIALOGBOX_USER_ENTERED_URL_CORRECT,false);

    }

    public String get_prev_master_dialog_url_entered()
    {
        return pref.getString(MASTER_DIALOGBOX_USER_ENTERED_URL,_context.getString(R.string.Master_gs_url));
    }

    public String getTeacherName() {
        return pref.getString(TEACHER_NAME,"");
    }
}