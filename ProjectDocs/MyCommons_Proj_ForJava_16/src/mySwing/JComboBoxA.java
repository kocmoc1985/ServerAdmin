/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySwing;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import other.CBoxParam;
import statics.HelpMy;
import sql.SqlBasic;

/**
 * This instance of ComboBox is especially done for use with auto fill library
 * "glazedlists".
 *
 * It implements also fields such as "PARAMETER" and "IS_NUMBER" to work with
 * Databases
 *
 * @author KOCMOC
 */
public class JComboBoxA extends JComboBox {

    private long FLAG_WAIT;
    private String PARAMETER; // Column name in DB
    private String PARAMETER_2; // For MultiParams -> fillComboBoxMultiple(.....)
    private boolean NUMBER;
    //
    //glazedlists AutoComplete
    private final EventList<Object> LIST = new BasicEventList<Object>();
    private AutoCompleteSupport support;
    private boolean TRACKING_TOOL_TIP = false;

    public JComboBoxA() {
    }

    public JComboBoxA(boolean trackinToolTip) {
        this.TRACKING_TOOL_TIP = trackinToolTip;
    }

    /**
     * For multiparams
     *
     * @param param
     */
    public JComboBoxA(CBoxParam param) {
        if (param.isMultipleParam()) {
            this.PARAMETER = param.getParam();
            this.PARAMETER_2 = param.getParam2();
            this.NUMBER = param.isNumber();
        } else {
            this.PARAMETER = param.getParam();
            this.NUMBER = param.isNumber();
        }
    }

    /**
     * @deprecated
     */
    public JComboBoxA(String PARAMETER, String PARAMETER_2, boolean isNumber) {
        this.PARAMETER = PARAMETER;
        this.NUMBER = isNumber;
        this.PARAMETER_2 = PARAMETER_2;
    }

    public JComboBoxA(String PARAMETER, boolean isNumber) {
        this.PARAMETER = PARAMETER;
        this.NUMBER = isNumber;
    }

    public void clearContent() {
        LIST.clear();
        LIST.addAll(new BasicEventList<>());
    }

    public void AUTOFILL_ADD(List list) {
        if (support == null || support.isInstalled() == false) {
            LIST.addAll(list);
            support = AutoCompleteSupport.install(this, this.LIST);
        } else {
            LIST.clear();
            LIST.addAll(list);
            setEditable(true);
        }
    }

    public boolean isNUMBER() {
        return NUMBER;
    }

    public boolean isMULTI_PARAM() {
        if (PARAMETER != null && PARAMETER_2 != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getPARAMETER_2() {
        return PARAMETER_2;
    }

    public String getPARAMETER() {
        return PARAMETER;
    }

    public void setPARAMETER(String PARAMETER) {
        this.PARAMETER = PARAMETER;
    }

    public long getFLAG_WAIT() {
        return FLAG_WAIT;
    }

    public void setFLAG_WAIT(long FLAG_WAIT) {
        this.FLAG_WAIT = FLAG_WAIT;
    }
    //==========================================================================
    private static final HashMap<String, String> fakeValuesMap = new HashMap<String, String>();
    private Border initialComboBoxBorder;

    static {
        //STATUS -> RECIPE_DETAILED -> TABLE_INVERT
        fakeValuesMap.put("S", "ACTIVE");
        fakeValuesMap.put("I", "UNLOCKED");
        fakeValuesMap.put("O", "OLD");
        //CLASS -> RECIPE_DETAILED -> TABLE_INVERT
        fakeValuesMap.put("P", "PRODUCTION");
        fakeValuesMap.put("C", "CALCULATION");
        fakeValuesMap.put("R", "DEVELOPMENT");
    }

    public JComboBox fillComboBox(SqlBasic sql, JComboBox jbox, String query,
            Object initialValue, boolean showMultipleValues, boolean fakeValue) {
        //
        ArrayList<Object> list = new ArrayList<Object>();
        //
        boolean cond_1 = initialValue != null && (initialValue instanceof Boolean == false)
                && showMultipleValues == false && fakeValue == false;
        //
        if (cond_1) {
            list.add(initialValue);
        }
        //
        if (fakeValue) {
            list.add(" ");
        }
        //
        try {
            //
            ResultSet rs = sql.execute(query);
            //
            while (rs.next()) {
                //
                String val;
                //
                try {
                    val = rs.getString(1);
                } catch (Exception ex) {
                    break;
                }
                //
                if (val != null && val.isEmpty() == false) {
                    if (showMultipleValues) {
                        //
                        list.add(new ComboBoxObjectB(HelpMy.getValueResultSet(rs, 1), HelpMy.getValueResultSet(rs, 2), HelpMy.getValueResultSet(rs, 3)));
                        //
                    } else if (fakeValue) {
                        //
                        String value = HelpMy.getValueResultSet(rs, 1);
                        String fakeVal = fakeValuesMap.get(value);
                        if (fakeVal != null) {
                            list.add(new ComboBoxObjectC(value, fakeVal, ""));
                        }
                        //
                    } else {
                        //
                        list.add(new ComboBoxObject(HelpMy.getValueResultSet(rs, 1), HelpMy.getValueResultSet(rs, 2), HelpMy.getValueResultSet(rs, 3)));
                        //
                    }
                }
            }
            //
        } catch (Exception ex) {
            Logger.getLogger(JComboBoxA.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        if (jbox instanceof JComboBoxA) {
            JComboBoxA boxA = (JComboBoxA) jbox;
            boxA.AUTOFILL_ADD(list);
            //
            try {
                jbox.setSelectedIndex(0);
            } catch (Exception ex) {
            }
            //
        }
        //
        //
        if (initialComboBoxBorder == null) {
            initialComboBoxBorder = jbox.getBorder();
        }
        //
        HelpMy.tryMatch(jbox, (String) initialValue, showMultipleValues, fakeValue);
        //
        if (TRACKING_TOOL_TIP) {
            HelpMy.setTrackingToolTip(jbox, query);
        }
        //
        return jbox;
    }

    public String getComboBoxSelectedValue() {
        //
        Object val = getSelectedItem();
        //
        if (val == null) {
            return "NULL";
        }
        //
        if (val instanceof String) {
            String v = (String) val;
            if (v.isEmpty()) {
                return "NULL";
            } else {
                return v.toString();
            }
        }
        //
        //
        if (val instanceof ComboBoxObject) {
            ComboBoxObject cbo = (ComboBoxObject) val;
            return cbo.getParam_1(); // The "'" shall be remowed in feature!!!!!
        }
        //
        return null;
    }

    class ComboBoxObject {

        String param_1;
        String param_2;
        String param_3;

        public ComboBoxObject(String param_1, String param_2, String param_3) {
            this.param_1 = param_1;
            this.param_2 = param_2;
            this.param_3 = param_3;
        }

        @Override
        public String toString() {
            return param_1;
        }

        public String getParam_1() {
            return param_1;
        }

        public String getParam_2() {
            return param_2;
        }

        public String getParam_3() {
            return param_3;
        }
    }

    class ComboBoxObjectB extends ComboBoxObject {

        public ComboBoxObjectB(String param_1, String param_2, String param_3) {
            super(param_1, param_2, param_3);
        }

        @Override
        public String toString() {
            return param_1 + "   " + param_2;
        }
    }

    class ComboBoxObjectC extends ComboBoxObject {

        public ComboBoxObjectC(String param_1, String param_2, String param_3) {
            super(param_1, param_2, param_3);
        }

        @Override
        public String toString() {
            return param_2;
        }
    }
}
