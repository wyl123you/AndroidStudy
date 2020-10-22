package com.example.study;

import java.util.Enumeration;
import java.util.Vector;

public class Test {

    public static void main(String[] args) {
        Enumeration<String> days;
        Vector<String> dayNames = new Vector<>();
        dayNames.add("Sunday");
        dayNames.add("Monday");
        dayNames.add("Tuesday");
        dayNames.add("Wednesday");
        dayNames.add("Thursday");
        dayNames.add("Friday");
        dayNames.add("Saturday");
//        days = dayNames.elements();
//        while (days.hasMoreElements()) {
//            System.out.println(days.nextElement());
//        }
        for (String dayName : dayNames) {
            System.out.println(dayName);
        }
    }
}
