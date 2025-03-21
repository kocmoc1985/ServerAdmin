/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

//import com.jezhumble.javasysmon.JavaSysMon;
//import com.jezhumble.javasysmon.ProcessInfo;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import supplementary.MyCalcDiffBetweenTwoTimePoints;

/**
 *
 * @author Administrator
 */
public class SA {

    public static void run_application(String path_and_name, String argument) {
        String[] commands = {path_and_name, argument};
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.directory(new File("lib"));
        try {
            builder.start();
        } catch (IOException ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void run_application(String[] commands) {
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.directory(new File("lib"));
        try {
            Process p = builder.start();
        } catch (IOException ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void run_application_2(String path) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Process p = Runtime.getRuntime().exec(path);
        } catch (IOException ex) {
            System.out.println("" + ex);
        }
    }

    public static void openMyComputer() throws AWTException {
        Robot robot = new Robot();
        //
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_E);
        //
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.keyRelease(KeyEvent.VK_E);
        //
    }

    public static void run_with_cmd(String cmd_application, String arg) {
        String[] commands = {"cmd", "/c", "start", "\"" + cmd_application + "\"", cmd_application, arg};
        ProcessBuilder builder = new ProcessBuilder(commands);
        try {
            builder.start();
        } catch (IOException ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks if the given process is running
     *
     * @processName str the process name to search for "Browser.exe"
     * @return
     */
//    public static boolean processRunning(String processName) {
//        JavaSysMon monitor = new JavaSysMon();
//        ProcessInfo[] pinfo = monitor.processTable();
//
//        for (int i = 0; i < pinfo.length; i++) {
//            String pname = pinfo[i].getName();
//            if (pname.equals(processName)) {
////                mainForm.textArea1.append("" + processName + " running = " + "true" + "\n");
//                return true;
//            }
//        }
////        mainForm.textArea1.append("" + processName + " running = " + "false" + "\n");
//        return false;
//    }
    public static void main(String[] args) {
        pingPort();
    }

    public static void millisToDate(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String millis = HelpM.getLastEntered("lib/_ms_to_date.io", "Specify millis");
        //
        String date = MyCalcDiffBetweenTwoTimePoints.millisToDateConverter(millis);

        //
        jtxt.append("MILLIS : " + millis + " = " + date);
        //
    }

    public static void devidedMillisToDate(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String millis = HelpM.getLastEntered("lib/_ms_to_date.io", "Specify millis");
        //
        long m = Long.parseLong(millis);
        long mm = m * 200000;
        //
        String date = MyCalcDiffBetweenTwoTimePoints.millisToDateConverter("" + mm);

        //
        jtxt.append("MILLIS :" + m + "* 200000 = " + mm + " ms      =     " + date);
        //
    }

    public static void dateToMillis(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String date = HelpM.getLastEntered("lib/_date_to_ms.io", "Specify date YYYY-MM-DD") + " 00:00:00";
        //
        long date_ms = MyCalcDiffBetweenTwoTimePoints.dateToMillisConverter(date);
        //
        jtxt.append("DATE (DONT USE THIS FOR BOUT) : " + date + " = " + date_ms);
        jtxt.append("\nDATE / 200000 USE THIS FOR BOUT:  = " + date_ms / 200000);
        //
    }

    public static void dateToMillis_when_date_on_pc_is_back(TextArea jtxt) { // Toest' kogda chasin otstajut
        //
        jtxt.setText("");
        //
        String date_back = HelpM.getLastEntered("lib/_date_back.io", "Specify date BACK YYYY-MM-DD") + " 00:00:00";
        //
        String date_real = HelpM.getLastEntered("lib/_date_real.io", "Specify date REAL YYYY-MM-DD") + " 00:00:00";
        //
        String date_bout = HelpM.getLastEntered("lib/_date_bout_aa.io", "Specify date BOUT YYYY-MM-DD") + " 00:00:00";
        //
        int days_to_be_minus = MyCalcDiffBetweenTwoTimePoints.findDiffBetweenTwoDates(date_back, date_real, 3);
        //
        String date_bout_minus = HelpM.get_date_time_minus_some_time_in_days(date_bout, Long.parseLong("" + days_to_be_minus));
        //
        date_bout_minus += " 00:00:00";
        //
        long date_ms = MyCalcDiffBetweenTwoTimePoints.dateToMillisConverter(date_bout_minus);
        //
        jtxt.append("DATE BACK : " + date_back);
        jtxt.append("\nDATE REAL : " + date_real);
        jtxt.append("\nDATE BOUT REAL : " + date_real);
        jtxt.append("\n");
        jtxt.append("\nDATE BOUT BACK: " + date_bout_minus + " = " + date_ms);
        jtxt.append("\nDATE / 200000 USE THIS FOR BOUT BACK:  = " + date_ms / 200000);
        //
    }

    public static void calcDiffBetween2Dates(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String date_a = HelpM.getLastEntered("lib/_date_a.io", "Specify first date YYYY-MM-DD") + " 00:00:00";
        String date_b = HelpM.getLastEntered("lib/_date_b.io", "Specify second date YYYY-MM-DD") + " 00:00:00";
        //
        int days = MyCalcDiffBetweenTwoTimePoints.findDiffBetweenTwoDates(date_a, date_b, 3);
        //
        jtxt.append("DATE A: " + date_a);
        jtxt.append("\r\nDATE B: " + date_b);
        jtxt.append("\r\nDIFFERENCE: " + days + " days");
        jtxt.append("\r\nDIFFERENCE: " + days * 24 + " hours");
        jtxt.append("\r\nDIFFERENCE: " + days * 1440 + " minutes");
        jtxt.append("\r\nDIFFERENCE: " + days * 86400 + " seconds");
        //
    }

    public static void date_plus(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String date_a = HelpM.getLastEntered("lib/_date_plus_a.io", "Specify date YYYY-MM-DD") + " 00:00:00";
        String days_to_be_added = HelpM.getLastEntered("lib/_date_plus_b.io", "Specify ammount of days to be added");
        //
        String date_plus = HelpM.get_date_time_plus_some_time_in_days(date_a, Long.parseLong(days_to_be_added));
        //
        jtxt.append("DATE: " + date_a);
        jtxt.append("\r\nDAYS ADDED: " + days_to_be_added);
        jtxt.append("\r\nNEW DATE: " + date_plus);
        //
    }

    public static void date_minus(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String date_a = HelpM.getLastEntered("lib/_date_minus_a.io", "Specify date YYYY-MM-DD") + " 00:00:00";
        String days_to_be_minus = HelpM.getLastEntered("lib/_date_minus_b.io", "Specify ammount of days to be minused");
        //
        String date_minus = HelpM.get_date_time_minus_some_time_in_days(date_a, Long.parseLong(days_to_be_minus));
        //
        jtxt.append("DATE: " + date_a);
        jtxt.append("\r\nDAYS MINUS: " + days_to_be_minus);
        jtxt.append("\r\nNEW DATE: " + date_minus);
        //
    }

    public static void string_to_byte_array(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String date = HelpM.getLastEntered("lib/str_to_byte_arr.io", "Write the string");
        //
        byte[] bytes = date.getBytes();
        String to_return = "new String(new byte[]{";
        for (byte b : bytes) {
            to_return += "" + b + ",";
            System.out.println(b);
        }
        jtxt.append("\r\n" + to_return + "})");
    }

    public static void byte_array_to_string(TextArea jtxt) {
        //
        jtxt.setText("");
        //
        String str = HelpM.getLastEntered("lib/byte_arr_to_str.io", "Insert all including: new String(new byte[]{");
        //
        String str_to_run_in_netbeans = "";
        str_to_run_in_netbeans += "System.out.println(\"\" + ";
        str_to_run_in_netbeans += str;
        str_to_run_in_netbeans += ");";
        //
        jtxt.append("Run the below code from NetBeans:");
        jtxt.append("\r\n" + str_to_run_in_netbeans);
    }

    public static boolean pingPort() {
        //
        Socket socket = null;
        //
        try {
            //
            String host = HelpM.getLastEntered("lib/_pingportip.io", "Specify ip");
            String port = HelpM.getLastEntered("lib/_pingport.io", "Specify port");
            //

            //
            if (host.isEmpty() == false && port.isEmpty() == false) {
                InetAddress adress = InetAddress.getByName(host);
                socket = new Socket(adress, Integer.parseInt(port));
                socket.setTcpNoDelay(true);
//                Thread x = new Thread(new RecieveMessage(socket)); // This one is if we await some answe on connection to port
//                x.start();
                JOptionPane.showMessageDialog(null, "Port open", "", JOptionPane.INFORMATION_MESSAGE);
            }
            //
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Port busy/closed", "", JOptionPane.INFORMATION_MESSAGE);
//            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }

        }
        return true;
    }

    public static void open_dir(String path) {
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void check_if_component_present(String path, JButton button) {
        if (get_if_file_exist(path) == false) {
            button.setText(button.getText() + " (missing)");
        }
    }

    private static boolean get_if_file_exist(String path) {
        File f = new File(path);
        return f.exists();
    }

    public static void restart() throws IOException {
        //
        if (confirm() == false) {
            return;
        }
        //
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("shutdown -r -t 0");
        System.exit(0);
    }

    public static void shut_down_immediately() throws IOException {
        //
        if (confirm() == false) {
            return;
        }
        //
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("shutdown -s -t 0");
        System.exit(0);
    }

    public static boolean confirm() {
        return JOptionPane.showConfirmDialog(null, "Confirm action?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
