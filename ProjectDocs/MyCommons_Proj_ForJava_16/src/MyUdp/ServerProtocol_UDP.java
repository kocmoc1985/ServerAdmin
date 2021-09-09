/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyUdp;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KOCMOC
 */
public abstract class ServerProtocol_UDP implements Runnable {

    public LinkedList<String> buffer = new LinkedList<String>();
    public final ShowMessage OUT;

    public ServerProtocol_UDP(ShowMessage OUT) {
        this.OUT = OUT;
        startThread();
    }
    
    private void startThread(){
        Thread x = new Thread(this);
        x.start();
    }

    public synchronized void add(String msg) {
        //
        if (msg == null) {
            return;
        }
        //
        buffer.add(msg);
        //
        notify();
        //
    }

    @Override
    public void run() {
        while (true) {
            if (buffer.size() > 0) {
                handleRequest(buffer.pollFirst());
            } else {
                synchronized (this) {
                    try {
                        wait(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerProtocol_UDP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public abstract void handleRequest(String msg);

}
