/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAccounting;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author MCREMOTE
 */
public class Buh_Konto_Component {
    
    private final JPanel panel = new JPanel(new GridLayout(1, 2));
    private final JTextArea textArea = new JTextArea();
    private final JLabel label = new JLabel();
    
    public Buh_Konto_Component(String kontoNumber, double defaultValue) {
        init(kontoNumber, defaultValue);
    }
    
    private void init(String kontoNumber, double defaultValue) {
        label.setText(kontoNumber);
        label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        panel.add(label);
//        textArea.setBorder(BorderFactory.createLineBorder(Color.black));
        textArea.setText("" + defaultValue);
        panel.add(textArea);
    }
    
    public JPanel getComponent() {
        return panel;
    }
    
    public double getValue() {
        return Double.parseDouble(textArea.getText());
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setSize(200, 200);
//        frame.setLayout(new GridLayout(8, 2, 0, 2));
//        Buh_Konto_Component bkc = new Buh_Konto_Component("3030");
//        Buh_Konto_Component bkc_b = new Buh_Konto_Component("3028");
//        frame.add(bkc.getComponent());
//        frame.add(bkc_b.getComponent());
//        frame.setVisible(true);
//    }
}
