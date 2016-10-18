/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import java.awt.TextArea;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author KOCMOC
 */
public class MyBroadCast implements Runnable {

    private String host;
    private TextArea output;

    public MyBroadCast(String host, TextArea output) {
        this.host = host;
        this.output = output;
        startThread();
    }

    private void startThread() {
        Thread x = new Thread(this);
        x.start();
    }

    @Override
    public void run() {
        try {
            ping3(host);
        } catch (IOException ex) {
            Logger.getLogger(MyBroadCast.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyBroadCast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean ping3(String host) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "1", host);
        Process proc = processBuilder.start();

        int returnVal = proc.waitFor();
        boolean reachable = returnVal == 0;

        if (reachable) {
            InetAddress inetAddress = InetAddress.getByName(host);
            output.append("\n" + host + "  " + inetAddress.getHostName());
        }

        return returnVal == 0;
    }
    
//    public static void main(String[] args) throws IOException, InterruptedException {
//        MyBroadCast mbc = new MyBroadCast("10.87.0.2", new TextArea());
//        System.out.println("" + mbc.ping3("10.87.0.2"));
//    }
}
