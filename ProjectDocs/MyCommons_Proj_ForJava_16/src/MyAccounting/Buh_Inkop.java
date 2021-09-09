/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAccounting;

import statics.HelpMy;

/**
 *
 * @author MCREMOTE
 */
public class Buh_Inkop extends Buh_Basic {

    /**
     * Creates new form Buh_Inkop
     */
    public Buh_Inkop() {
        initComponents();
        initOther();
        setTitle("Inköp");
        jTextField4.setEnabled(false);
    }

    @Override
    protected void defineTextArea() {
        jtextAreaOutPut = jTextArea1;
    }

    @Override
    protected void defineMomsTextField() {
        textFieldMoms = jTextField_moms;
    }

    private void initOther() {
        init();
//        jTextField2.setEnabled(false);
    }

    private boolean isEgetUttag() {
        return jCheckBox_eget_uttag.isSelected();
    }

    private boolean isForenkladVMB() {
        return jCheckBox_forenklad_vmb.isSelected();
    }

    private boolean isNormalVMB() {
        return jCheckBox_normal_vmb.isSelected();
    }

    private boolean isNormalInkop() {
        return jCheckBox_normal_med_moms.isSelected();
    }

    private double getTotal() {
        return Double.parseDouble(jTextField1.getText());
    }

    private double getMoms() {
        return Double.parseDouble(jTextField2.getText());
    }

    private double getInkopsKostnaderWithMoms() {
        return Double.parseDouble(jTextField3.getText());
    }

    private double getInkopsKostnaderWithoutMoms() {
        return Double.parseDouble(jTextField4.getText());
    }

    @Override
    protected void CALC() {
        //
        jTextArea1.setText("");
        showMessage("---------------------", "----------------------------");
        showMessage_debet_kredit_header();
        //
        //
        boolean obligatory_chekbox_selected = jCheckBox_normal_vmb.isSelected()
                || jCheckBox_forenklad_vmb.isSelected()
                || jCheckBox_normal_med_moms.isSelected();
        //
        if (obligatory_chekbox_selected == false) {
            HelpMy.showNotification("Välj en av följande markeringar: Förenklad VMB, Normal VMB, Normal med moms. Och försök igen" );
            return;
        }
        //
        //
        if (isNormalVMB() && getInkopsKostnaderWithoutMoms() != 0) {
            egetUttagAndVMBAndMoms(isEgetUttag(), false, false);
            return;
        }
        //
        if (isNormalVMB() && getMoms() != 0 && getInkopsKostnaderWithMoms() != 0) {
            egetUttagAndVMBAndMoms(isEgetUttag(), false, true);
            return;
        }
        //
        if (isNormalVMB()) {
            egetUttagAndVMB(isEgetUttag(), false);
            return;
        }
        //
        //**********************************************************
        //
        if (isForenkladVMB() && getInkopsKostnaderWithoutMoms() != 0) {
            egetUttagAndVMBAndMoms(isEgetUttag(), true, false);
            return;
        }
        //
        if (isForenkladVMB() && getMoms() != 0 && getInkopsKostnaderWithMoms() != 0) {
            egetUttagAndVMBAndMoms(isEgetUttag(), true, true);
            return;
        }
        //
        if (isForenkladVMB()) {
            egetUttagAndVMB(isEgetUttag(), true);
            return;
        }
        //
        //*********************************************************
        //
        // NOT WORKING CORRECTLY WITH MOMS YET
        //
        if (isEgetUttag() && isNormalInkop()) {
            showMessage_b(Buh_Konto.EGNA_INSATTNINGAR, "kredit", getTotal());
            showMessage_b(Buh_Konto.ING_MOMS, "debet", getMoms());
            showMessage_b(Buh_Konto.INKOP_NORMAL, "debet", (getTotal() - getMoms()));
            return;
        }
        //
        if (isEgetUttag() == false && isNormalInkop()) {
            showMessage_b(Buh_Konto.KASSA, "kredit", getTotal());
            showMessage_b(Buh_Konto.ING_MOMS, "debet", getMoms());
            showMessage_b(Buh_Konto.INKOP_NORMAL, "debet", (getTotal() - getMoms()));
            return;
        }
        //
        //*********************************************************

        //*********************************************************
    }

    private void egetUttagAndVMBAndMoms(boolean egetUttag, boolean förenklad, boolean moms) {
        //
        String konto = "";
        //
        if (förenklad) {
            konto = Buh_Konto.INKOP_VMB_VAROR_FORENKLAD;
        } else {
            konto = Buh_Konto.INKOP_VMB_VAROR;
        }
        //
        if (egetUttag) {
            showMessage_b(Buh_Konto.EGNA_INSATTNINGAR, "kredit", getTotal());
        } else {
            showMessage_b(Buh_Konto.KASSA, "kredit", getTotal());
        }
        //
//        showMessage_b(Buh_Konto.KASSA, "kredit", "" + getTotal());
        //
        if (moms) {
            showMessage_b(Buh_Konto.ING_MOMS, "debet", getMoms());
            double inkops_kost_minus_moms = (getInkopsKostnaderWithMoms() - getMoms());
            showMessage_b(Buh_Konto.OVR_INKOPS_KOST, "debet", inkops_kost_minus_moms);
            showMessage_b(konto, "debet", (getTotal() - getMoms() - inkops_kost_minus_moms));
        } else {
            showMessage_b(Buh_Konto.OVR_INKOPS_KOST, "debet", getInkopsKostnaderWithoutMoms());
            showMessage_b(konto, "debet", (getTotal() - getInkopsKostnaderWithoutMoms()));
        }

    }

