/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAccounting;

/**
 *
 * @author MCREMOTE
 */
public class Buh_ForenkladVMB extends Buh_NormalVMB {

    private final Buh_Konto_Component konto_3021 = new Buh_Konto_Component(Buh_Konto.FORSALJNING_FORENKLAD_VMB_VAROR + " sålt", 200000);
    private final Buh_Konto_Component konto_4021 = new Buh_Konto_Component(Buh_Konto.INKOP_VMB_VAROR_FORENKLAD + " köpt", 150000);
    private final Buh_Konto_Component konto_4028 = new Buh_Konto_Component(Buh_Konto.NEGATIV_VM_OMFORING + " negativ VM", 10000);

    public Buh_ForenkladVMB() {
        setTitle("FörenkladVMB");
        jTextField1_inkops_pris.setEnabled(false);
        jButton2.setVisible(true);
        addKontoComponents();
    }

    private void addKontoComponents() {
        jPanel1.add(konto_3021.getComponent());
        jPanel1.add(konto_4021.getComponent());
        jPanel1.add(konto_4028.getComponent());
    }

    private boolean areZero() {
        if (konto_3021.getValue() == 0 && konto_4021.getValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void CALC() {
        //
        jTextArea1.setText("");
        showMessage("---------------------", "----------------------------");
        //
        //
        showMessage_debet_kredit_header();
        //
        if (getFrakt() == 0) {
            showMessage_b(Buh_Konto.KASSA, "debet",  getForsaljningsPris());
            showMessage_b(Buh_Konto.FORSALJNING_FORENKLAD_VMB_VAROR, "kredit",  getForsaljningsPris());
        } else {
            showMessage_b(Buh_Konto.KASSA, "debet",  (getForsaljningsPris() + getFrakt()));
            showMessage_b(Buh_Konto.FRAKTER, "kredit", getFrakt());
            showMessage_b(Buh_Konto.FORSALJNING_FORENKLAD_VMB_VAROR, "kredit",  getForsaljningsPris());
        }
        //
    }

    @Override
    protected void CALC_B() {
        //
        // Kalkulerar en sammanställning för FÖRENKLAD VMB, MOMS också kalkulerat.
        // Som jag förstår det görs i slutet av året
        // Exempel finns här: https://www.bokforingstips.se/artikel/bokforing/vinstmarginalbeskattning.aspx
        //
        jTextArea1.setText("");
        showMessage("---------------------", "----------------------------");
        //
        //
        showMessage_debet_kredit_header();
        //
        if (areZero()) {
            return;
        }
        //
        //Now only as example:
        double konto_3021_sell = konto_3021.getValue(); // 200000
        double konto_4021_kop = konto_4021.getValue(); // 150000
        double konto_4028_negativ_vm = konto_4028.getValue(); // 0
        // Below you see how to calculate MOMS for "förenklad VMB"
//        double vinstMarginal_eller_positiv_vm_omf_konto_3028 = konto_3021_sell - konto_4021_kop - konto_4028_negativ_vm;
        double vinstMarginal_eller_positiv_vm_omf_konto_3028 = konto_3021_sell - konto_4021_kop;
        double beskatt_underlag_konto_3030 = (vinstMarginal_eller_positiv_vm_omf_konto_3028 - konto_4028_negativ_vm) * 0.8;
//
        //Below you see how to book keep it
        showMessage_b(Buh_Konto.POSITIV_VM_OMFORING, "debet", vinstMarginal_eller_positiv_vm_omf_konto_3028);
        showMessage_b(Buh_Konto.NEGATIV_VM_OMFORING, "kredit",  konto_4028_negativ_vm);
        showMessage_b(Buh_Konto.POSITIV_VM_BESKATT_UNDERLAG, "kredit", beskatt_underlag_konto_3030);
        showMessage_b(Buh_Konto.UTG_MOMS_VMB_25_PROCENT, "kredit",  beskatt_underlag_konto_3030 * getMomsSats());
        //
    }

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Buh_NormalVMB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Buh_NormalVMB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Buh_NormalVMB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Buh_NormalVMB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Buh_ForenkladVMB().setVisible(true);
            }
        });
    }

}
