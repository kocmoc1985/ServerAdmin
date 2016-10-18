/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author KOCMOC
 */
public class PreventStandBy implements Runnable {

    private PopupMenu popup;
    private MenuItem exit;
    private MenuItem open;
    private SystemTray tray;
    private TrayIcon trayIcon;
    private Image image = new ImageIcon("lib/2.png").getImage();
    private Robot robot;

    public PreventStandBy() throws AWTException {
        robot = new Robot();
        toTray();
        startThread();
    }
    
    private void startThread(){
        Thread x = new Thread(this);
        x.start();
    }

    private void go() {
        robot.mouseMove(random(), random());
    }

    private int random() {
        int x = (int) ((Math.random() * 500) + 1);
        return x;
    }

    public static void main(String[] args) throws AWTException {
        HelpM.err_output_to_file();
        PreventStandBy psb = new PreventStandBy();
    }

    @Override
    public void run() {
        while (true) {
            go();
            //
            wait_(180000);//3min 180000
        }
    }

    private void wait_(int millis) {
        synchronized (this) {
            try {
                wait(millis);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreventStandBy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void toTray() {
        if (SystemTray.isSupported()) {

            tray = SystemTray.getSystemTray();

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == exit) {
                        System.exit(0);
                    }
                }
            };

            popup = new PopupMenu();
            exit = new MenuItem("EXIT");

            exit.addActionListener(actionListener);

            popup.add(exit);

            trayIcon = new TrayIcon(image, "Prevent StandBy", popup);

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);

            try {
                tray.add(trayIcon);

            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }

        } else {
            //  System Tray is not supported
        }
    }
}
