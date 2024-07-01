package com.sajiman.mychat.utility;

import com.sajiman.mychat.R;

import java.util.ArrayList;
import java.util.List;

public class SignUpUtils {

    public static String getGender(int radiobuttonChecked) {
        switch (radiobuttonChecked) {
            case R.id.rbMale:
                return "Male";
            case R.id.rbFemale:
                return "Female";
            default:
                return null;
        }
    }

    public static List<String> getDays() {
        List<String> dayList = new ArrayList<>();
        dayList.add("Day");
        for (int count = 1; count <= 32; count++) {
            dayList.add(String.valueOf(count));
        }
        return dayList;
    }

    public static List<String> getMonths() {
        List<String> monthList = new ArrayList<>();
        monthList.add("Month");
        monthList.add("JAN");
        monthList.add("FEB");
        monthList.add("MAR");
        monthList.add("APR");
        monthList.add("MAY");
        monthList.add("JUN");
        monthList.add("JUL");
        monthList.add("AUG");
        monthList.add("SEP");
        monthList.add("OCT");
        monthList.add("NOV");
        monthList.add("DEC");
        return monthList;
    }

    public static List<String> getYear() {
        List<String> yearList = new ArrayList<>();
        yearList.add("Year");
        for (int count = 1945; count <= 2018; count++) {
            yearList.add(String.valueOf(count));
        }
        return yearList;
    }
}
