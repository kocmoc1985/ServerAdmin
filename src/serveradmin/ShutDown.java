/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author Administrator
 */
public class ShutDown extends JFrame implements Runnable, ActionListener {

    private long SHUT_DOWN_TIME = 0;
    private Kalendar kalendar;
    private JTextField textField;
    private Font time_font = new Font("Arial", Font.BOLD, 90);
    private Font btn_font = new Font("Arial", Font.BOLD, 45);
    private Image image;

    public ShutDown() {
        choose_date();
        start_thread();
    }
    
    public ShutDown(Image image) {
        this.image = image;
        choose_date();
        start_thread();
    }

    private void start_thread() {
        Thread t = new Thread(this);
        t.start();
    }

    private void choose_date() {
        if(image!= null){
            this.setIconImage(image);
        }
        this.setTitle("Set shutdown time");
        this.setLayout(new GridLayout(3, 1));
        kalendar = new Kalendar();

        textField = new JTextField("23:30");
        JButton button = new JButton("SET");

        button.addActionListener(this);

        button.setFont(btn_font);
        textField.setFont(time_font);
        textField.setHorizontalAlignment(JTextField.CENTER);

        this.add(kalendar);
        this.add(textField);
        this.add(button);

        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ShutDown sh = new ShutDown();
    }

    @Override
    public void run() {
        while (true) {
            wait_(60000);
            String curr_time = millisToDateConverter("" + System.currentTimeMillis());
            String shut_down_time = millisToDateConverter("" + SHUT_DOWN_TIME);

            if (curr_time.equals(shut_down_time)) {
                try {
                    //Perform shutdown here
                    shut_down_immediately();
                } catch (IOException ex) {
                    Logger.getLogger(ShutDown.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        }
    }

    //===========================================================================
    private static void shut_down_immediately() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("shutdown -s -t 0");
        System.exit(0);
    }

    private static void wait_(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(ShutDown.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            SHUT_DOWN_TIME = kalendar.get_selected_date_and_time_as_millis(textField.getText());
            System.out.println("shut_down_date_time = " + kalendar.get_current_date_YYYY_MM_DD());
            System.out.println("shut_down_in_millis = " + SHUT_DOWN_TIME);
            this.setVisible(false);
        }
    }

    public static String millisToDateConverter(String millis) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // this works to!
        long now = Long.parseLong(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }
}