    private void egetUttagAndVMB(boolean egetUttag, boolean förenklad) {
        //
        String konto = "";
        //
        if (förenklad) {
            konto = Buh_Konto.INKOP_VMB_VAROR_FORENKLAD;
        } else {
            konto = Buh_Konto.INKOP_VMB_VAROR;
        }
        //
        if (egetUttag) {
            showMessage_b(Buh_Konto.EGNA_INSATTNINGAR, "kredit", getTotal());
        } else {
            showMessage_b(Buh_Konto.KASSA, "kredit", getTotal());
        }
        //
        showMessage_b(konto, "debet", getTotal());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox_eget_uttag = new javax.swing.JCheckBox();
        jCheckBox_forenklad_vmb = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jTextField_moms = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox_normal_vmb = new javax.swing.JCheckBox();
        jCheckBox_normal_med_moms = new javax.swing.JCheckBox();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox_utlands_kop = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jCheckBox_eget_uttag.setText("Eget uttag");

        jCheckBox_forenklad_vmb.setText("Förenklad VMB");
        jCheckBox_forenklad_vmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_forenklad_vmbActionPerformed(evt);
            }
        });

        jButton1.setText("Calc");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel3.setText("Moms sats");

        jTextField_moms.setText("0.25");

        jTextField1.setText("0");

        jLabel1.setText("Inköps pris");

        jCheckBox_normal_vmb.setText("Normal VMB");
        jCheckBox_normal_vmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_normal_vmbActionPerformed(evt);
            }
        });

        jCheckBox_normal_med_moms.setText("Normal med moms");
        jCheckBox_normal_med_moms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_normal_med_momsActionPerformed(evt);
            }
        });

        jTextField2.setText("0");

        jLabel2.setText("Moms");

        jTextField3.setText("0");
        jTextField3.setToolTipText("Används tillsammans med moms");

        jLabel4.setText("Övr. Inköps kost. inkl moms");

        jTextField4.setText("0");

        jLabel5.setText("Övr. inköp kost. exkl moms");

        jCheckBox_utlands_kop.setText("Utlands köp");
        jCheckBox_utlands_kop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_utlands_kopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField_moms, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox_eget_uttag)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox_forenklad_vmb)
                        .addGap(10, 10, 10)
                        .addComponent(jCheckBox_normal_vmb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox_normal_med_moms)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox_utlands_kop)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_eget_uttag)
                    .addComponent(jCheckBox_forenklad_vmb)
                    .addComponent(jCheckBox_normal_vmb)
                    .addComponent(jCheckBox_normal_med_moms)
                    .addComponent(jCheckBox_utlands_kop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_moms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.out.println("" + isEgetUttag());
        System.out.println("" + isForenkladVMB());
        System.out.println("" + isNormalVMB());
        System.out.println("" + isNormalInkop());
        CALC();
        AFTER_CALC();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox_normal_med_momsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_normal_med_momsActionPerformed
        if (jCheckBox_normal_med_moms.isSelected()) {
            jCheckBox_normal_vmb.setSelected(false);
            jCheckBox_forenklad_vmb.setSelected(false);
            jCheckBox_utlands_kop.setSelected(false);
            //
            jTextField2.setEnabled(true);
            jTextField3.setEnabled(true);
            //
            if (jCheckBox_utlands_kop.isSelected() == false) {
                jTextField4.setEnabled(false);
            }
            //
        }
    }//GEN-LAST:event_jCheckBox_normal_med_momsActionPerformed

    private void jCheckBox_normal_vmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_normal_vmbActionPerformed
        if (jCheckBox_normal_vmb.isSelected()) {
            jCheckBox_forenklad_vmb.setSelected(false);
            jCheckBox_normal_med_moms.setSelected(false);
            jCheckBox_utlands_kop.setSelected(false);
            //
            jTextField2.setEnabled(true);
            jTextField3.setEnabled(true);
            //
            if (jCheckBox_utlands_kop.isSelected() == false) {
                jTextField4.setEnabled(false);
            }
            //
        }
    }//GEN-LAST:event_jCheckBox_normal_vmbActionPerformed

    private void jCheckBox_forenklad_vmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_forenklad_vmbActionPerformed
        if (jCheckBox_forenklad_vmb.isSelected()) {
            jCheckBox_normal_vmb.setSelected(false);
            jCheckBox_normal_med_moms.setSelected(false);
            //
            if (jCheckBox_utlands_kop.isSelected() == false) {
                jTextField4.setEnabled(false);
            }
            //
        }
    }//GEN-LAST:event_jCheckBox_forenklad_vmbActionPerformed

    private void jCheckBox_utlands_kopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_utlands_kopActionPerformed
        if (jCheckBox_utlands_kop.isSelected()) {
            jTextField2.setEnabled(false);
            jTextField3.setEnabled(false);
            jTextField2.setText("0");
            jTextField3.setText("0");
            jTextField4.setEnabled(true);
        } else {
            jTextField2.setEnabled(true);
            jTextField3.setEnabled(true);
            jTextField4.setText("0");
            jTextField4.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBox_utlands_kopActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Buh_Inkop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Buh_Inkop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Buh_Inkop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Buh_Inkop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Buh_Inkop().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox_eget_uttag;
    private javax.swing.JCheckBox jCheckBox_forenklad_vmb;
    private javax.swing.JCheckBox jCheckBox_normal_med_moms;
    private javax.swing.JCheckBox jCheckBox_normal_vmb;
    private javax.swing.JCheckBox jCheckBox_utlands_kop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField_moms;
    // End of variables declaration//GEN-END:variables
}
