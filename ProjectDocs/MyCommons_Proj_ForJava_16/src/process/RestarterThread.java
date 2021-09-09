/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KOCMOC
 */
public class RestarterThread implements Runnable {
    
    private final String SHUT_DOWN_TIME_HH_MM;
    
    public RestarterThread(String TIME_HH_MM){
        this.SHUT_DOWN_TIME_HH_MM = TIME_HH_MM;
    }
    
    @Override
    public void run() {
        while (true) {
            wait_(60000);
            String curr_time = get_proper_time_same_format_on_all_computers();
            
            if (curr_time.equals(SHUT_DOWN_TIME_HH_MM)) {
//                System.out.println("Closing program");
                System.exit(0);
            }
            
        }
    }
    
    private static void wait_(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(RestarterThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static String get_proper_time_same_format_on_all_computers() {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }
    
    
    
//    public static void main(String[] args) {
//      Thread x = new Thread(new RestarterThread("10:21"));
//      x.start();
//    }
}
