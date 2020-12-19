package com.freelance.school_attendance;

public class Status {


    public String setstatus(String lecsattended, String totallecs) {

        Integer lectures_attended = Integer.parseInt(lecsattended);
        String default_status = "No Status";
        int requiredclassesfor75 = (75 * Integer.parseInt(totallecs)) / 100;


        if (lectures_attended > requiredclassesfor75) {
            int requiredClasses = lectures_attended - requiredclassesfor75;
            String status = "On track, May leave next " + requiredClasses + " lectures";
            return status;
        } else if (requiredclassesfor75 > lectures_attended) {
            int requiredClasses = requiredclassesfor75 - lectures_attended;
            String status = "Not on track, Need to attend next " + (requiredClasses) + " lectures";
            return status;
        } else
            return default_status;

    }
}
