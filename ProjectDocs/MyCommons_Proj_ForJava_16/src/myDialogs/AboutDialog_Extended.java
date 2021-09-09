/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myDialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author KOCMOC
 */
public class AboutDialog_Extended extends AboutDialog {

    public AboutDialog_Extended(java.awt.Frame parent, boolean modal, String title) {
        super(parent, modal, title);
    }

//    @Override
//    protected void init_b() {
//         this.setSize(800, 800);
//    }

    

    @Override
    protected String buildHTML() {
        return buildHtml_RdpComm_Example();
//        return  buildHtml_MCLabStats_Example();
    }
    
    private String buildHtml_MCLabStats_Example(){
        //
        String img_a = getImageIconURL("images", "file.png").toString();
        String img_b = getImageIconURL("images/images_b", "star.png").toString(); ;
        //
        return "<html>"
                + "<body style='background-color:#F1F3F6'>" //style='background-color:#F1F3F6'
                + "<div style='margin-left:10px;color:gray;background-color:#EEF0F4;padding:5 5 5 5px;'>"
                + "<h2>Basic search:</h2>"
                + "<table>"
                //
                + "<tr>"
                + "<td><h3>" + "1.To start a search, click button" + "</h3></td>"
                + "<td><img src='" + img_a + "' alt='MCRemote' width='32' height='32' ></td>"
                + "</tr>"
                //
                + "<tr>"
                + "<td><h3>" + "2.Fill obligatory parameters QUALITY, TEST CODE, TEST NAME" + "</h3></td>"
                + "</tr>"
                //
                + "<tr>"
                + "<td><h3>" + "3.To show results, click button" + "</h3></td>"
                + "<td><img src='" + img_b + "' alt='MCRemote' width='32' height='32' ></td>"
                + "</tr>"
                + "<tr>"
                //
                + "</table>"
                //
                + "</div>"
                + "</body>"
                + "</html>";
    }
    
    private String buildHtml_RdpComm_Example(){
        //
        String img_a = getImageIconURL("images", "pc.png").toString();
        //
        return  "<html>"
                + "<body style='background-color:#F1F3F6'>" //style='background-color:#F1F3F6'
                + "<div style='margin-left:10px;color:gray;background-color:#EEF0F4;padding:5 5 5 5px;'>"
                + "<table>"
                + "<tr>"
                + "<td><img src='" + img_a + "' alt='MCRemote' width='32' height='32' ></td>"
                + "<td><h1>MCRemote " + "1.6.7" + "</h1></td>"
                + "</tr>"
                + "</table>"
                //
                + "<table style='font-size:12px'>"
                + "<tr><td>Copyright (C) 2017-2020 MixCont AB</td></tr>"
                + "<tr><td>ask@mixcont.com</td></tr>"
                + "<tr><td><a style='text-decoration:none;' href='http://www.mixcont.com'>www.mixcont.com</a></td></tr>"
                + "</table>"
                + "</div>"
                //
                //
                + "<br>"
                //
                + "<div style='margin-left:10px;color:gray;background-color:#EEF0F4;padding:5 5 5 5px;'>" //EEF0F4, EEF0F4
                + "<table style='font-size:11px'>"
                //
                + "<tr>"
                + "<td>Client id<td>"
                + "<td>" + "?" + "</td>"
                + "</tr>"
                //
                + "<tr>"
                + "<td>Java<td>"
                + "<td>" + "?" + "</td>"
                + "</tr>"
                //
                + "<tr>"
                + "<td>Java vendor<td>"
                + "<td>" + "?" + "</td>"
                + "</tr>"
                //
                + "<tr>"
                + "<td>System<td>"
                + "<td>" + "?" + "</td>"
                + "</tr>"
                //
                + "<tr>"
                + "<td>IP<td>"
                + "<td>" + "?" + "</td>"
                + "</tr>"
                //
                + "<tr>"
                + "<td>MAC<td>"
                + "<td>" + "?" + "</td>"
                + "</tr>"
                //
                + "</table>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
    
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLayout(new GridLayout(1, 1));
        JButton b = new JButton("Open");
        final AboutDialog_Extended ad = new AboutDialog_Extended(frame, true, "Help");
        //
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ad.setVisible(true);
            }
        });
        //
        frame.add(b);
        frame.setVisible(true);
    }

}
