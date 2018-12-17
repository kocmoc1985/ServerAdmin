/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import MyUdp.CMD_UDP;
import MyUdp.ServerProtocol_UDP;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        }
        //
//        System.out.println("MSG: " + msg);
        //
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
