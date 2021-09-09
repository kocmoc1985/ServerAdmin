/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySwing;

import swing.interfaces.JComboBoxValueChangedListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.ComboPopup;

/**
 * Listens for the changes in the list, so when you hover the mouse cursor over the
 * entry in the list it fires the "valueChanged" method
 * @author KOCMOC
 */
public class JComboBoxL extends JComboBox<Object> {

    private ListSelectionListener listener;
    private ArrayList<JComboBoxValueChangedListener> list = new ArrayList<JComboBoxValueChangedListener>();

    public JComboBoxL() {
        valueChangedListener_uninstall();
        valueChangedListener_install();
    }

    public void addValueChangedListener(JComboBoxValueChangedListener jcbvcl) {
        list.add(jcbvcl);
    }

    @Override
    public void updateUI() {
        valueChangedListener_uninstall();
        super.updateUI();
        valueChangedListener_install();
    }

    private void valueChangedListener_uninstall() {
        if (listener == null) {
            return;
        }
        getPopupList().removeListSelectionListener(listener);
        listener = null;
    }

    private void valueChangedListener_install() {
        ListSelectionListener[] arr = getPopupList().getListSelectionListeners();

        listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //
                if (e.getValueIsAdjusting()) {
                    return;
                }
                //
                JList list = getPopupList();
                Object obj = list.getSelectedValue();
                String str = String.valueOf(obj);
                callListeners(str);

            }
        };
        getPopupList().addListSelectionListener(listener);
    }

    private void callListeners(String value) {
        for (JComboBoxValueChangedListener listnr : list) {
            listnr.comboBoxvalueChanged(value, this);
        }
    }

    private JList getPopupList() {
        ComboPopup popup = (ComboPopup) getUI().getAccessibleChild(this, 0);
        return popup.getList();
    }
}
