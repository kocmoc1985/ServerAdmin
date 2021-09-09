/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAccounting;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import statics.HelpMy;

/**
 *
 * @author MCREMOTE
 */
public abstract class Buh_Basic extends JFrame {

    protected JTextArea jtextAreaOutPut;
    protected JTextField textFieldMoms;

    public Buh_Basic() {
        super();
    }

    protected void init() {
        defineTextArea();
        defineMomsTextField();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    protected abstract void defineMomsTextField();

    protected abstract void defineTextArea();
    
    protected double getMomsSats() {
        return Double.parseDouble(textFieldMoms.getText());
    }

    protected void showMessage(String key, String value) {
        jtextAreaOutPut.append(key + " : " + value + "\n");
    }

    protected void showMessage_debet_kredit_header() {
        jtextAreaOutPut.append("konto" + "     debet" + "         kredit" + "\n");
    }

    private double DEBET;
    private double KREDIT;

    protected void showMessage_b(String konto, String debetOrKredit, double value) {
        //
        value = HelpMy.rounding_of_double(value);
        //
        if (debetOrKredit.equals("debet")) {
            jtextAreaOutPut.append(konto + "     " + value + "\n"); // 5 spaces
            DEBET += value;
        } else {
            jtextAreaOutPut.append(konto + "                         " + value + "\n"); //25 spaces
            KREDIT += value;
        }
        //
    }

    protected boolean isBalanced() {
        boolean balanced = Math.abs(DEBET - KREDIT) == 0;
        System.out.println("isBalanced: " + balanced);
        return balanced;
    }

    protected void isBalancedNotification() {
        //
        System.out.println("DEBET: " + DEBET);
        System.out.println("KREDIT: " + KREDIT);
        //
        showMessage("---------------------", "----------------------------");
        //
        jtextAreaOutPut.append("              " + DEBET + "         " + KREDIT + "\n");
        //
        showMessage("---------------------", "----------------------------");
        //
        if (isBalanced() == false) {
            JOptionPane.showMessageDialog(null, "OBS! Debet och Kredit Balancerar ej");
        }
        //
    }

    protected void resetDebetKredit() {
        DEBET = 0;
        KREDIT = 0;
    }

    protected abstract void CALC();

    protected void CALC_HEADER(){
        //
        jtextAreaOutPut.setText("");
        showMessage("---------------------", "----------------------------");
        showMessage_debet_kredit_header();
        //
    }
    
    protected void AFTER_CALC() {
        isBalancedNotification();
        resetDebetKredit(); // OBS!!!
    }

}
