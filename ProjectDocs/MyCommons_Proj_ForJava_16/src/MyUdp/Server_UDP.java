/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyUdp;

import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Server_UDP implements Runnable {

    private DatagramSocket dsocket;
    private final int recieve_port;
    private final ServerProtocol_UDP protocol_UDP;
    public final ShowMessage OUT;
    static int BUFFER_SIZE=1024;

    public Server_UDP(int port, ServerProtocol_UDP protocol_UDP, ShowMessage OUT) {
        //
        this.recieve_port = port;
        this.protocol_UDP = protocol_UDP;
        this.OUT = OUT;
        //
        try {
            dsocket = new DatagramSocket(recieve_port);
            OUT.showMessage("UDP Server started on port: " + port);
        } catch (SocketException ex) {

            Logger.getLogger(Server_UDP.class.getName()).log(Level.SEVERE, null, ex);
            OUT.showMessage("Failed to start UDP Server on port: " + port + "\n" + ex);
        }
        //
        startThread();
        //
    }

    private void startThread() {
        //
        Thread x = new Thread(this);
        x.start();
        //
    }

    public void recieve() {
        //
        byte[] buffer = new byte[BUFFER_SIZE];
        int buffersize = BUFFER_SIZE;
        DatagramPacket recPacket = new DatagramPacket(buffer, buffersize);
        //
//        System.out.println("trying to recieve!");
//        OUT.showMessage("trying to recieve!");
        //
        try {
            //
            dsocket.receive(recPacket);
            String msg = new String(recPacket.getData()).trim();
//            System.out.println("recieved msg: " + msg);
            protocol_UDP.add(msg);
            //
        } catch (IOException ex) {
            Logger.getLogger(Server_UDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
    }

    @Override
    public void run() {
        while (true) {
            recieve();
        }
    }

    public static void main(String[] args) {
//        new Thread(new Server_UDP(9999, new ServerProtocolTest())).start();
        ShowMessage out = new ShowMessage() {
            @Override
            public void showMessage(String str) {
                System.out.println("" + str);
            }
        };
        Server_UDP server_UDP = new Server_UDP(9999, new ServerProtocolTest(out), out);
    }
}
