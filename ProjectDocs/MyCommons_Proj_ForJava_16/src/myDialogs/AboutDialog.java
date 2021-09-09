/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myDialogs;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * IF you plan to use it for another project, take the example from "MyCommons.myDialogs" [2020-06-09]
 * 
 * Have also look at AboutDialog_Extended
 * @author
 */
public class AboutDialog extends javax.swing.JDialog implements HyperlinkListener {

    private final String title;

    /**
     * Creates new form AboutDialog
     *
     * @param parent
     * @param modal
     * @param title
     */
    public AboutDialog(java.awt.Frame parent, boolean modal, String title) {
        super(parent, modal);
        this.title = title;
        initComponents();
        initOther();
    }
    //
    /**
     * This is an example how you use the AboutDialog when 
     * you for example press a button [2020-06-09]
     */
    private void HOW_TO_USE_EXAMPLE(){
//        AboutDialog aboutDialog = new AboutDialog(this, true,"Help");
//        Point p = aboutDialog.position_window_in_center_of_the_screen(aboutDialog);
//
//        aboutDialog.setLocation(p);
//        aboutDialog.setVisible(true);
    }
    //

    /**
     * For proper opening of web links - NEEDED![2020-06-09]
     * @param e 
     */
    @Override
    public synchronized void hyperlinkUpdate(HyperlinkEvent e) {
        //
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (Exception ex) {
                Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //
    }

    private void initOther() {
        //
        init_a();
        //
        init_b();
        //
    }
    
    protected void init_a(){
        jEditorPane1.setEditable(false);
        jEditorPane1.setContentType("text/html");
        jEditorPane1.addHyperlinkListener(this);
        jEditorPane1.setText(buildHTML());
        this.setTitle(" " + title);
    }
    
    protected void init_b(){
        this.setSize(400, 400);
    }

    protected String buildHTML() {
        //
        String img_a = getImageIconURL("images", "file.png").toString();
        String img_b = getImageIconURL("images", "file.png").toString(); 
        String img_c = getImageIconURL("images/images_b", "star.png").toString(); 
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
                + "<tr>"
                + "<td><h3>" + "4.To calculate Std dev, Average, Median etc., click button" + "</h3></td>"
                + "<td><img src='" + img_c + "' alt='MCRemote' width='32' height='32' ></td>"
                + "</tr>"
                + "<tr>"
                //
                + "</table>"
                //
                + "</div>"
                + "</body>"
                + "</html>";
    }
    
    /**
     * 
     * @param path - path to image folder, play around to get the path working
     * @param picName
     * @return 
     */
    protected URL getImageIconURL(String path, String picName) {
        //OBS! YES the first "/" is NEEDED - 100% [2020-06-09]
        return AboutDialog.class.getResource("/" + path + "/" + picName);
    }
    
     public Point position_window_in_center_of_the_screen(JDialog window) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((d.width - window.getSize().width) / 2, (d.height - window.getSize().height) / 2);
    }

    /**
     * This is for testing
     *
     * @param args
     */
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLayout(new GridLayout(1, 1));
        JButton b = new JButton("Open");
        final AboutDialog ad = new AboutDialog(frame, true, "Help");
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane2.setViewportView(jEditorPane1);

        getContentPane().add(jScrollPane2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

}
