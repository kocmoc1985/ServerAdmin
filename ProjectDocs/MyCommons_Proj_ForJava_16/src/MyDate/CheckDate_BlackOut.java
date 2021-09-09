///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package MyDate;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import other.SimpleLoggerLight;
//
///**
// *
// * @author KOCMOC
// */
//public class CheckDate_BlackOut {
//
//    public static void main(String[] args) {
////        checkD("2020-03-16");
//        checkDateAndClose("2020-03-16");
//    }
//
//    private static void checkDateAndClose(String date_yyyy_mm_dd) {
//        if (checkD(date_yyyy_mm_dd)) {
//            System.exit(0);
//        }
//    }
//
//    private static boolean checkD(String date_yyyy_mm_dd) {
//        //
//        long today = dateToMillis(getDate());
//        long dday = dateToMillis(date_yyyy_mm_dd);
//        //
//        if (today >= dday) {
//            SimpleLoggerLight.logg("ddstop.log", "err_12002");
//            System.out.println("YEEEE");
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private static String getDate() {
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        return formatter.format(calendar.getTime());
//    }
//
//    private static long dateToMillis(String date_yyyy_MM_dd) {
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // this works to!
//        try {
//            return formatter.parse(date_yyyy_MM_dd).getTime();
//        } catch (ParseException ex) {
//            Logger.getLogger(CheckDate_BlackOut.class.getName()).log(Level.SEVERE, null, ex);
//            return -1;
//        }
//    }
//}
