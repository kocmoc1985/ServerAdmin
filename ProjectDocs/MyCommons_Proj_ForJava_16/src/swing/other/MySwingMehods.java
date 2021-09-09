/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing.other;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author KOCMOC
 */
public class MySwingMehods {

    /**
     * Very effective - Tested
     * @param frame 
     */
    public static void bringToFront(JFrame frame) {
        frame.setState(JFrame.ICONIFIED);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static void requestFocus(final JComponent component) {
        Thread x = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MySwingMehods.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //
                component.requestFocus();
                //
            }
        };
        //
        x.start();
        //
    }

    /**
     * Very good to remember
     *
     * @param frame
     */
    public void windowClosing_(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Very Very useful method
     *
     * @tags setMargin, setInsets, set margin
     * @param jc
     */
    public static void setMarginJComponent(JComponent jc) {
        jc.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
    }

    public static void replaceComponentRecursively(JComponent search, Component replace, Component replaceWith) {
        //
        Component[] c_arr = search.getComponents();
        //
        for (Component component : c_arr) {
            if (component == replace) {
                //to do code
                Component parent = component.getParent();
                //
                if (parent instanceof JComponent) {
                    JComponent par = (JComponent) parent;
                    par.remove(replace);
                    par.add(replaceWith);
                }
                //
            } else {
                if (component instanceof JComponent) {
                    replaceComponentRecursively((JComponent) component, replace, replaceWith);
                }
            }
        }
    }

    /**
     * It's possible to adjust appereance for java components with CSS! Java
     * components which supports CSS: 1. JLabel 2. JButton
     *
     * @tested
     */
    public static void set_visual_settins_with_css() {
        String value = "value";
        JLabel key_label = new JLabel("<html><p style='margin-left:5px'>" + value + "</p></html>");

        JButton save_btn = new JButton("<html><div style='background-color:black;width:100px;'></div>save</html>");
    }

    /**
     * @tags:set icon, iconImage,jframe
     * @param frame
     * @param path
     */
    public static void setIconImageForJFrame(JFrame frame, String path) {
        frame.setIconImage(new ImageIcon(path).getImage());
    }

    /**
     * Adjust look & feel for a component
     */
    private static void manageUI() {
        Icon icon = new ImageIcon("images/1.png");
        //This options are for JSlider
        UIManager.put("Slider.verticalThumbIcon", icon);
        UIManager.put("Slider.trackWidth", 10);
    }

    /**
     * Remember that PopupMenu blocks threads so use JPopupMenu!!!
     */
    private static void popupReminder() {
        //Just a reminder
    }

    /**
     * Still the best way of "refreshing,repainting" a component is to resize
     * it!!
     *
     * @BestRefreshingMethod
     * @param comp
     */
    public static void refreshComponentMostEfficient(Component comp) {
        //**************************************************
//        setSize(getWidth() - 1, getHeight() - 1);
//        setSize(getWidth() + 1, getHeight() + 1);
        //**************************************************
        //This is what happens/should happen on ComponentResizeEvent if 
        // you implement resizing on some Components in conjuction with
        // the resizing. The "if block" prevents calling resize if the resizing
        // was tiny
        //    public void componentResized(ComponentEvent e) {
//        if (width_prev != this.getWidth() && Math.abs(getWidth() - width_prev) > 2) {
//            resize();
//            System.out.println("DisplayM resized");
//        } else {
//            this.setSize(this.getWidth(), (int) (ALL_COMP_HEIGHT * frame_coeff));
//        }
//        width_prev = this.getWidth();
//    }
    }

    /**
     * Works but still i would preffer "refreshComponentMostEfficient(Component
     * comp)" Updates all components in the whole JFrame! But it's very tuff
     * method, takes much capacity
     *
     * @param frame
     */
    public static void refreshAllComponentsInFrame(final JFrame frame) {
        //Note it's very important to do this with "java.awt.EventQueue.invokeLater"
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.updateComponentTreeUI(frame.getContentPane());
            }
        });
    }

    /**
     * This is a proven method. It worked when i had difficulties with
     * MyRamPosGraph.class note it must not be JLabel, it could be JPanel also!
     *
     * @param c
     * @return
     */
    public static JLabel refreshComponent(JLabel c) {
        c.repaint();
        c.updateUI();
        return c;
    }

    public static void refreshJtable(JTable table) {
        table.validate();
        table.revalidate();
        table.repaint();
    }

    /**
     * Very Good!!! Worked when nothing other worked!
     *
     * @param frame
     */
    public static void refreshFrameByRepaintingIt(JFrame frame) {
        frame.paint(frame.getGraphics());
    }

    public static Point position_window_in_center_of_the_screen(JFrame window) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((d.width - window.getSize().width) / 2, (d.height - window.getSize().height) / 2);
    }

    public static void catch_close_window_event() {
    }

    /**
     * This one is tested, 100% working. Tested for both TextArea & JTextArea
     *
     * @tags drag and drop, drag & drop
     */
    public static void drag_and_drop() {
//        textArea1.setDropTarget(new DropTarget() {
//            @Override
//            public synchronized void drop(DropTargetDropEvent evt) {
//                try {
//                    evt.acceptDrop(DnDConstants.ACTION_COPY);
//                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
//                    for (File file : droppedFiles) {
//                        file_list.add(file);
//                        show_message(file.getName());
//                    }
//                } catch (Exception ex) {
//                    Logger.getLogger(FileSenderModul.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
    }

    /**
     * Repaints your JFrame Component
     *
     * @param frame The frame to repaint
     * @param red colors RGB
     * @param green colors RGB
     * @param black colors RGB
     */
    public static void setBGColorJFrame(JFrame frame, int red, int green, int black) {
        Container g = frame.getContentPane();
        Color c = new Color(red, green, black);
        g.setBackground(c);
    }

    /**
     * This makes that the jframe is expanded/maximized to max value
     *
     * @param frame
     */
    public static void maximize_jframe(JFrame frame) {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * VERY IMPORTANT
     *
     * @Tags [jscrollpane, scrollpane,scrolpane,caretpostion,caret
     * position,scroll]
     */
    public static void setScrollPaneToTopPositon() {
//        jtextarea1.setCaretPosition(0);
    }

    public static void goToEndPosition() {
//        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }

    /**
     * @tags wrap text, wraptext
     */
    public static void wrapTextJTextArea() {
//        jTextArea.setLineWrap(true);
    }

    /**
     * @tags: margin-left, margin, insets
     */
    public static void setMarginJtextArea() {
//        textArea1.setMargin(new Insets(5, 5, 5, 5));
    }
}
