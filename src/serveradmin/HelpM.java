/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveradmin;

//import com.jezhumble.javasysmon.JavaSysMon;
//import com.jezhumble.javasysmon.ProcessInfo;
import java.awt.Desktop;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import myDialogs.TextFieldCheck_Sql;
import myDialogs.myDialogs;

/**
 *
 * @author KOCMOC
 */
public class HelpM {

    public static void err_output_to_file() {
        //Write error stream to a file
        create_dir_if_missing("err_output");
        try {
            String err_file = "err_" + get_date_time() + ".txt";
            String output_path = "err_output/" + err_file;

            PrintStream out = new PrintStream(new FileOutputStream(output_path));
            System.setErr(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HelpM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void create_dir_if_missing(String path_and_folder_name) {
        File f = new File(path_and_folder_name);
        if (f.exists() == false) {
            f.mkdir();
        }
    }

    public static String get_date_time() {
        DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String getLastEntered(String filePath, String msg) {
        //
        ArrayList<String> list;
        //
        String previous = null;
        //
        if (HelpM.file_exists(filePath)) {
            //
            list = HelpM.read_Txt_To_ArrayList(filePath);
            //
            if (list.isEmpty() == false) {
                previous = list.get(0);
            }
            //
        }
        //
        String actual;
        //
        if (previous == null || previous.isEmpty()) {
            //
            actual = JOptionPane.showInputDialog(msg);
            //
        } else {
            //
            actual = JOptionPane.showInputDialog(msg, previous);
            //
        }
        //
        if (actual == null || actual.isEmpty()) {
            return "";
        }
        //
        try {
            HelpM.writeToFile(filePath, actual, false);
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        return actual;
    }

    public static void runWithBuilder(String[] commands) {
        ProcessBuilder builder = new ProcessBuilder(commands);
        try {
            builder.start();
        } catch (IOException ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void run_java_app_with_processbuiler(String app_path, String name, String arg) throws IOException {
        String[] commands = {"java", "-jar", name, arg};
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.directory(new File(app_path));
        builder.start();
    }

    public static Process run_cmd() throws IOException {
        String[] commands = new String[3];
        commands[0] = "cmd";
        commands[1] = "/c";
        commands[2] = "start";
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.directory(new File("lib"));
        return builder.start();
    }

    public static void generate_psexec_str_for_launching_cmd_remotely(String host, String username, String pass) {
        String instr = "psexec \\\\" + host + " -u " + username + " -p " + pass + " cmd";
        showCmdInstructionInTextField(instr);
    }

    public static void generate_pskill_str_remote(String host, String username, String pass, String processOrPid) {
        String instr = "pskill \\\\" + host + " -u " + username + " -p " + pass + " " + processOrPid;
        showCmdInstructionInTextField(instr);
    }

    public static void generate_enable_rdp_str() {
        String str = "reg add \"HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Terminal Server\" /v fDenyTSConnections /t REG_DWORD /d 0 /f";
        showCmdInstructionInTextField(str);
    }

    public static void generate_disable_rdp_str() {
        String str = "reg add \"HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Terminal Server\" /v fDenyTSConnections /t REG_DWORD /d 1 /f";
        showCmdInstructionInTextField(str);
    }

    public static void generate_useful_cmd_commands() {
        String str = "";
        str += "\n1. tasklist = show processes running";
        str += "\n2. querry session = show sessions";
        str += "\n3. tsdiscon 0 = disconnect session (0 nr of the session)";
        showCmdInstructionInTextArea(str);
    }

    public static void generate_enable_remote_admin_str() {
        String str = "";
        str += "\nThis is to be done on the Machine which is to be administrated remotely";
        str += "\n\nInstruction: Use *regedit* and find the *EnableLUA* setting and set to *0* ";
        str += "\n\n HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\System\\EnableLUA";
        showCmdInstructionInTextArea(str);
    }

    public static void generate_map_network_drive_str(String host, String driveLetterToUse, String driveToMap, String user, String pass) {
        String str = "net use " + driveLetterToUse + ": \\\\" + host + "\\" + driveToMap + " /user:" + user + " " + pass;
        showCmdInstructionInTextField(str);
    }

    public static void showCmdInstructionInTextField(String instr) {
        JTextField jtf = new JTextField(instr);
        JOptionPane.showMessageDialog(null, jtf);
    }

    public static void showCmdInstructionInTextArea(String instr) {
        JTextArea jta = new JTextArea(instr);
        JOptionPane.showMessageDialog(null, jta);
    }

    public static void writeToFile(String fileName, String textToWrite, boolean apend) throws IOException {
        FileWriter fstream = new FileWriter(fileName, apend);
        BufferedWriter out = new BufferedWriter(fstream);
        //
        out.write(textToWrite);
        out.newLine();
        //Very Important this makes that file is not in use after this operation
        out.flush();
        out.close();
//        fstream.flush();
//        fstream.close();
    }

    public static ArrayList<String> read_Txt_To_ArrayList(String filename) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String rs = br.readLine();
            while (rs != null) {
                list.add(rs);
                rs = br.readLine();
            }
            //
            br.close();
            //
        } catch (IOException e) {
            Logger.getLogger(HelpM.class.getName()).log(Level.SEVERE, null, e);
        }
        //
        return list;
    }

    public static void main(String[] args) {
        try {
            run_cmd();
        } catch (IOException ex) {
            Logger.getLogger(HelpM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void find_and_run_application(String path, String application_to_run_name) {
        File[] f = new File(path).listFiles();

        for (File file : f) {
            if (file.isDirectory()) {
                find_and_run_application(file.getPath(), application_to_run_name);
            } else if (file.getName().toLowerCase().contains(application_to_run_name.toLowerCase())) {
                try {
                    run_application_exe_or_jar(application_to_run_name, file.getParent());
                } catch (IOException ex) {
                    Logger.getLogger(HelpM.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Runs both .exe & .jar applications
     *
     * @param application_to_run_name
     * @param path
     * @throws IOException
     */
    public static Process run_application_exe_or_jar(String application_to_run_name, String path) throws IOException {
        String[] commands = new String[3];
        if (application_to_run_name.contains(".jar")) {
            commands[0] = "java";
            commands[1] = "-jar";
            commands[2] = application_to_run_name; //OBS! pay attention here
        } else {
            commands[0] = path + "/" + application_to_run_name; // and here!
            commands[1] = "";
            commands[2] = "";
        }
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.directory(new File(path));
        return builder.start();
    }

    private static void terminate_process_no_external_apps_in_use(String processName) {
//        JavaSysMon monitor = new JavaSysMon();
//        ProcessInfo[] pinfo = monitor.processTable();
//
//        for (int i = 0; i < pinfo.length; i++) {
//            String pname = pinfo[i].getName();
//            int pid = pinfo[i].getPid();
//            if (pname.toLowerCase().equals(processName.toLowerCase())) {
//                monitor.killProcess(pid);
//            }
//        }
    }

    private static boolean processRunning(String processName) {
//        JavaSysMon monitor = new JavaSysMon();
//        ProcessInfo[] pinfo = monitor.processTable();
//        for (int i = 0; i < pinfo.length; i++) {
//            String pname = pinfo[i].getName();
//            if (pname.toLowerCase().equals(processName.toLowerCase())) {
//                return true;
//            }
//
//        }
        return false;
    }

    public static void run_program_with_catching_output(TextArea textarea, String appName, String path, String cmd1, String cmd2, String cmd3) {
        textarea.setText("");
        String[] commands2 = {cmd1, cmd2, cmd3};
        try {
            run_program_with_catching_output_overall(textarea, appName, path, commands2);
        } catch (IOException ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void run_program_with_catching_output_overall(TextArea textarea, String appName, String path, String[] commands) throws IOException {
        String line;
        OutputStream stdin;
        InputStream stderr;
        InputStream stdout;

        Process process = run_application_exe_for_output_catching(appName, path, commands);

        // launch EXE and grab stdin/stdout and stderr
        stdin = process.getOutputStream();
        stderr = process.getErrorStream();
        stdout = process.getInputStream();

        // "write" the parms into stdin
        line = "param1" + "\n";
        stdin.write(line.getBytes());
        stdin.flush();

        line = "param2" + "\n";
        stdin.write(line.getBytes());
        stdin.flush();

        line = "param3" + "\n";
        stdin.write(line.getBytes());
        stdin.flush();

        stdin.close();

        // clean up if any output in stdout
        BufferedReader brCleanUp
                = new BufferedReader(new InputStreamReader(stdout));
        while ((line = brCleanUp.readLine()) != null) {
            textarea.append("" + line + "\n");
        }
        brCleanUp.close();

        // clean up if any output in stderr
        brCleanUp
                = new BufferedReader(new InputStreamReader(stderr));
        while ((line = brCleanUp.readLine()) != null) {
            textarea.append("" + line);
            System.out.println("[Stderr] " + line);
        }
        brCleanUp.close();
    }

    private static Process run_application_exe_for_output_catching(String application_to_run_name, String path, String[] args) throws IOException {
        String[] commands = new String[4];
        //
        if (path.isEmpty() == false) {
            commands[0] = path + "/" + application_to_run_name; // and here!
        } else {
            commands[0] = application_to_run_name;
        }
        //
        commands[1] = args[0];
        commands[2] = args[1];
        commands[3] = args[2];
        //
        ProcessBuilder builder = new ProcessBuilder(commands);
        //
        if (path.isEmpty() == false) {
            builder.directory(new File(path));
        }
        //
        return builder.start();
    }

    public static void run_application_with_associated_application(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    public static ArrayList getCurrentEnvironmentNetworkIp() {
        Enumeration<NetworkInterface> netInterfaces;
        ArrayList<String> network_interface_list = new ArrayList();
        String currentHostIpAddress = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();

            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();

                    if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
                            && !(addr.getHostAddress().indexOf(":") > -1)) {
                        currentHostIpAddress = addr.getHostAddress();
                        network_interface_list.add(currentHostIpAddress);
                    }
                }
            }
            if (currentHostIpAddress == null) {
                currentHostIpAddress = "127.0.0.1";
            }

        } catch (SocketException e) {
//                log.error("Somehow we have a socket error acquiring the actual IP... Using loopback instead...");
            currentHostIpAddress = "127.0.0.1";
        }
        return network_interface_list;
    }

    public static String extract_first_3_parts_of_ip(String ip) {
        String[] arr = ip.split("\\.");
        //
        if (arr.length == 4) {
            return arr[0] + "." + arr[1] + "." + arr[2];
        } else {
            return "";
        }
    }

    public static void openShareWithExploere(String host) {
        if (host == null) {
            return;
        }
        String[] arr = {"cmd", "/k", "start", "\"" + "runas" + "\"", "explorer", "\\\\" + host};
        runWithBuilder(arr);
    }

    public static boolean file_exists(String path) {
        File f = new File(path);
        return f.exists();
    }

    //==========================================================================
    /**
     * THis is the main one
     *
     * @param host
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static void getMacAddrHost(TextArea txt, String host) throws IOException, InterruptedException {
        //
        boolean ok = ping3(host);
        //
        if (ok) {
            InetAddress address = InetAddress.getByName(host);
            String ip = address.getHostAddress();
            txt.setText(getMacAddrHost_run_with_output("arp -a " + ip));
        }
        //
    }

    private static boolean ping3(String host) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "1", host);
        Process proc = processBuilder.start();

        int returnVal = proc.waitFor();
        return returnVal == 0;
    }

    private static String getMacAddrHost_run_with_output(String param) throws IOException {
        Process p = Runtime.getRuntime().exec(param);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            if (!line.trim().equals("")) {
                // keep only the process name
                line = line.substring(1);
                String mac = getMacAddrHost_extractmac(line);
                if (mac.isEmpty() == false) {
                    return mac;
                }
            }
        }
        return null;
    }

    private static String getMacAddrHost_extractmac(String str) {
        String arr[] = str.split("   ");
        for (String string : arr) {
            if (string.trim().length() == 17) {
                return string.trim().toUpperCase();
            }
        }
        return "";
    }

//==========================================================================
    public static void nimbusLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HelpM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HelpM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HelpM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HelpM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

}
