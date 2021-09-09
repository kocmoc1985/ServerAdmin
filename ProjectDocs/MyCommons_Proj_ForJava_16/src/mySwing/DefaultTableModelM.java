/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySwing;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import statics.HelpMy;

/**
 * To be able to sort as Integer/Float values
 * 
 * jTable.setModel(new DefaultTableModelM(Object[][] content, String[] headers, String[] sortAsInt,JTable jTable));
 * 
 * HelpA.build_table_common_with_rounding(...........)
 * 
 * @author KOCMOC
 */
public class DefaultTableModelM extends DefaultTableModel {

    private String[] sortAsFloatColNames;
    private JTable table;

    public DefaultTableModelM(Object[][] os, Object[] os1, String[] sortAsFloatColNames, JTable table) {
        super(os, os1);
        this.sortAsFloatColNames = sortAsFloatColNames;
        this.table = table;
    }
    
    @Override
    public Class<?> getColumnClass(int i) {

        for (String colName : sortAsFloatColNames) {
            if (HelpMy.getColByName(table, colName) == i) {
                return Integer.class;
            }
        }
        return super.getColumnClass(i);
    }
    
    //    @Override
//    public Class<?> getColumnClass(int i) {
//        if (i == 0) {
//            return Integer.class;
//        }
//        return super.getColumnClass(i);
//    }
    
    
//    @Override
//    public String getColumnName(int column) {
//        return getColumnClass(column).getSimpleName();
//    }
}
