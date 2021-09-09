/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myDialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import statics.HelpMy;
import static statics.HelpMy.file_exist;
import static statics.HelpMy.read_Txt_To_ArrayList;
import static statics.HelpMy.writeToFile;

/**
 *
 * @author KOCMOC
 */
public class JPDialogs {

    public static void main(String[] args) {
        showErrorMessage("AAA");
    }
    
    /**
     * Very important, saves last entered value to file
     * @param filePath
     * @param msg
     * @return 
     */
    public static String getLastEntered_with_dialog(String filePath, String msg) {
        //
        ArrayList<String> list;
        //
        String previous = null;
        //
        if (file_exist(filePath)) {
            //
            list = read_Txt_To_ArrayList(filePath);
            //
            if (list.isEmpty() == false) {
                previous = list.get(0);
            }
            //
        }
        //
        String actual;
        //
        if (previous == null || previous.isEmpty()) {
            //
            actual = JOptionPane.showInputDialog(msg);
            //
        } else {
            //
            actual = JOptionPane.showInputDialog(msg, previous);
            //
        }
        //
        if (actual == null || actual.isEmpty()) {
            return "";
        }
        //
        try {
            writeToFile(filePath, actual);
        } catch (IOException ex) {
            Logger.getLogger(HelpMy.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        return actual;
    }
    
    public static void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showNotificationCustomFontSize(String msg,int fontSize) {
        JLabel label = new JLabel("<html><font color='blue'>"+msg+"</font></html>");
        
        label.setFont(new Font("Arial", Font.PLAIN, fontSize));
        JOptionPane.showMessageDialog(null,label,"NOTE",JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showNotificationCustomFontSize(String msg,int fontSize,String color) {
        JLabel label = new JLabel("<html><font color='"+ color +"'>"+msg+"</font></html>");
        
        label.setFont(new Font("Arial", Font.PLAIN, fontSize));
        JOptionPane.showMessageDialog(null,label,"NOTE",JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showNotification(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static boolean confirm() {
        return JOptionPane.showConfirmDialog(null, "Confirm action?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean confirm(Object message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
     /**
     * JTextField jtf = new JTextField(noteName); jtf.setPreferredSize(new
     * Dimension(300, 50)); boolean yes = HelpA.chooseFromJTextField(jtf,
     * "Please specify the new note name"); String value = jtf.getText();
     *
     * @param jtf
     * @param msg
     * @return
     */
    public static boolean chooseFromJTextField(JTextField jtf, String msg) {
        HelpMy.requestFocus(jtf);
        return JOptionPane.showConfirmDialog(null, jtf, msg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean chooseFromComboBoxDialog(JComboBox box, String msg) {
        return JOptionPane.showConfirmDialog(null, box, msg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean chooseFrom2ComboBoxDialogs(JComboBox box, JComboBox box2, String msg) {
        JPanel container = new JPanel(new GridLayout(1, 2));
        container.add(box);
        container.add(box2);
        return JOptionPane.showConfirmDialog(null, container, msg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean chooseFromComboBoxDialogBoxAndTextfield(JComboBox box, JTextField field, String msg) {
        JPanel container = new JPanel(new GridLayout(1, 2));
        container.add(box);
        container.add(field);
        return JOptionPane.showConfirmDialog(null, container, msg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean chooseFrom2Textfields(JTextField field1, JTextField field2, String label1, String label2, String msg) {
        JPanel container = new JPanel(new GridLayout(4, 1));
        container.setPreferredSize(new Dimension(200, 100));
        JLabel lbl1 = new JLabel(label1);
        JLabel lbl2 = new JLabel(label2);
        container.add(lbl1);
        container.add(field1);
        container.add(lbl2);
        container.add(field2);
        return JOptionPane.showConfirmDialog(null, container, msg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
}
