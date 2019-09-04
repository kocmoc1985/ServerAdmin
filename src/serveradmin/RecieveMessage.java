/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Marina
 */
public class RecieveMessage implements Runnable {

    private final Socket socket;
    private boolean run = true;

    public RecieveMessage(Socket socket) {
        this.socket = socket;
    }

    private void recieve() throws ClassNotFoundException {

        try {
            if (socket != null) {
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                String sMsg = (String) input.readObject();
                JOptionPane.showMessageDialog(null, sMsg);
                run = false;
            }
        } catch (IOException e) {
            Logger.getLogger(RecieveMessage.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void run() {
        while (run) {
            try {
                recieve();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RecieveMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
