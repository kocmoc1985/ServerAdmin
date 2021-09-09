/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import statics.HelpMy;

/**
 * OPCReader_SQL for reading data from MCBrowsers database
 *
 * @author Administrator
 */
public class OPCReader_SQL {

    private boolean opc_read_ok = false;
    private Thread opc_reader_thread;
    private final int line_id = 1;
    private Sql_B sql = new Sql_B(false, false);
    //
    public String RECIPE = "";
    public String ORDER = "";
    public String BATCH = "";
    public static String BATCH_ID;
    private ResultSet RS;
    //
    public final static String CONTROL_START = "charge_start";
    public final static String BATCH_MC_MODE = "charge_active";
    public final static String DISCHARGE = "discharge";
    public final static String TOTAL_ADD = "total_add";
    public final static String RB = "rubber";
    public final static String OIL = "oil";
    public final static String CB = "carbon";
    public final static String FILLERS = "fillers";
    public final static String SMALL_CH = "small_chemicals";
    public final static String RCL = "rcl";
    //
    public final static String SPEED_MIXER = "speed_mixer";
    public final static String TEMP_MIXER = "temperature_mixer";
    public final static String TORQ_MIXER = "torque_mixer";
    public final static String RAM_POS_MIXER = "ramposition_mixer";
    public final static String RAM_PRESSURE_MIXER = "rampressure_mixer";

    public OPCReader_SQL(Sql_B connectedSql, String batchId) {
        //
        this.sql = connectedSql;
        BATCH_ID = batchId;
        //
        getResultSet();
    }

    public OPCReader_SQL(Sql_B connectedSql, String recipe, String order, String batch) {
        this.RECIPE = recipe;
        this.ORDER = order;
        this.BATCH = batch;
        //
        this.sql = connectedSql;
        //
        findBatchId();
        getResultSet();
    }

    public OPCReader_SQL(String recipe, String order, String batch) {
        this.RECIPE = recipe;
        this.ORDER = order;
        this.BATCH = batch;
        init();
    }

    private void init() {
        if (connectSql()) {
            checkDuplicity();
            findBatchId();
            getResultSet();
        } else {
            JOptionPane.showMessageDialog(null, "Sql connection failed: EXITING");
            System.exit(0);
        }
    }

