/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import MyUdp.CMD_UDP;
import MyUdp.ServerProtocol_UDP;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import other.ShowMessage;

/**
 *
 * @author KOCMOC
 */
public class ServerProtocolUDP extends ServerProtocol_UDP {

    public ServerProtocolUDP(ShowMessage OUT) {
        super(OUT);
    }

    @Override
    public void handleRequest(String msg) {
        //
        OUT.showMessage(msg);
        //
        if (msg.equals(CMD_UDP.SHUT_DOWN_PC)) {
            SHUT_DOWN_PC__REQUEST();
        } else if (msg.contains(CMD_UDP.SEND_MESSAGE)) {
            SEND_MESSAGE__REQUEST(msg);
        } else if (msg.contains(CMD_UDP.MAKE_ACTION_1)) {
            MAKE_ACTION_1__REQUEST();
        }
        //
//        System.out.println("MSG: " + msg);
        //
    }

    private void MAKE_ACTION_1__REQUEST() {
        //
        String PATH = "lib/actions/";
        //
        if (HelpM.file_exists(PATH + "action.cmd")) {
            try {
                HelpM.run_application_with_associated_application(new File(PATH + "action.cmd"));
            } catch (IOException ex) {
                Logger.getLogger(ServerProtocolUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (HelpM.file_exists(PATH + "action.bat")) {
            try {
                HelpM.run_application_with_associated_application(new File(PATH + "action.bat"));
            } catch (IOException ex) {
                Logger.getLogger(ServerProtocolUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void SEND_MESSAGE__REQUEST(String msg) {
        String msg_ = msg.replace(CMD_UDP.SEND_MESSAGE + ";", "");
        JTextField jtf = new JTextField(msg_);
        jtf.setPreferredSize(new Dimension(300, 50));
        JOptionPane.showMessageDialog(null, jtf, "Message from RDPCommander", JOptionPane.INFORMATION_MESSAGE);
    }

    private void SHUT_DOWN_PC__REQUEST() {
        try {
            shut_down();
        } catch (IOException ex) {
            Logger.getLogger(ServerProtocolUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void shut_down() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("shutdown -s -t 10");
//        System.exit(0);
    }

}
