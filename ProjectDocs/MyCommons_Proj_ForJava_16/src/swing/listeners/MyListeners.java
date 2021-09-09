/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing.listeners;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.border.Border;

/**
 *
 * @author KOCMOC
 */
public class MyListeners {

    /**
     * Use it in ComponentListener -> componentShown(ComponentEvent e)
     * @param jFrameOrJDialog
     * @param button 
     */
    public static void bindEnterToButton(RootPaneContainer jFrameOrJDialog, JButton button) {
        jFrameOrJDialog.getRootPane().setDefaultButton(button);
        jFrameOrJDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "none");
        jFrameOrJDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ENTER"), "press");
    }

    /**
     * Hides a window on escape button press,
     * This can be called once from constructor
     * @param jDialog 
     */
    public static void bindEscape(final JDialog jDialog) {
        //
        Action closeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jDialog.dispose();
            }
        };
        //
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        jDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "closex");
        jDialog.getRootPane().getActionMap().put("closex", closeAction);
    }
    
    private static Border PREV_BORDER;

    public static void addMouseListenerToAllComponentsOfComponent(JComponent c) {
        Component[] c_arr = c.getComponents();
        for (Component component : c_arr) {
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    String str = "SOURCE ELEM: " + me.getSource();
                    System.out.println(str);
                }

                @Override
                public void mouseEntered(MouseEvent me) {
                    if (me.getSource() instanceof JComponent) {
                        JComponent jc = (JComponent) me.getSource();
                        PREV_BORDER = jc.getBorder();
                        jc.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                    }
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    if (me.getSource() instanceof JComponent) {
                        JComponent jc = (JComponent) me.getSource();
                        jc.setBorder(PREV_BORDER);
                    }
                }
            });
            if (component instanceof JComponent) {
                addMouseListenerToAllComponentsOfComponent((JComponent) component);
            }
        }
    }
    
    public static void addMouseListenerJComboBox(JComponent c, MouseListener ml) {
        Component[] c_arr = c.getComponents();
        for (Component component : c_arr) {
            try {
//                addMouseListenerJComboBox((JComponent) component, ml);
                component.addMouseListener(ml);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Must be "public" Add MouseListener to all components of the JFrame For
     * graphic debugging.This one is used together with
     * "addMouseListenerToAllComponentsOfComponent" method
     *
     * @param frame
     * @param ml
     */
    public static void addMouseListenerToAllSubComponents(JFrame frame, MouseListener ml) {
        Component[] c_arr = frame.getContentPane().getComponents();

        for (Component component : c_arr) {
            component.addMouseListener(ml);
            addMouseListenerToAllComponentsOfComponent((JComponent) component, ml);
        }
    }

    public static void addMouseListenerToAllComponentsOfComponent(JComponent c, MouseListener ml) {
        Component[] c_arr = c.getComponents();
        for (Component component : c_arr) {
            component.addMouseListener(ml);
            addMouseListenerToAllComponentsOfComponent((JComponent) component, ml);
        }
    }

}