    private boolean connectSql() {
        try {
//            sql.connect_tds("10.87.0.2", "1433", "FederalMogul", "sa", "", false, "", 
            sql.connect_tds("10.87.0.2", "1433", "FederalMogul", "sa", "", false, "", "");
//            sql.connect_odbc("", "", "COMPOUND_DEMO");
//            sql.connect_mdb("", "", "demoFiles/export.mdb");
//            System.out.println("FakeOPC SQL connection ok");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(OPCReader_SQL.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void checkDuplicity() {
//        int ids = Sql_B.getRowCount(sql, "MC_BATCHINFO", "ARBEITSREF = '" + ORDER + "' and BATCHNO='" + BATCH + "'");
//        //
//        if(ids == 0){
//             JOptionPane.showMessageDialog(null, "Batch not found");
//        }
//        //
//        if (ids > 1) {
//            JOptionPane.showMessageDialog(null, "OBS duplicity detected!");
//        }
    }

    private void findBatchId() {
        try {
            //
            String q = "select ID from MC_BATCHINFO where "
                    + "OrderName = '" + ORDER + "'"
                    + "AND "
                    + "BATCHNO = " + BATCH + "";
            //
            ResultSet rs = sql.execute(q);
            //
            System.out.println("q: " + q);
//             ResultSet rs = sql.execute("select ID from MC_BATCHINFO where "
//                    + "ARBEITSREF = '" + ORDER + "'"
//                    + "AND "
//                    + "BATCHNO = " + BATCH + "");
            //
            if (rs.next()) {
                BATCH_ID = rs.getString("ID");
                System.out.println("BATCH_ID: " + BATCH_ID);
            }

        } catch (SQLException ex) {
            Logger.getLogger(OPCReader_SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getResultSet() {
        try {
            RS = sql.execute("select * from MC_TREND where IDBATCHINFO=" + BATCH_ID + " order by TICKS asc");
        } catch (SQLException ex) {
            Logger.getLogger(OPCReader_SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLine() {
        return this.line_id;
    }

    /**
     * This method should be called from the server To start reading from the
     * PLC
     */
    public void startReading() {
        if (opc_reader_thread == null) {
            return;
        }
        opc_read_ok = true;
        opc_reader_thread.start();
        //
    }

    /**
     * Stops reading signals
     */
    public void stopReading() {
        opc_read_ok = false;
    }
    private boolean oneTimeFlag = false;
    private String date;
    private long now;

    public boolean resetResultSet() {
        try {
            RS.beforeFirst();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OPCReader_SQL.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public int getSize() throws SQLException {
        //
        int size = 0;
        resetResultSet();
        //
        while (RS.next()) {
            size++;
        }
        //
        resetResultSet();
        //
        return size;

    }

    /**
     *
     * @param call
     * @return
     */
    public Properties aquire_signals_within_one_sec(String call) {
        //
        Properties one_sec_signal_map = new Properties();// reset values
        //
        try {
            if (RS.next()) {
                //
                int digital_in = RS.getInt("DIGITAL_IN");
                char[] digital_signals = HelpMy.reverseCharArr(HelpMy.decimal_to_binary(digital_in).toCharArray());
                //
//              System.out.println("Dig in: " + new String(digital_signals));
                //
                one_sec_signal_map = fillSignals(digital_signals, RS);
                //
                if (sql.ODBC_OR_MDB) {
                    if (!oneTimeFlag) {
                        now = System.currentTimeMillis();
                        date = millisToDateConverter("" + now);
                        oneTimeFlag = true;
                    } else {
                        now += 1000;
                        date = millisToDateConverter("" + now);
                    }

                } else {
                    date = RS.getString("RTime");
                }
                //
                one_sec_signal_map.put("date", date);
                //
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OPCReader_SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        return one_sec_signal_map;
    }

    public Properties fillSignals(char[] digital_signals, ResultSet RS) throws SQLException {
        //
        Properties one_sec_signal_map = new Properties();
        //
        one_sec_signal_map.put(BATCH_MC_MODE, HelpMy.charToBoolean(digital_signals[0]));
        one_sec_signal_map.put(CONTROL_START, HelpMy.charToBoolean(digital_signals[1]));
        one_sec_signal_map.put(DISCHARGE, HelpMy.charToBoolean(digital_signals[2]));
        one_sec_signal_map.put(TOTAL_ADD, HelpMy.charToBoolean(digital_signals[11]));
        one_sec_signal_map.put(RB, HelpMy.charToBoolean(digital_signals[8]));
        one_sec_signal_map.put(OIL, HelpMy.charToBoolean(digital_signals[7]));
        one_sec_signal_map.put(CB, HelpMy.charToBoolean(digital_signals[9]));
        // one_sec_signal_map.put(FILLERS, HelpM.charToBoolean(digital_signals[???])); // On fedmog fillers dont exist
        one_sec_signal_map.put(SMALL_CH, HelpMy.charToBoolean(digital_signals[10]));
        one_sec_signal_map.put(RCL, HelpMy.charToBoolean(digital_signals[14]));
        //
        one_sec_signal_map.put(TORQ_MIXER, RS.getString("ANALOG1"));//RS.getString("ANALOG1")
        one_sec_signal_map.put(TEMP_MIXER, RS.getString("ANALOG2"));//RS.getString("ANALOG2")
        one_sec_signal_map.put(SPEED_MIXER, RS.getString("ANALOG3"));//RS.getString("ANALOG3")
        one_sec_signal_map.put(RAM_POS_MIXER, RS.getString("ANALOG4"));//RS.getString("ANALOG4")
        one_sec_signal_map.put(RAM_PRESSURE_MIXER, RS.getString("ANALOG5"));//RS.getString("ANALOG5")
        //
        one_sec_signal_map.put(RECIPE, RECIPE);//RECIPE
        one_sec_signal_map.put(ORDER, ORDER); //ORDER
        one_sec_signal_map.put(BATCH, BATCH); // BATCH
        //
        return one_sec_signal_map;
    }

    public static String millisToDateConverter(String millis) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // this works to!
        long now = Long.parseLong(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }

    public boolean isActive() {
        return opc_read_ok;
    }

    public static void main(String[] args) {
        OPCReader_SQL opcr = new OPCReader_SQL("", "180312", "79819");
        System.out.println("" + opcr.aquire_signals_within_one_sec(""));

    }

}
