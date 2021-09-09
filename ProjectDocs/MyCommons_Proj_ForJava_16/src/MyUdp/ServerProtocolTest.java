/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyUdp;


/**
 *
 * @author KOCMOC
 */
public class ServerProtocolTest extends ServerProtocol_UDP {

    public ServerProtocolTest(ShowMessage OUT) {
        super(OUT);
    }

    
    
    @Override
    public void handleRequest(String msg) {
        //
        if(msg.equals(CMD_UDP.SHUT_DOWN_PC)){
//            System.out.println("shut down cmd received");
            OUT.showMessage("shut down cmd received");
        }
        //
    }

}
