/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * mainForm.java
 *
 * Created on 2011-okt-06, 11:14:31
 */
package serveradmin;

import MyUdp.Server_UDP;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import myDialogs.TextFieldCheck_simple;
import static myDialogs.myDialogs.chooseFromJTextFieldWithCheck;
import other.ShowMessage;
import statics.HelpMy;

/**
 *
 * @author Administrator
 */
public class mainForm extends javax.swing.JFrame implements ShowMessage {

    private static boolean console = false;
    private Image image = new ImageIcon("lib/1.png").getImage();
    ShutDown SHUT_DOWN = null;
    //
    private PopupMenu popup;
    private MenuItem portRedirectionAuto;
    private MenuItem rdp;
    private MenuItem shutdown;
    private MenuItem restart;
    private MenuItem myComputer;
    private MenuItem netWorkSettings;
    private MenuItem wakeOnLan;
    private MenuItem refreshIps;
    private MenuItem processExp;
    private MenuItem tightVnc;
    private MenuItem exit;
    private MenuItem open;
    private SystemTray tray;
    private TrayIcon trayIcon;
    //

    /**
     * Creates new form mainForm
     */
    public mainForm() {
        initComponents();
        initOther();
        startUdpServer();
    }

    private void startUdpServer() {
        ServerProtocolUDP protocolUDP = new ServerProtocolUDP(this);
        Server_UDP server_UDP = new Server_UDP(9999, protocolUDP, this);
    }

    @Override
    public void showMessage(String msg) {
        jTextArea1.append(HelpMy.get_proper_date_time_same_format_on_all_computers() + "  " + msg + "\n");
    }

    private void initOther() {
        this.setTitle("Server Admin (java v. " + System.getProperty("java.version") + ")");
        showWinVersionAndBuild();
        this.setBounds(0, 0, 650, 605);
        this.setIconImage(image);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        toTray();
    }
    
    private void showWinVersionAndBuild(){
        jLabelOsVersion.setText(HelpMy.getWinVersionAndBuild() + "  " + HelpMy.getOperatingSystem());
    }

