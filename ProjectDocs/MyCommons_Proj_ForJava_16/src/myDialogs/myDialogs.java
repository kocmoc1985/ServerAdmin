/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myDialogs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author KOCMOC
 */
public class myDialogs {

    public static void main(String[] args) {
        //==================================================================================
//        exampleWithGetTextWithRegexCheck();
        //==================================================================================
        exampleWithPasswordField();
    }

    public static void exampleWithGetTextWithRegexCheck() {
        TextFieldCheck_Sql field_A = new TextFieldCheck_Sql(null, null, "\\d{5}", 15);
        boolean yesNo = chooseFromJTextFieldWithCheck(field_A, "Please type the code");

        String rst = field_A.getText();

        if (yesNo == false || rst == null || rst.isEmpty()) {
            return;
        }
    }

    public static boolean chooseFromJTextFieldWithCheck(JTextComponent tf, String msg) {
        requestFocus(tf);
        return JOptionPane.showConfirmDialog(null, tf, msg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static void exampleWithPasswordField() {
        JPasswordField jpf = chooseFromPasswordField("Enter password", false, "$");
        String pass = new String(jpf.getPassword());
        System.out.println("pass:" + pass);
    }

    public static JPasswordField chooseFromPasswordField(String msg, boolean hideChars, String echoChar) {
        //
        JPasswordField jpf = new JPasswordField();
        //
        requestFocus(jpf);
        //
        if (hideChars == false) {
            jpf.setEchoChar((char) 0);
        }
        //
        if (echoChar != null && echoChar.isEmpty() == false) {
            jpf.setEchoChar(echoChar.charAt(0));
        }

        //
        boolean x = JOptionPane.showConfirmDialog(null, jpf, msg, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
        //
        return jpf;
    }

    private static void requestFocus(final JComponent component) {
        Thread x = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(myDialogs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //
                component.requestFocus();
            }
        };

        x.start();
    }
}
