/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-A
 */
public class OPCReader_SQL_Consumer {

    public OPCReader_SQL reader_SQL;
    public Sql_B sql_B__src = new Sql_B(false, false); // SOURCE DATABASE
    public Sql_B sql_B__dest = new Sql_B(false, false); // DEST DATABASE
    public PreparedStatement preparedStatement;
    public int i = 0;

    public OPCReader_SQL_Consumer(Sql_B sql_B__src, Sql_B sql_B__dest) {
        this.sql_B__src = sql_B__src;
        this.sql_B__dest = sql_B__dest;
        prepare();
    }

    public OPCReader_SQL_Consumer() {
        connectDestAndPrepareStatement();
        connectSource();
    }

    private boolean connectSource() {
        try {
//            sql_B__src.connect_tds("85.227.248.129", "3389", "FederalMogul", "sa", "", false, "", "");
            sql_B__src.connect("mysql", "10.87.0.145", "3306", "igt", "root", "0000");
//            sql.connect_odbc("", "", "COMPOUND_DEMO");
//            sql.connect_mdb("", "", "demoFiles/export.mdb");
            return true;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OPCReader_SQL.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SqlTypeNotSpecifiedException ex) {
            Logger.getLogger(OPCReader_SQL_Consumer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OPCReader_SQL.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void connectDestAndPrepareStatement() {
        //
        try {
            sql_B__dest.connect("mysql", "10.87.0.175", "3306", "signals", "root", "0000");
        } catch (ClassNotFoundException | SQLException | SqlTypeNotSpecifiedException ex) {
            Logger.getLogger(OPCReader_SQL_Consumer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        //
        prepareStatement();
        //
    }

    private void prepare() {
        prepareStatement();
    }

    public void prepareStatement() {
        //
        String rst = "update input set  LastUpdate=?, Watchdog=?,"
                + "SIGNAL1=?,SIGNAL2=?,SIGNAL3=?,SIGNAL4=?,SIGNAL5=?,SIGNAL6=?,SIGNAL7=?,SIGNAL8=?,SIGNAL9=?,SIGNAL10=?,"
                + "SIGNAL11=?,SIGNAL12=?,SIGNAL13=?,SIGNAL14=?,SIGNAL15=?,SIGNAL16=?,SIGNAL17=?,SIGNAL18=?,SIGNAL19=?,SIGNAL20=?,"
                + "SIGNAL21=?,SIGNAL22=?,SIGNAL23=?,SIGNAL24=?,SIGNAL25=?,SIGNAL26=?,SIGNAL27=?,SIGNAL28=?,SIGNAL29=?,SIGNAL30=?,"
                + "SIGNAL31=?,SIGNAL32=?,SIGNAL33=?,SIGNAL34=?,SIGNAL35=?,SIGNAL36=?,SIGNAL37=?,SIGNAL38=?,SIGNAL39=?,SIGNAL40=?,"
                + "SIGNAL41=?,SIGNAL42=?,SIGNAL43=?,SIGNAL44=?,SIGNAL45=?,SIGNAL46=?,SIGNAL47=?,SIGNAL48=?,SIGNAL49=?,SIGNAL50=?";
        //
        try {
            preparedStatement = sql_B__dest.prepareStatementB(rst);
        } catch (SQLException ex) {
            Logger.getLogger(OPCReader_SQL_Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
    }

    public void update(Properties signalMap) throws SQLException {
        //
        i++;
        //
        String MC_IN_CHARGE;
        //
        String total_add = signalMap.getProperty(OPCReader_SQL.TOTAL_ADD);
        //
        if (total_add.equals("false")) {
            MC_IN_CHARGE = "1";
            System.out.println("MC_IN_CHARGE: 1_______________________________");
        } else {
            MC_IN_CHARGE = "0";
        }
        //
        preparedStatement.setString(1, get_proper_date_time_same_format_on_all_computers());
        preparedStatement.setString(2, "" + i);
        preparedStatement.setString(3, signalMap.getProperty(OPCReader_SQL.TORQ_MIXER)); // 1. POWER
        preparedStatement.setString(4, signalMap.getProperty(OPCReader_SQL.TORQ_MIXER)); // 2. CURRENT 
        preparedStatement.setString(5, signalMap.getProperty(OPCReader_SQL.TEMP_MIXER)); // 3. TEMP_1 
        preparedStatement.setString(6, signalMap.getProperty(OPCReader_SQL.TEMP_MIXER)); // 4. TEMP_2 
        preparedStatement.setString(7, signalMap.getProperty(OPCReader_SQL.TEMP_MIXER)); // 5. TEMP_3 
        preparedStatement.setString(8, signalMap.getProperty(OPCReader_SQL.SPEED_MIXER)); // 6. SPEED 
        preparedStatement.setString(9, signalMap.getProperty(OPCReader_SQL.RAM_POS_MIXER)); // 7. RAM 
        preparedStatement.setString(10, signalMap.getProperty(OPCReader_SQL.RAM_PRESSURE_MIXER)); // 8. PRESSURE 
        preparedStatement.setString(11, "-1");// 9. SET_SPEED 
        preparedStatement.setString(12, "-1");// 10. Set_Pressure 
        preparedStatement.setString(13, "-1");// 11. GAP 
        preparedStatement.setString(14, "-1");// 12. TORQUE
        preparedStatement.setString(15, "-1");// 13. SET_GAP
        preparedStatement.setString(16, "-1");// 14. PLC_POSITION
        preparedStatement.setString(17, "-1");// 15. PLC_WATCHDOG
        preparedStatement.setString(18, "-1");// 16. MC_POSITION
        preparedStatement.setString(19, "-1");// 17. MC_WATCHDOG
        preparedStatement.setString(20, "-1");// 18. MC_READY
        preparedStatement.setString(21, "-1");// 19. AlARM_ID_PLC
        preparedStatement.setString(22, "-1");// 20. BATCH_MC_MODE
        preparedStatement.setString(23, signalMap.getProperty(OPCReader_SQL.CONTROL_START));// 21. CONTROL_START
        preparedStatement.setString(24, signalMap.getProperty(OPCReader_SQL.DISCHARGE));// 22. DISCHARGE
        preparedStatement.setString(25, "-1");// 23. Ram_IS_UP
        preparedStatement.setString(26, "-1");// 24. RAM_IS_DOWN
        preparedStatement.setString(27, signalMap.getProperty(OPCReader_SQL.RCL));// 25. RCL
        preparedStatement.setString(28, "-1");// 26. MATERIAL_DOOR
        preparedStatement.setString(29, signalMap.getProperty(OPCReader_SQL.OIL));// 27. Oil
        preparedStatement.setString(30, "-1");// 28. Polymers
        preparedStatement.setString(31, signalMap.getProperty(OPCReader_SQL.RCL));// 29. CARBON
        preparedStatement.setString(32, signalMap.getProperty(OPCReader_SQL.SMALL_CH));// 30. SMALL_CHEM
        preparedStatement.setString(33, signalMap.getProperty(OPCReader_SQL.TOTAL_ADD));// 31. TOTAL_ADDING
        preparedStatement.setString(34, "-1");// 32. PLC_CONTROL
        preparedStatement.setString(35, "-1");// 33. MOTOR
        preparedStatement.setString(36, "-1");// 34. Fillers
        preparedStatement.setString(37, "-1");// 35. SMALL_CHEM_AUTO
        preparedStatement.setString(38, MC_IN_CHARGE);// 36. MC_IN_CHARGE ------------> 
        preparedStatement.setString(39, "-1");// 37. BCS_RCL
        preparedStatement.setString(40, "-1");// 38. Manual_Mode
        preparedStatement.setString(41, "-1");// 39. MC_CONTROL
        preparedStatement.setString(42, "-1");// 40. RAM_UP
        preparedStatement.setString(43, "-1");// 41. RAM_DOWN
        preparedStatement.setString(44, "-1");// 42. ALARM
        preparedStatement.setString(45, "-1");// 43. INTERRUPT
        preparedStatement.setString(46, "-1");// 44. Set_OPEN_DOOR
        preparedStatement.setString(47, "-1");// 45. DOOR_CLOSE
        preparedStatement.setString(48, "-1");// 46. MC_RCL
        preparedStatement.setString(49, "-1");// 47. RECIPE_ID
        preparedStatement.setString(50, "-1");// 48. ORDER_ID
        preparedStatement.setString(51, "-1");// 49. BATCH_NR
        preparedStatement.setString(52, "-1");// 50. REVISION_NR
        //
        System.out.println("update: " + signalMap.getProperty("date"));
        //
        preparedStatement.execute();
        //
    }

    public static String get_proper_date_time_same_format_on_all_computers() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public void loadBatch(String order, String batch) {
        reader_SQL = new OPCReader_SQL(sql_B__src, "", order, batch);
    }

    public void loadBatchById(String batchId) {
        reader_SQL = new OPCReader_SQL(sql_B__src, batchId);
    }

    public void loadBatchById(String batchId, String recipe, String order, String batch,Object someObj) {
        reader_SQL = new OPCReader_SQL(sql_B__src, batchId);
        reader_SQL.RECIPE = recipe;
        reader_SQL.ORDER = order;
        reader_SQL.BATCH = batch;
    }

    public String getBatchId() {
        return OPCReader_SQL.BATCH_ID;
    }

    public void resetRecord() {
        reader_SQL.resetResultSet();
    }

    public int getSize() throws SQLException {
        return reader_SQL.getSize();
    }

    public Properties getSignalsOneSec() {
        return reader_SQL.aquire_signals_within_one_sec("");
    }

    public Object getSignal(String signal) {
        return getSignalsOneSec().getProperty(signal);
    }

    public void go() throws SQLException {
        //
        Properties signalsOneSec = getSignalsOneSec();
        //
        while (signalsOneSec != null) {
            //
            update(signalsOneSec);
            //
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OPCReader_SQL_Consumer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //
            signalsOneSec = getSignalsOneSec();
            //
        }
    }

    public static void main(String[] args) {
        //
        Sql_B sql_src = new Sql_B(false, false);
        Sql_B sql_dest = new Sql_B(false, false);
        //
        try {
            sql_src.connect("mysql", "10.87.0.145", "3306", "igt", "root", "0000");
            sql_dest.connect("mysql", "10.87.0.175", "3306", "signals", "root", "0000");
        } catch (ClassNotFoundException | SQLException | SqlTypeNotSpecifiedException ex) {
            Logger.getLogger(OPCReader_SQL_Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        OPCReader_SQL_Consumer consumer = new OPCReader_SQL_Consumer(sql_src, sql_dest);
        //
        consumer.loadBatchById("2358");
        //
        try {
            consumer.go();
        } catch (SQLException ex) {
            Logger.getLogger(OPCReader_SQL_Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
    }

}
