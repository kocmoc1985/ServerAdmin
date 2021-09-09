/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package other;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import statics.HelpMy;

/**
 * SimpleLogger light does not use any external libraries
 * @author Administrator
 */
public class SimpleLoggerLight {

    private static final int MAX_FILE_SIZE_MB = 50;
    private static final String DEFAULT_LOG_MSG = "default";
    private static final String EXCEPTION_LOG_MSG = "exception";

    public static void logg(String LOGFILE, String textToWrite) {
        if (get_file_size_mb(LOGFILE) > MAX_FILE_SIZE_MB) {
            return;
        }
        try {
            FileWriter fstream = new FileWriter(LOGFILE, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(build_log_message(DEFAULT_LOG_MSG, textToWrite));
            out.newLine();
            out.flush();
        } catch (Exception ex) {
            System.out.println("" + ex);
        }
    }
    
     public static void main(String[] args) {
        logg("debug/abc/log.txt", "ajaj");
    }
    
    

    public static void logg_no_append(String LOGFILE, String textToWrite) {
        if (get_file_size_mb(LOGFILE) > MAX_FILE_SIZE_MB) {
            return;
        }
        try {
            FileWriter fstream = new FileWriter(LOGFILE, false);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(build_log_message(DEFAULT_LOG_MSG, textToWrite));
            out.newLine();
            out.flush();
        } catch (Exception ex) {
            System.out.println("" + ex);
        }
    }

//    public static void loggException(String LOGFILE, String textToWrite) {
//        if (get_file_size_mb(LOGFILE) > MAX_FILE_SIZE_MB) {
//            return;
//        }
//        try {
//            FileWriter fstream = new FileWriter(LOGFILE, true);
//            BufferedWriter out = new BufferedWriter(fstream);
//            out.write(build_log_message(EXCEPTION_LOG_MSG, textToWrite));
//            out.newLine();
//            out.flush();
//        } catch (Exception ex) {
//            System.out.println("" + ex);
//        }
//    }
    private static String build_log_message(String type, String text_to_write) {
        if (type.equals(DEFAULT_LOG_MSG)) {
            return "[" + HelpMy.get_proper_date_time_same_format_on_all_computers() + "] " + "  " + text_to_write;
        } else if (type.equals(EXCEPTION_LOG_MSG)) {
            return "[" + HelpMy.get_proper_date_time_same_format_on_all_computers() + "] "
                    + "  "  + " [Exception] " + text_to_write;
        } else {
            return "build_log_message(...) - > type not defined";
        }
    }

    private static double get_file_size_mb(String path) {
        File f = new File(path);
        return f.length() / 1048576;
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static String getMethodName(final int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        return ste[ste.length - 1 - depth].getMethodName();
    }

   
}
