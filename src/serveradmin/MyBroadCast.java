/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private synchronized boolean ping3(String host) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "1", host);
        Process proc = processBuilder.start();

        int returnVal = proc.waitFor();
        boolean reachable = returnVal == 0;

        InetAddress address = InetAddress.getByName(host);
        String ip = address.getHostAddress();
        String mac = getMacAddrHost_run_with_output("arp -a " + ip);
        
        if(mac == null ||mac.isEmpty()){
            mac = "n/a";
        }

        if (reachable) {
            InetAddress inetAddress = InetAddress.getByName(host);
            output.append("\n" + host + "  " + inetAddress.getHostName() + "     " + mac);
        }

        return returnVal == 0;
    }
    

    private synchronized String getMacAddrHost_run_with_output(String param) throws IOException {
        Process p = Runtime.getRuntime().exec(param);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            if (!line.trim().equals("")) {
                // keep only the process name
                line = line.substring(1);
                String mac = getMacAddrHost_extractmac(line);
                if (mac.isEmpty() == false) {
                    return mac;
                }
            }
        }
        return null;
    }

    private synchronized String getMacAddrHost_extractmac(String str) {
        String arr[] = str.split("   ");
        for (String string : arr) {
            if (string.trim().length() == 17) {
                return string.trim().toUpperCase();
            }
        }
        return "";
    }
//    public static void main(String[] args) throws IOException, InterruptedException {
//        MyBroadCast mbc = new MyBroadCast("10.87.0.2", new TextArea());
//        System.out.println("" + mbc.ping3("10.87.0.2"));
//    }
}
