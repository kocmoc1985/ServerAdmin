/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyUdp;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Client_UDP implements Runnable {
    
    private final String client_ip;
    private final int client_port;
    
    public Client_UDP(String ip, int port) {
        this.client_ip = ip;
        this.client_port = port;
    }

    /**
     * Don't call this method manually
     * @param dpacket
     */
    private void sendDatagram(DatagramPacket dpacket) {
        //
        DatagramSocket dsocketX = null;
        //
        try {
            dsocketX = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Client_UDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dsocketX.send(dpacket);
            System.out.println("datagram sent to:" + dpacket.getAddress() + "  " + dpacket.getPort());
        } catch (IOException ex) {
            Logger.getLogger(Client_UDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param msg
     */
    public void prepareAndSendDatagram(String msg) {
        //
        DatagramPacket dpacket;
        //
        try {

            //<Prepare a datagramm with ip & socket parameters requested from server>
            byte[] clMsg = msg.getBytes();
            dpacket = new DatagramPacket(
                    clMsg, clMsg.length,
                    InetAddress.getByName(client_ip), client_port);
            //</Prepare a datagramm with ip & socket parameters requested from server>
            //<Pass over the datagram to the method that handles sending>
            sendDatagram(dpacket);
            //</Pass over the datagram to the method that handles sending>

        } catch (UnknownHostException ex) {
            Logger.getLogger(Client_UDP.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Override
    public void run() {
//        activate();
        while (true) {
//            activate();
            prepareAndSendDatagram("TestUDP");
            synchronized (this) {
                try {
                    wait(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client_UDP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
    public static void main(String[] args) {
//        new Thread(new Client_UDP("localhost", 9999)).start();
        
        Client_UDP udpc = new Client_UDP("localhost", 9999);
//        udpc.prepareAndSendDatagram(CMD_UDP.SHUT_DOWN_PC);
//        udpc.prepareAndSendDatagram("TEST");
    }
}
