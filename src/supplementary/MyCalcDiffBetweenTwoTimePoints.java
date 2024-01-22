/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supplementary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class MyCalcDiffBetweenTwoTimePoints {

    /**
     * Converts milliseconds into date
     * @param millis That millis is a String is not a mistake!
     * @return 
     * @last feb-2012
     * @tags millis to date ; millisToDate ; milliseconds to date ; millisecondsToDate
     */
    public static String millisToDateConverter(String millis) {

//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS"); //this works!
        //note if to write hh instead of HH it will show like 03:15:16 and not 15:15:16
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // this works to!

        long now = Long.parseLong(millis);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        System.out.println(now + " = " + formatter.format(calendar.getTime()));
        return formatter.format(calendar.getTime());
    }

    /**
     * Converts date into milli seconds
     * @param date_yyyy_mm_dd - should be written as ex. 2012-02-02
     * @return 
     * @tags date to milliseconds, date to millis, dateToMillis
     */
    public static long dateToMillisConverter(String date_yyyy_mm_dd) {
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // this works to!
//        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); // it works with "." to!
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(date_yyyy_mm_dd).getTime();
        } catch (ParseException ex) {
            Logger.getLogger(MyCalcDiffBetweenTwoTimePoints.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * 
     * @param date_1
     * @param date_2
     * @param mode - mode = 1 - return in seconds, mode = 2 - return in minutes, mode = 3 - days
     * @return 
     */
    public static int findDiffBetweenTwoDates(String date_1, String date_2, int mode) {

        long date_1_millis = dateToMillisConverter(date_1);
        long date_2_millis = dateToMillisConverter(date_2);

        long diff = date_2_millis - date_1_millis;

        int division = 0;
        if (mode == 1) {
            division = 1000; // seconds
        } else if (mode == 2) {
            division = 60000; // minutes
        }else if(mode == 3){
            division = 86400000; // days
        }

        return (int) (diff / division);
    }

    /**
     * 
     * @param date - the date of 2011-03-09 16:37:48.880
     * @return 
     */
    public static String dateFromMC_MIXPARAMS_HIST_to_normal_date_time_format(String date) {
        String a = date.replace(".", ";");
        String[] arr = a.split(";");
        return arr[0].trim();
    }

    public static void main(String[] args) {
//        String formated_date = dateFromMC_MIXPARAMS_HIST_to_normal_date_time_format("2011-03-09 16:37:48.880");
//        System.out.println("" + formated_date);

//        System.out.println("" + findDiffBetweenTwoDates("2022-01-24 00:00:00", "2022-03-21 00:00:00", 3));
//        System.out.println("" + findDiffBetweenTwoDates("2022-01-24 00:00:00", "2022-01-26 00:00:00", 3));
        System.out.println("" + findDiffBetweenTwoDates("2022-01-24 00:00:00", "2022-01-26 00:00:00", 3));
    }
}
