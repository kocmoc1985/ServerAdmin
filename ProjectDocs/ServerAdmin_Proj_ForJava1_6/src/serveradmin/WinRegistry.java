///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package serveradmin;
//
//import ca.beq.util.win32.registry.RegistryKey;
//import ca.beq.util.win32.registry.RegistryValue;
//import ca.beq.util.win32.registry.RootKey;
//import ca.beq.util.win32.registry.ValueType;
//
//
//import javax.swing.JOptionPane;
//
///**
// *
// * @author KOCMOC
// */
//public class WinRegistry {
//
//    public static void change_rdp_port(int port) {
//        //
//        String KEY = "PortNumber";
//        //
//        RegistryKey r = new RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "System\\CurrentControlSet\\Control\\Terminal Server\\WinStations\\RDP-Tcp");
//        //
//        if (r.exists() == false) {
//            JOptionPane.showMessageDialog(null, "Registry key does not exist", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        //
//        RegistryValue v = new RegistryValue(KEY, ValueType.REG_DWORD, port);
//        r.setValue(v);
//        //
//    }
//
//    public static String get_rdp_port() {
//        //
//        String KEY = "PortNumber";
//        //
//        RegistryKey r = new RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "System\\CurrentControlSet\\Control\\Terminal Server\\WinStations\\RDP-Tcp");
//        //
//        if (r.exists() == false) {
//            JOptionPane.showMessageDialog(null, "Registry key does not exist", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        //
////        RegistryValue v = new RegistryValue("PortNumber", ValueType.REG_DWORD);
//        //
//        return r.getValue(KEY).getStringValue();
//    }
//
//    public static void enable_disable_rdp() {
//        //
//        String KEY = "fDenyTSConnections";
//        //
//        RegistryKey r = new RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "System\\CurrentControlSet\\Control\\Terminal Server");
//        //
//        if (r.exists() == false) {
//            JOptionPane.showMessageDialog(null, "Registry key does not exist", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        //
//        if (r.hasValue(KEY) == false) {
//            JOptionPane.showMessageDialog(null, "Reg key:" + KEY + " -> not found", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        //
//        int val = Integer.parseInt(r.getValue(KEY).getStringValue());
//        //
//        if (val == 0) { // 0 = enabled / 1 = disabled
//            r.setValue(new RegistryValue(KEY, 1));
//            JOptionPane.showMessageDialog(null, "RDP Disabled", "Info", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            r.setValue(new RegistryValue(KEY, 0));
//            JOptionPane.showMessageDialog(null, "RDP Enabled", "Info", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//}