    private void toTray() {
        if (SystemTray.isSupported()) {

            tray = SystemTray.getSystemTray();

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == exit) {
                        System.exit(0);
                    } else if (e.getSource() == open) {
                        makeVisible();
                    } else if (e.getSource() == refreshIps) {
                        String str = buildTrayMessage();
                        trayIcon.setToolTip(str);
                    } else if (e.getSource() == processExp) {
                        jButton8ActionPerformed(null);
                    } else if (e.getSource() == netWorkSettings) {
                        jButton36ActionPerformed(null);
                    } else if (e.getSource() == rdp) {
                        jButton39ActionPerformed(null);
                    } else if (e.getSource() == tightVnc) {
                        jButton35ActionPerformed(null);
                    } else if (e.getSource() == portRedirectionAuto) {
                        jButton46ActionPerformed(null);
                    } else if (e.getSource() == wakeOnLan) {
                        jButton24ActionPerformed(null);
                    } else if (e.getSource() == myComputer) {
                        jButton31ActionPerformed(null);
                    } else if (e.getSource() == restart) {
                        try {
                            SA.restart();
                        } catch (IOException ex) {
                            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (e.getSource() == shutdown) {
                        try {
                            SA.shut_down_immediately();
                        } catch (IOException ex) {
                            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        makeVisible();
                    }

                }
            };
            //
            popup = new PopupMenu();
            exit = new MenuItem("EXIT");
            open = new MenuItem("OPEN");
            netWorkSettings = new MenuItem("NETWORK SETTINGS");
            myComputer = new MenuItem("MY COMPUTER");
            restart = new MenuItem("RESTART");
            shutdown = new MenuItem("SHUTDOWN");
            rdp = new MenuItem("RDP");
            portRedirectionAuto = new MenuItem("PORT REDIR AUTO");
            wakeOnLan = new MenuItem("WAKE ON LAN");
            refreshIps = new MenuItem("REFRESH IP's");
            processExp = new MenuItem("PROCESS EXP");
            tightVnc = new MenuItem("VNC");
            //
            exit.addActionListener(actionListener);
            open.addActionListener(actionListener);
            netWorkSettings.addActionListener(actionListener);
            myComputer.addActionListener(actionListener);
            restart.addActionListener(actionListener);
            shutdown.addActionListener(actionListener);
            rdp.addActionListener(actionListener);
            portRedirectionAuto.addActionListener(actionListener);
            wakeOnLan.addActionListener(actionListener);
            refreshIps.addActionListener(actionListener);
            processExp.addActionListener(actionListener);
            tightVnc.addActionListener(actionListener);
            //
            popup.add(myComputer);
            popup.add(netWorkSettings);
            popup.add(rdp);
            popup.add(tightVnc);
            popup.add(portRedirectionAuto);
            popup.add(wakeOnLan);
            popup.add(processExp);
            popup.add(restart);
            popup.add(shutdown);
            popup.add(refreshIps);
            popup.add(open);
            popup.add(exit);
            //
            //
//            ArrayList list = HelpM.getCurrentEnvironmentNetworkIp();
//            String trayMsg = "ServerAdmin";
//            //
//            for (Object ip : list) {
//                trayMsg += "\n" + (String) ip;
//            }
            //
            String trayMsg = buildTrayMessage();
            //
            trayIcon = new TrayIcon(image, trayMsg, popup);
            //
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            //
            try {
                //
                tray.add(trayIcon);
                //
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
            //
        } else {
            //  System Tray is not supported
        }
    }

    private String buildTrayMessage() {
        //
        ArrayList list = HelpM.getCurrentEnvironmentNetworkIp();
        String trayMsg = "ServerAdmin:\n" + System.getProperty("user.name");
        //
        for (Object ip : list) {
            trayMsg += "\n" + (String) ip;
        }
        //
        showRefreshIpsInMainWindow(trayMsg);
        //
        return trayMsg;
    }

    private void showRefreshIpsInMainWindow(String msg) {
        jLabel1.setText(msg.replaceAll("(\r\n|\n)", " / ").replaceAll("ServerAdmin:", "").replaceFirst("/", ""));
    }
    

    private void makeVisible() {
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jButton36 = new javax.swing.JButton();
        jButton1gpedit = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jButton66 = new javax.swing.JButton();
        jButton69 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton39 = new javax.swing.JButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jButton59 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton54 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton2sessions = new javax.swing.JButton();
        textArea1 = new java.awt.TextArea();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField1RD = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton11 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton12 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jButton67 = new javax.swing.JButton();
        jButton71 = new javax.swing.JButton();
        jButton72 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jLabelOsVersion = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton21 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton53 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(680, 576));
        setResizable(false);
        getContentPane().setLayout(null);

        jTabbedPane2.setPreferredSize(new java.awt.Dimension(400, 400));
        jTabbedPane2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseDragged(evt);
            }
        });

        jPanel6.setLayout(null);

        jButton36.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton36.setText("Network settings");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton36);
        jButton36.setBounds(10, 10, 160, 40);

        jButton1gpedit.setText("Group policy (gpedit.msc)");
        jButton1gpedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1gpeditActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1gpedit);
        jButton1gpedit.setBounds(120, 320, 180, 40);

        jButton17.setText("Comp management");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton17);
        jButton17.setBounds(10, 60, 150, 40);

        jButton34.setText("Services");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton34);
        jButton34.setBounds(10, 320, 100, 40);

        jButton18.setText("ODBC32");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton18);
        jButton18.setBounds(10, 270, 90, 40);

        jButton37.setText("power options");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton37);
        jButton37.setBounds(170, 110, 130, 40);

        jButton38.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton38.setText("Region & Lang");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton38);
        jButton38.setBounds(10, 110, 150, 40);

        jButton40.setText("Screen Resolution");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton40);
        jButton40.setBounds(310, 110, 135, 40);

        jButton41.setText("System Config (msconfig)");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton41);
        jButton41.setBounds(330, 60, 179, 40);

        jButton42.setText("System Info");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton42);
        jButton42.setBounds(160, 160, 120, 40);

        jButton43.setText("System properties");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton43);
        jButton43.setBounds(290, 10, 150, 40);

        jButton44.setText("Security Center");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton44);
        jButton44.setBounds(10, 380, 130, 40);

        jButton45.setText("Firewall");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton45);
        jButton45.setBounds(150, 380, 100, 40);

        jButton47.setText("Performance monitor");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton47);
        jButton47.setBounds(290, 160, 160, 40);

        jButton48.setText("Security Policy");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton48);
        jButton48.setBounds(410, 380, 130, 40);

        jButton49.setText("Disk management");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton49);
        jButton49.setBounds(10, 160, 140, 40);

        jButton50.setText("Device management");
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton50);
        jButton50.setBounds(170, 60, 150, 40);

        jButton66.setText("ODBC 64bit");
        jButton66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton66ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton66);
        jButton66.setBounds(110, 270, 100, 40);

        jButton69.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton69.setText("START CMD");
        jButton69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton69ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton69);
        jButton69.setBounds(10, 430, 160, 60);

        jButton70.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton70.setText("CMD AS ADMIN");
        jButton70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton70ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton70);
        jButton70.setBounds(170, 430, 180, 60);

        jButton31.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton31.setText("My Computer");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton31);
        jButton31.setBounds(350, 430, 160, 60);

        jButton32.setText("Firewall advanced");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton32);
        jButton32.setBounds(260, 380, 140, 40);

        jToggleButton1.setText("Programs");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jToggleButton1);
        jToggleButton1.setBounds(180, 10, 100, 40);

        jButton39.setText("RDP");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton39);
        jButton39.setBounds(10, 220, 90, 40);

        jToggleButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jToggleButton2.setText("Win. ver");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jToggleButton2);
        jToggleButton2.setBounds(510, 430, 110, 60);

        jToggleButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jToggleButton3.setText("Kaspersky virus check");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jToggleButton3);
        jToggleButton3.setBounds(320, 320, 180, 40);

        jButton59.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton59.setText("On Screen Keyboard");
        jButton59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton59ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton59);
        jButton59.setBounds(240, 270, 170, 40);

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton3.setText("User Credentials");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3);
        jButton3.setBounds(450, 10, 170, 40);

        jToggleButton4.setText("Sound");
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });
        jPanel6.add(jToggleButton4);
        jToggleButton4.setBounds(450, 110, 130, 40);

        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jPanel6.add(jLabel1);
        jLabel1.setBounds(20, 496, 600, 20);

        jButton4.setText("CHANGE RDP PORT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton4);
        jButton4.setBounds(280, 220, 150, 40);

        jButton20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton20.setText("START UP");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton20);
        jButton20.setBounds(450, 270, 110, 40);

        jButton27.setText("ENABLE DISABLE RDP");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton27);
        jButton27.setBounds(110, 220, 160, 40);

        jButton54.setText("RDP PORT");
        jButton54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton54ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton54);
        jButton54.setBounds(440, 220, 110, 40);

        jTabbedPane2.addTab("Windows", jPanel6);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 400));

        jButton2sessions.setText("show sessions");
        jButton2sessions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2sessionsActionPerformed(evt);
            }
        });

        jButton1.setText("show processes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton6.setText("Remote Desktop");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTextField1RD.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jTextField1RD.setText("192.168.X.X");

        jCheckBox1.setText("Console");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton11.setText("IP / MAC");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton12.setText("Installed soft");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton14.setText("Sys Info");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Disk info");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton19.setText("Ping");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton22.setText("Port Ping");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton13.setText("Set shutdown time");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton23.setText("Ultra VNC");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton35.setText("Tight VNC");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton30.setText("T VNC Server");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton33.setText("Trace Route");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton68.setText("Map Drive");
        jButton68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton68ActionPerformed(evt);
            }
        });

        jButton67.setText("Clear ARP");
        jButton67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton67ActionPerformed(evt);
            }
        });

        jButton71.setText("BroadCast");
        jButton71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton71ActionPerformed(evt);
            }
        });

        jButton72.setText("Open Share");
        jButton72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton72ActionPerformed(evt);
            }
        });

        jButton26.setText("Port Redirection");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton2.setText("MAC remote");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("Installed drivers");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton46.setText("Port Redirection Auto");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jButton51.setText("Redirections.txt");
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        jButton24.setText("Wake On Lan");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton52.setText("WOL properties");
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });

        jLabelOsVersion.setForeground(new java.awt.Color(153, 153, 153));
        jLabelOsVersion.setText("OS Version:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton52, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton46)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton51, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1RD, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2sessions, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jButton14)
                                        .addGap(1, 1, 1)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton15)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(80, 80, 80)
                                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(textArea1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelOsVersion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton67, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addGap(7, 7, 7)
                        .addComponent(jButton19)
                        .addGap(3, 3, 3)
                        .addComponent(jButton22)
                        .addGap(7, 7, 7)
                        .addComponent(jButton33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton67)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton71)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton72)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(117, 117, 117)
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton30))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton14)
                                        .addComponent(jButton15))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton5)
                                        .addComponent(jButton1)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton12))
                                    .addGap(10, 10, 10)
                                    .addComponent(jButton2sessions)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1RD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jCheckBox1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                                .addComponent(jButton52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(textArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelOsVersion))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Commons", jPanel1);

        jPanel2.setLayout(new java.awt.GridLayout(6, 0));

        jButton21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton21.setText("LOOK LAN");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton21);

        jButton28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton28.setText("FileZilla (FTP)");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton28);

        jButton7.setText("SET AUTO LOGON");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);

        jButton8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton8.setText("PROCESS EXPLORER");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8);

        jButton9.setText("STARTUP PROGRAMMS");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9);

        jButton10.setText("TCP VIEW");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10);

        jButton29.setText("CURRENT PORTS");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton29);

        jButton16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton16.setText("Pick color from desktop");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton16);

        jButton25.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton25.setText("MAKE SCREEN SHOT");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton25);

        jButton53.setText("APPLICOM OPC BROWSER");
        jButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton53ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton53);

        jTabbedPane2.addTab("External", jPanel2);

        jPanel5.setLayout(new java.awt.GridLayout(3, 2));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel5.add(jScrollPane1);

        jTabbedPane2.addTab("Log", jPanel5);

        getContentPane().add(jTabbedPane2);
        jTabbedPane2.setBounds(10, 0, 640, 560);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseDragged
        System.out.println("" + this.getSize());
    }//GEN-LAST:event_jTabbedPane2MouseDragged

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        SA.run_application_2("lib/capture.exe"); // OBS! Must be run with "SA.run_application_2" otherwise not working
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        SA.run_application("lib/pixie.exe", "");
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        SA.run_application("lib/cports.exe", "");
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        //        SA.run_application("lib/tcpview.exe", "");
        HelpM.find_and_run_application("lib", "tcpview.exe");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        SA.run_application("lib/autoruns.exe", "");
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        SA.run_application("lib/procexp.exe", "");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        SA.run_application("lib/autologon.exe", "");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed
        SA.run_application("lib/applicom.exe", "");
    }//GEN-LAST:event_jButton53ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        try {
            HelpM.run_java_app_with_processbuiler("lib", "MyPortRedirection.jar", "");
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton72ActionPerformed
        String ip_prefix = chooseNetworkInterFace();
        String host = JOptionPane.showInputDialog("Type last digits", ip_prefix + ".");
        HelpM.openShareWithExploere(host);
    }//GEN-LAST:event_jButton72ActionPerformed

    private void jButton71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton71ActionPerformed

        String prefix = chooseNetworkInterFace();

        if (prefix == null) {
            return;
        }
        //
        textArea1.setText("");
        //
        for (int i = 1; i < 255; i++) {
            MyBroadCast bc = new MyBroadCast(prefix + "." + i, textArea1);
        }
    }//GEN-LAST:event_jButton71ActionPerformed

    private void jButton67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton67ActionPerformed
        HelpM.showCmdInstructionInTextField("netsh interface ip delete arpcache");
        //
    }//GEN-LAST:event_jButton67ActionPerformed

    private void jButton68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton68ActionPerformed
        String host = JOptionPane.showInputDialog("Type host");
        String driveLetterToUse = JOptionPane.showInputDialog("Type DriveLetterToUse");
        String driveOrFolderToMap = JOptionPane.showInputDialog("Type driveOrFolderToMap");
        String user = JOptionPane.showInputDialog("Type user");
        String pass = JOptionPane.showInputDialog("Type pass");
        //
        HelpM.generate_map_network_drive_str(host, driveLetterToUse, driveOrFolderToMap, user, pass);
    }//GEN-LAST:event_jButton68ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //
                String host = HelpM.getLastEntered("lib/_tracert.io", "Specify ip");
                //
                if (host.isEmpty() == false) {
                    HelpM.run_program_with_catching_output(textArea1, "tracert", "", host, "", "");
                }
                //
            }
        };
        //
        Thread x = new Thread(r);
        x.start();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        try {
            HelpM.run_java_app_with_processbuiler("lib/myvnc", "myvnc.jar", "start");
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        SA.run_application("lib/tvnviewer.exe", "");
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        //        SA.run_application("lib/filezilla/filezilla.exe", "");
        HelpM.find_and_run_application("lib", "filezilla.exe");
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        SA.run_application("lib/ultravnc.exe", "");
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        SHUT_DOWN = new ShutDown(image);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //
                SA.pingPort();
                //
            }
        };
        //
        Thread x = new Thread(r);
        x.start();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        SA.run_application("lib/looklan/lookatlan.exe", "");
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //
                String host = HelpM.getLastEntered("lib/_ping.io", "Specify ip");
                //
                if (host.isEmpty() == false) {
                    HelpM.run_program_with_catching_output(textArea1, "ping", "", host, "", "");
                }
                //
            }
        };
        //
        Thread x = new Thread(r);
        x.start();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        //        SA.run_program_with_catching_output(textArea1, "lib/psinfo", "-d", "");
        HelpM.run_program_with_catching_output(textArea1, "psinfo.exe", "lib", "-d", "", "");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        //        SA.run_program_with_catching_output(textArea1, "lib/psinfo", "", "");
        HelpM.run_program_with_catching_output(textArea1, "psinfo.exe", "lib", "", "", "");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        //        SA.run_program_with_catching_output(textArea1, "lib/psinfo", "-s", "");
        HelpM.run_program_with_catching_output(textArea1, "psinfo.exe", "lib", "-s", "", "");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        //        SA.run_program_with_catching_output(textArea1, "ipconfig", "", "");
        HelpM.run_program_with_catching_output(textArea1, "ipconfig", "", "/all", "", "");
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (console == false) {
            console = true;
        } else if (console) {
            console = true;
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (console) {
            String[] commands2 = {"mstsc.exe", "/v:" + jTextField1RD.getText(), "/f", "/Admin"};
            SA.run_application(commands2);
        } else {
            String[] commands2 = {"mstsc.exe", "/v:" + jTextField1RD.getText(), "/f"};
            SA.run_application(commands2);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //        SA.run_program_with_catching_output(textArea1, "lib/query.exe", "process", "");
        HelpM.run_program_with_catching_output(textArea1, "query.exe", "lib", "process", "", "");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2sessionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2sessionsActionPerformed
        HelpM.run_program_with_catching_output(textArea1, "query.exe", "lib", "session", "", "");
    }//GEN-LAST:event_jButton2sessionsActionPerformed

    private void jButton59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton59ActionPerformed
        SA.run_with_cmd("osk", "");
    }//GEN-LAST:event_jButton59ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        if (Desktop.isDesktopSupported()) {
            try {
                try {
                    Desktop.getDesktop().browse(new URI("https://virusdesk.kaspersky.com/"));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        SA.run_with_cmd("winver", "");
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        SA.run_with_cmd("mstsc", "");
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        SA.run_with_cmd("appwiz.cpl", "");
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        SA.run_with_cmd("wf.msc", "");
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        try {
            SA.openMyComputer();
        } catch (AWTException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton70ActionPerformed
        String adminUser = JOptionPane.showInputDialog("Type Admin account name");
        //
        String[] commands = {"cmd", "/k", "start", "\"" + "runas" + "\"", "runas", "/user:" + adminUser, "cmd"};
        HelpM.runWithBuilder(commands);
    }//GEN-LAST:event_jButton70ActionPerformed

    private void jButton69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton69ActionPerformed
        try {
            HelpM.run_cmd();
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton69ActionPerformed

    private void jButton66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton66ActionPerformed
//        String str = "%windir%\\syswow64\\odbcad32.exe";
        //        HelpM.showCmdInstructionInTextArea(str);
//        SA.run_with_cmd("%windir%\\syswow64\\odbcad32.exe", "");
        SA.run_with_cmd("odbcad32", "");
    }//GEN-LAST:event_jButton66ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        SA.run_with_cmd("devmgmt.msc", "");
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        SA.run_with_cmd("diskmgmt.msc", "");
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        SA.run_with_cmd("secpol.msc", "");
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        SA.run_with_cmd("perfmon.msc", "");
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        SA.run_with_cmd("firewall.cpl", "");
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        SA.run_with_cmd("wscui.cpl", "");
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        SA.run_with_cmd("sysdm.cpl", "");
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        SA.run_with_cmd("msinfo32", "");
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        SA.run_with_cmd("msconfig", "");
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        SA.run_with_cmd("desk.cpl", "");
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        SA.run_with_cmd("intl.cpl", "");
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        SA.run_with_cmd("powercfg.cpl", "");
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed

        SA.run_with_cmd("%windir%\\syswow64\\odbcad32.exe", "");
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        SA.run_with_cmd("services.msc", "");
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        SA.run_with_cmd("compmgmt.msc", "");
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton1gpeditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1gpeditActionPerformed
        SA.run_with_cmd("gpedit.msc", "");
        textArea1.append("1. Disable Task Manager = User Configuration - Administrative Templates - System - Ctrl + "
                + "Alt + Del Option - Remove Task Manager \n");
        textArea1.append("2. Disable LogOff Btn = User Configuration - Administrative Templates - Start Menu and Taskbar "
                + " - Remove LoggOff to the Start Menu \n");
        textArea1.append("3. Disable Autorun = Computer Configuration - Administrative Templates - System - Turn off Autoplay (property) \n");
        textArea1.append("4. Set disconnect for an idle session: User Configuration -> Administrative Templates -> Windows Components -> Terminal Services -> Sessions \n");
    }//GEN-LAST:event_jButton1gpeditActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        SA.run_with_cmd("ncpa.cpl", "");
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        textArea1.setText("");
        String ip_prefix = chooseNetworkInterFace();
        String host = JOptionPane.showInputDialog("Type last digits", ip_prefix + ".");
        try {
            HelpM.getMacAddrHost(textArea1, host);
        } catch (Exception ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        HelpM.showCmdInstructionInTextField("rundll32.exe keymgr.dll, KRShowKeyMgr");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        HelpM.run_program_with_catching_output(textArea1, "driverquery", "", "-v", "", "");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        try {
            HelpM.run_java_app_with_processbuiler("lib", "MyPortRedirectionAuto.jar", "");
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        try {
            HelpM.run_application_with_associated_application(new File("lib/redirections.txt"));
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        try {
            HelpM.run_java_app_with_processbuiler("lib/wol", "WakeOnLan.jar", "");
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        try {
            HelpM.run_application_with_associated_application(new File("lib/wol/main.properties"));
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //
        TextFieldCheck_simple tfc = new TextFieldCheck_simple("\\d{4}", 10);
        boolean yesNo = chooseFromJTextFieldWithCheck(tfc, "Type port");
        String rst = tfc.getText();
        //
        if(yesNo == false || rst == null || rst.isEmpty()){
            return;
        }
        //
        WinRegistry.change_rdp_port(Integer.parseInt(rst));
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        String username = JOptionPane.showInputDialog("Type username");
        String path = "C:/Users/"+username+"/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup";
        HelpM.showCmdInstructionInTextArea(path);
        SA.open_dir(path);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        WinRegistry.enable_disable_rdp();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton54ActionPerformed

    private String chooseNetworkInterFace() {
        ArrayList<String> interface_list = HelpM.getCurrentEnvironmentNetworkIp();
        String menu = "Choose Network Interface:";
        //
        for (int i = 0; i < interface_list.size(); i++) {
            menu += "\n " + (i + 1) + ". " + interface_list.get(i);
        }
        //
        int val = Integer.parseInt(JOptionPane.showInputDialog(menu));

        String interfacee = interface_list.get(val - 1);

        return HelpM.extract_first_3_parts_of_ip(interfacee);
    }

    public static String get_proper_date_time_same_format_on_all_computers() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH_mm");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //
//        HelpMy.err_output_to_file();
        //
        HelpM.nimbusLookAndFeel();
        //
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
//                HelpM.err_output_to_file();
                //
                new mainForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    public static javax.swing.JButton jButton1gpedit;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    public static javax.swing.JButton jButton2sessions;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelOsVersion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    public static javax.swing.JTextField jTextField1RD;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    public static java.awt.TextArea textArea1;
    // End of variables declaration//GEN-END:variables
}
