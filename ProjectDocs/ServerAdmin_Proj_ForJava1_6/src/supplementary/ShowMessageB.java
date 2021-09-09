/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplementary;

import MyUdp.ShowMessage;
import javax.swing.JTextArea;
import serveradmin.HelpM;

/**
 *
 * @author KOCMOC
 */
public class ShowMessageB extends ShowMessage {

    private final JTextArea jtxt;

    public ShowMessageB(JTextArea jtxt) {
        this.jtxt = jtxt;
    }

    @Override
    public void showMessage(String msg) {
        jtxt.append("\n" + HelpM.get_date_time() + " " + msg);
    }

}
