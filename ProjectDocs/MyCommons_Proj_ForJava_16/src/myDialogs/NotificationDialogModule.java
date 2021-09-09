/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myDialogs;

import java.util.Properties;
import statics.HelpMy;

/**
 *
 * @author KOCMOC
 */
public class NotificationDialogModule {

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NotificationDialogModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotificationDialogModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotificationDialogModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotificationDialogModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //
//        JPDialogs.showNotificationCustomFontSize("This module is not available in demo mode!", 24);
        //
        //
        //
        //[2021-03-05]
        Properties p = HelpMy.properties_load_properties("note.properties", true);
        //
        String note_text = p.getProperty("note", "Not available!");
        int font_size = Integer.parseInt(p.getProperty("font_size", "18"));
        String color = p.getProperty("color", "blue");
        //
        if (p.isEmpty() == false) {
            JPDialogs.showNotificationCustomFontSize(note_text, font_size, color);
        } else {
            JPDialogs.showNotificationCustomFontSize("This application is not available yet!", 16, "black");
        }
        //
    }
}
