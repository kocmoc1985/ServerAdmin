/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySwing;

import Exceptions.TableNameNotSpecifiedException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import statics.HelpMy;

/**
 *
 * @author KOCMOC
 */
public class JTableM extends JTable implements TableColumnModelListener, MouseListener, Runnable {

    private String TABLE_NAME;
    private boolean SAVE_COL_WIDTHS;
    private ArrayList COL_WIDTH_LIST_SAVE;
    private String COL_WIDTH_LIST_FILE_NAME;
    private boolean SAVE_ALLOWED = false;
    private ArrayList<JTable> SYNC_TABLES_LIST = new ArrayList<>();
    private boolean ONCE = false;
    private boolean TABLE_IS_BUILT = false;

    /**
     * The basic one
     *
     * If not working, check that you build the table not with HelpA...
     *
     * For separate JFrame windows, they do not refresh after restoring the
     * columns, so what you need to do is to setSize(getWidth()-1,getHeight())
     * after building the table
     *
     * @param tableName
     * @param saveColWidths
     */
    public JTableM(String tableName, boolean saveColWidths) {
        basic(tableName, saveColWidths);
        startThread();
    }

    public void addSyncTable(JTable table) {
        this.SYNC_TABLES_LIST.add(table);
    }
    
    private void startThread() {
        Thread x = new Thread(this);
        x.start();
    }
    
    @Override
    public void run() {
        //
        while (getHeight() <= 0) {
            System.out.println("table not built: " + TABLE_NAME);
            wait_(250);
        }
        //
        TABLE_IS_BUILT = true;
        System.out.println(TABLE_NAME + " IS BUILD");
        //
        columnMarginChanged(null); // OBS! OBS! Much better then using 
        //
    }

    private synchronized void wait_(int millis) {
        try {
            wait(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(JTableM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void basic(String tableName, boolean saveColWidths) {
        //
        this.TABLE_NAME = tableName;
        //
        this.SAVE_COL_WIDTHS = saveColWidths;
        //
        COL_WIDTH_LIST_FILE_NAME = "col_widths_save__" + TABLE_NAME;
        //
        try {
            checkTableName();
        } catch (TableNameNotSpecifiedException ex) {
            Logger.getLogger(JTableM.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
//        addMouseListener(this);
        getTableHeader().addMouseListener(this);
        //
    }

    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    /**
     * @deprecated 
     * Was very essential before, but now [2019-11-22] after 
     * implementing a special thread which monitors the "BUILD READY STATE"
     * it's not longer needed to call this method manually
     */
    public void setTableBuild() {
        TABLE_IS_BUILT = true;
    }

    //==========================================================================
    private void checkTableName() throws TableNameNotSpecifiedException {
        //
        if (TABLE_NAME == null || TABLE_NAME.isEmpty()) {
            throw new TableNameNotSpecifiedException();
        }
        //
    }

    private void restore() {
        COL_WIDTH_LIST_SAVE = restoreSaveListFromObject(COL_WIDTH_LIST_FILE_NAME);
        restoreColumnWidths(COL_WIDTH_LIST_SAVE);

    }

    @Override
    public void columnMarginChanged(ChangeEvent ce) {
        //
        super.columnMarginChanged(ce);
        //
        //
        if (SYNC_TABLES_LIST == null) {
            SYNC_TABLES_LIST = new ArrayList<JTable>();
        }
        //
        if (TABLE_IS_BUILT) {
            TABLE_IS_BUILT = false;
            restore();
        }
        //
        //
        //
        if (SYNC_TABLES_LIST.isEmpty() == false) {
            for (JTable table : SYNC_TABLES_LIST) {
                synchColumnWidths(table);
            }
        }
        //
        if (SAVE_COL_WIDTHS == false) {
            return;
        }
        //
        if (COL_WIDTH_LIST_SAVE == null) {
            COL_WIDTH_LIST_SAVE = new ArrayList();
        }
        //
        if(ce == null){
            return;
        }
        //
//        System.out.println("COL MARGIN CHANGED: " + TABLE_NAME);
        //
        DefaultTableColumnModel dtcm = (DefaultTableColumnModel) ce.getSource();
        JTable parentTable = null;
        //
        Object[] signers = dtcm.getColumnModelListeners();
        //
        for (Object object : signers) {
            if (object instanceof JTable && parentTable == null) {
                parentTable = (JTable) object;
            }
        }
        //
        //
        if (parentTable instanceof JTableM && SAVE_ALLOWED) {
            COL_WIDTH_LIST_SAVE = saveColumnWidths();
        }
        //
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        if (ONCE == false) {
            System.out.println("Event: " + me.getSource());
            SAVE_ALLOWED = true;
            ONCE = true;
        }
    }

    private ArrayList<Integer> saveColumnWidths() {
        //
        ArrayList<Integer> list = new ArrayList<Integer>();
        //
        for (int i = 0; i < getColumnCount(); i++) {
            list.add(getColumnWidthByIndex(i));
        }
        //
        if (COL_WIDTH_LIST_SAVE.isEmpty()) {
            return list;
        }
        //
        objectToFile(COL_WIDTH_LIST_FILE_NAME, list);
//        System.out.println("saved");
        return list;
        //
    }

    private ArrayList restoreSaveListFromObject(String fileName) {
        try {
            Object obj = fileToObject(fileName);
            ArrayList colWidthList = (ArrayList) obj;
            return colWidthList;
        } catch (Exception ex) {
            return new ArrayList();
        }
    }

    private Object fileToObject(String path) throws IOException, ClassNotFoundException {
        FileInputStream fas = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fas);
        Object obj = ois.readObject();
        return obj;
    }

    private void restoreColumnWidths(ArrayList<Integer> list) {
        //
        if (list.isEmpty()) {
            return;
        }
        //
        for (int i = 0; i < getColumnCount() && i < list.size(); i++) {
            setColumnWidthByIndex(i, list.get(i));
        }
    }

    private void objectToFile(String path, Object obj) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);

        } catch (Exception ex) {
            Logger.getLogger(JTableM.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //==========================================================================
    public void synchColumnWidths(JTable tableToSyncWith) {
        for (int i = 0; i < getColumnCount(); i++) {
            int srcWidth = getColumnWidthByIndex(i);
            tableToSyncWith.getColumnModel().getColumn(i).setPreferredWidth(srcWidth);
        }
    }

    //==========================================================================
    public synchronized void build_table_common(String[] headers, Object[][] content) {
        this.setModel(new DefaultTableModel(content, headers));
        //
//        TABLE_IS_BUILT = true;
        //

    }

    public synchronized void build_table_common(ResultSet rs, String q) {
        //
        if (rs == null) {
            return;
        }
        //
//        HelpA.setTrackingToolTip(this, q);
        //
        try {
            String[] headers = getHeaders(rs);
            Object[][] content = getContent(rs);
            this.setModel(new DefaultTableModel(content, headers));

        } catch (SQLException ex) {
            Logger.getLogger(JTableM.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
//        TABLE_IS_BUILT = true;
        //

    }

    public synchronized void build_table_common(ResultSet rs, String q, int indexFirst, int indexLast) {
        //
        if (rs == null) {
            return;
        }
        //
//        HelpA.setTrackingToolTip(this, q);
        //
        try {
            String[] headers = getHeaders(rs);
            Object[][] content = getContent(rs);
            this.setModel(new DefaultTableModel(content, headers));

        } catch (SQLException ex) {
            Logger.getLogger(JTableM.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
//        TABLE_IS_BUILT = true;
        //

    }

    public synchronized void build_table_common_with_rounding(ResultSet rs, String q, String roundingFormat, String[] skipColumnsNames, String[] exceptionColumns, String[] sortAsInt) {
        //
        if (rs == null) {
            return;
        }
        //
//        HelpMy.setTrackingToolTip(jTable, q);
        //
        try {
            String[] headers = getHeaders(rs);
            Object[][] content = getContentRounding(rs, roundingFormat, headers, skipColumnsNames, exceptionColumns, sortAsInt);
            setModel(new DefaultTableModelM(content, headers, sortAsInt, this));
            setAutoCreateRowSorter(true);
        } catch (SQLException ex) {
            Logger.getLogger(HelpMy.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
//        TABLE_IS_BUILT = true;
        //
    }

    public synchronized void build_table_common_with_rounding_properties(ResultSet rs, String q, Properties props, String defaultFormat, String[] skipColumnsNames, String[] sortAsInt) {
        //
        if (rs == null) {
            return;
        }
        //
//        HelpA.setTrackingToolTip(jTable, q);
        //
        try {
            //
            String[] headers = getHeaders(rs);
            //
            Object[][] content = getContentRounding_properties(rs, props, defaultFormat, headers, skipColumnsNames, sortAsInt);
            //
            setModel(new DefaultTableModelM(content, headers, sortAsInt, this));
            setAutoCreateRowSorter(true);
            //
        } catch (SQLException ex) {
            Logger.getLogger(JTableM.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
//        TABLE_IS_BUILT = true;
        //
    }

    public synchronized String[] getHeaders(ResultSet rs) throws SQLException {
        ResultSetMetaData meta; // Returns the number of columns
        String[] headers; // skapar en ny array att lagra titlar i
        meta = rs.getMetaData(); // Den parameter som skickas in "ResultSet rs" innehåller Sträng vid initialisering
        headers = new String[meta.getColumnCount()]; // ger arrayen "headers" initialisering och anger antalet positioner
        for (int i = 0; i < headers.length; i++) {
            headers[i] = meta.getColumnLabel(i + 1);
        }
        //
        return headers;
    }

    public synchronized Object[][] getContent(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmt;
        Object[][] content;
        int rows, columns;
        rsmt = rs.getMetaData(); // får in antalet columner
        rs.last(); // flyttar pekaren till sista positon
        rows = rs.getRow(); // retrieves the current antalet rows och lagrar det i variabeln "rows"
        columns = rsmt.getColumnCount(); // retrieves number of columns och lagrar det i "columns".
        content = new Object[rows][columns]; // ger arrayen content som är en "Object"
        // initialisering i den första demensionen är "rows" i den andra "columns"
        //
        for (int row = 0; row < rows; row++) {
            rs.absolute(row + 1); // Flytta till rätt rad i resultatmängden
            for (int col = 0; col < columns; col++) {
                Object obj = rs.getString(col + 1);
                content[row][col] = obj;
            }
        }
        //
        return content;
    }

    public synchronized Object[][] getContent(ResultSet rs, int indexFirst, int indexLast) throws SQLException {
        ResultSetMetaData rsmt;
        Object[][] content;
        int rows, columns;
        rsmt = rs.getMetaData(); // får in antalet columner
        rs.last(); // flyttar pekaren till sista positon
        columns = rsmt.getColumnCount(); // retrieves number of columns och lagrar det i "columns".
        rows = (indexLast - indexFirst) + 1;
        content = new Object[rows][columns]; // ger arrayen content som är en "Object"
        // initialisering i den första demensionen är "rows" i den andra "columns"
        //
        int row_ = 0;
        for (int row = indexFirst; row <= indexLast; row++) {
            rs.absolute(row); // Flytta till rätt rad i resultatmängden
            for (int col = 0; col < columns; col++) {
//                System.out.println("Col: " + (col+1));
                Object obj = rs.getString(col + 1);
                content[row_][col] = obj;
            }
            row_++;
        }
        //
        return content;
    }

    private synchronized Object[][] getContentRounding(ResultSet rs, String format, String[] headers, String[] skipColumnsNames, String[] exceptionColumns, String[] sortAsInt) throws SQLException {
        ResultSetMetaData rsmt;
        Object[][] content;
        int rows, columns;
        rsmt = rs.getMetaData(); // får in antalet columner
        rs.last(); // flyttar pekaren till sista positon
        rows = rs.getRow(); // retrieves the current antalet rows och lagrar det i variabeln "rows"
        columns = rsmt.getColumnCount(); // retrieves number of columns och lagrar det i "columns".
        content = new Object[rows][columns]; // ger arrayen content som är en "Object"
        //
        // initialisering i den första demensionen är "rows" i den andra "columns"
        //
        for (int row = 0; row < rows; row++) {
            rs.absolute(row + 1); // Flytta till rätt rad i resultatmängden
            for (int col = 0; col < columns; col++) {
                //
                Object obj = rs.getString(col + 1);
                //
                String colName = headers[col];
                //
                if (exceptionColumn(col, headers, exceptionColumns)) {
                    content[row][col] = roundDouble(obj, "%2.3f");
                } else if (skipRounding(col, headers, skipColumnsNames) == false) {
                    content[row][col] = roundDouble(obj, format);//-----------------------OBS ROUNDING IS DONE HERE
                } else if (sortAsInteger(col, headers, sortAsInt)) {
                    content[row][col] = Integer.parseInt(obj.toString());
                } else {
                    content[row][col] = obj;
                }
                //
            }
        }
        //
        return content;
    }

    public synchronized Object[][] getContentRounding_properties(ResultSet rs, Properties pFomats, String defaultFormat, String[] headers, String[] skipColumnsNames, String[] sortAsInt) throws SQLException {
        ResultSetMetaData rsmt;
        Object[][] content;
        int rows, columns;
        rsmt = rs.getMetaData(); // får in antalet columner
        rs.last(); // flyttar pekaren till sista positon
        rows = rs.getRow(); // retrieves the current antalet rows och lagrar det i variabeln "rows"
        columns = rsmt.getColumnCount(); // retrieves number of columns och lagrar det i "columns".
        content = new Object[rows][columns]; // ger arrayen content som är en "Object"
        //
        // initialisering i den första demensionen är "rows" i den andra "columns"
        //
        for (int row = 0; row < rows; row++) {
            rs.absolute(row + 1); // Flytta till rätt rad i resultatmängden
            for (int col = 0; col < columns; col++) {
                //
                Object obj = rs.getString(col + 1);
                //
                if (skipRounding(col, headers, skipColumnsNames) == false) {
                    //
                    String roundFormat = defineRoundingByColName(col, headers, defaultFormat, pFomats);
                    //
                    content[row][col] = roundDouble(obj, roundFormat);//-----------------------OBS ROUNDING IS DONE HERE
                    //
                } else if (sortAsInteger(col, headers, sortAsInt)) {
                    content[row][col] = Integer.parseInt(obj.toString());
                } else {
                    content[row][col] = obj;
                }
                //
            }
        }
        //
        return content;
    }

    private String defineRoundingByColName(int col, String[] headers, String defaultFormat, Properties p) {
        //
        String colName = headers[col];
        String format = "%2.";
        String x = p.getProperty(colName, defaultFormat);
        //
        try {
            Integer.parseInt(x);
            return format += x + "f";
        } catch (Exception ex) {
            return x;
        }
        //
    }

    private boolean exceptionColumn(int colNr, String[] headers, String[] exceptionColumns) {
        for (String colName : exceptionColumns) {
            if (headers[colNr].equals(colName)) {
                return true;
            }
        }
        return false;
    }

    private synchronized Object roundDouble(Object obj, String format) {
        if (isDouble(obj)) {
            String val = (String) obj;
            double ret = Double.parseDouble(val);
//            return "" + Double.parseDouble(roundDouble(ret, format));
            return "" + roundDouble(ret, format);
        } else {
            return obj;
        }
    }

    private synchronized String roundDouble(double number, String format) {
        return String.format(format, number).replace(",", ".");
    }

    private boolean sortAsInteger(int colNr, String[] headers, String[] colNames) {
        for (String colName : colNames) {
            if (headers[colNr].equals(colName)) {
                return true;
            }
        }
        return false;
    }

    private synchronized boolean isDouble(Object obj) {
        if (obj instanceof String) {
            String val = (String) obj;
            //
            //
            try {
                Double.parseDouble(val);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }

    private boolean skipRounding(int colNr, String[] headers, String[] skipColumnsNames) {
        for (String colName : skipColumnsNames) {
            if (headers[colNr].equals(colName)) {
                return true;
            }
        }
        return false;
    }

    public String jTableToHTML(String[] jtableColsToInclude) {
        //
        ArrayList<String> colNames;
        //
        if (jtableColsToInclude != null) {
            colNames = getVisibleColumnsNames_B(jtableColsToInclude);
        } else {
            colNames = getVisibleColumnsNames();
        }
        //
        //
        String html = "";
        //
        //
        html += "<table class='jtable'>";
        //
        //<TABLE HEADER>
        html += "<tr>";
        //
        for (int i = 0; i < colNames.size(); i++) {
            html += "<th>" + colNames.get(i) + "</th>";
        }
        //
        html += "</tr>";
        //</TABLE HEADER>
        //
        //<TABLE BODY>
        for (int x = 0; x < getRowCount(); x++) {
            //
            ArrayList rowValues;
            //
            if (jtableColsToInclude != null) {
                rowValues = getLineValuesVisibleColsOnly_B(x, jtableColsToInclude);
            } else {
                rowValues = getLineValuesVisibleColsOnly(x);
            }
            //
            //
            html += "<tr>";
            //
            for (int i = 0; i < rowValues.size(); i++) {
                html += "<td>" + rowValues.get(i) + "</td>";
            }
            //
            html += "</tr>";
            //
        }
        //</TABLE BODY>
        //
        html += "</table>";
        //
        //
        return html;
    }

    public String jTableToCSV(boolean writeToFile) {
        //
        String csv = "";
        //
        for (Object colName : getVisibleColumnsNames()) {
            csv += colName + ";";
        }
        //
        csv += "\n";
        //
        //
        for (int x = 0; x < this.getRowCount(); x++) {
            for (Object rowValue : getLineValuesVisibleColsOnly(x)) {
                csv += rowValue + ";";
            }
            csv += "\n";
        }
        //
        String path = get_desktop_path() + "\\" + getDate() + ".csv";
        //
        if (writeToFile) {
            try {
                writeToFile(path, csv);
//                JOptionPane.showMessageDialog(null, "Export file ready, the file is in: " + path);
                run_application_with_associated_application(new File(path));

            } catch (IOException ex) {
                Logger.getLogger(JTableM.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        //
        return csv;
    }

    public String jTableToCSV(boolean writeToFile, String[] columnsToInclude) {
        //
        String csv = "";
        //
        for (Object colName : getVisibleColumnsNames_B(columnsToInclude)) {
            csv += colName + ";";
        }
        //
        csv += "\n";
        //
        //
        for (int x = 0; x < getRowCount(); x++) {
            for (Object rowValue : getLineValuesVisibleColsOnly_B(x, columnsToInclude)) {
                csv += rowValue + ";";
            }
            csv += "\n";
        }
        //
        String path = get_desktop_path() + "\\" + getDate() + ".csv";
        //
        if (writeToFile) {
            try {
                writeToFile(path, csv);
//                JOptionPane.showMessageDialog(null, "Export file ready, the file is in: " + path);
                run_application_with_associated_application(new File(path));

            } catch (IOException ex) {
                Logger.getLogger(JTableM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //
        return csv;
    }

    private String get_desktop_path() {
        return System.getProperty("user.home") + "\\" + "Desktop";
    }

    private String getDate() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH_mm");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    private void writeToFile(String fileName, String textToWrite) throws IOException {
        FileWriter fstream = new FileWriter(fileName, false);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(textToWrite);
        out.newLine();
        out.flush();
        out.close();
    }

    private static void run_application_with_associated_application(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    private boolean exists_(String col, String[] columns) {
        for (String colName : columns) {
            if (colName.equals(col)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param table
     * @param colName
     * @param rightOrLeft 2=left, 4=right
     */
    public void alignValueByColName(String colName, int rightOrLeft) {
        //
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(rightOrLeft);
        //
        int colNr = getColByName(colName);
        //
        if (colNr != -1) {
            getColumnModel().getColumn(colNr).setCellRenderer(renderer);
        }
    }

    public void rightAlignValues() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        //
        for (int x = 0; x < getColumnCount(); x++) {
            getColumnModel().getColumn(x).setCellRenderer(rightRenderer);
        }
        //
    }

    public int getColumnWidthByIndex(int colIndex) {
        return getColumnModel().getColumn(colIndex).getWidth();
    }

    public int getColumnWidthByIndex(JTable table, int colIndex) {
        return table.getColumnModel().getColumn(colIndex).getWidth();
    }

    /**
     *
     * @param colIndex - starts from 0
     * @param table
     * @param width
     */
    public void setColumnWidthByIndex(int colIndex, int width) {
        getColumnModel().getColumn(colIndex).setPreferredWidth(width);
    }

    /**
     *
     * @param colIndex - starts from 0
     * @param table
     * @param width
     */
    public void setColumnWidthByIndexP(int colIndex, int width) {
        getColumnModel().getColumn(colIndex).setPreferredWidth(width);
    }

    /**
     * OBS! Some times setWidth() works, some times setPreferredWidth()
     *
     * @param table
     * @param colIndex
     * @param width
     */
    public void setColumnWidthByIndex(JTable table, int colIndex, int width) {
        table.getColumnModel().getColumn(colIndex).setPreferredWidth(width);
    }

    public void disableColumnDragging() {
        getTableHeader().setReorderingAllowed(false);
    }

    public ArrayList getLineValuesVisibleColsOnly_B(int rowNr, String[] columnsToInclude) {
        ArrayList rowValues = new ArrayList();
        for (int x = 0; x < getColumnCount(); x++) {
            if (columnIsVisible(x)) {
                String value = "" + getValueAt(rowNr, x);
                if (exists_(getColumnNameByIndex(x), columnsToInclude)) {
                    rowValues.add(value);
                }
            }
        }

        return rowValues;
    }

    /**
     * OBS! JTable row index start with 0
     *
     * @param table
     * @param rowNr
     * @return
     */
    public ArrayList getLineValuesVisibleColsOnly(int rowNr) {
        ArrayList rowValues = new ArrayList();
        for (int x = 0; x < getColumnCount(); x++) {
            if (columnIsVisible(x)) {
                String value = "" + getValueAt(rowNr, x);
                rowValues.add(value);
            }
        }
        return rowValues;
    }

    public ArrayList getVisibleColumnsNames_B(String[] columnsToInclude) {
        ArrayList columnNames = new ArrayList();
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnIsVisible(i) && exists_(getColumnNameByIndex(i), columnsToInclude)) {
                columnNames.add(getColumnNameByIndex(i));
            }
        }

//        ArrayList visibleColumnsIndexes = getVisibleColumnsIndexes(table);
//
//        for (Object index : visibleColumnsIndexes) {
//            Integer ind = (Integer) index;
//            if (exists_(ind, columnsToInclude)) {
//                columnNames.add(getColumnNameByIndex(table, ind));
//            }
//        }
        return columnNames;
    }

    public ArrayList getVisibleColumnsNames() {
        ArrayList columnNames = new ArrayList();
        for (int i = 0; i < this.getColumnCount(); i++) {
            if (columnIsVisible(i)) {
                columnNames.add(getColumnNameByIndex(i));
            }
        }
        return columnNames;
    }

    public ArrayList getVisibleColumnsIndexes() {
        ArrayList indexes = new ArrayList();
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnIsVisible(i)) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    public int getVisibleColumnsCount(JTable table) {
        int count = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (columnIsVisible(i)) {
                count++;
            }
        }
        return count++;
    }

    public boolean columnIsVisible(int column) {
        int width = getColumnModel().getColumn(column).getWidth();
        return width == 0 ? false : true;
    }

    public String getColumnNameByIndex(int column) {
        JTableHeader th = getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(column);
        return (String) tc.getHeaderValue();
    }

    /**
     * OBS! Even if you change the header title of the column the "Real Name"
     * will be the same!!!!!
     *
     * @param table
     * @param column
     * @param newTitle
     */
    public static void changeTableHeaderTitleOfOneColumn(JTable table, int column, String newTitle) {
        JTableHeader th = table.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(column);
        tc.setHeaderValue(newTitle);
        th.repaint();
    }

    /**
     * OBS! Even if you change the header title of the column the "Real Name"
     * will be the same!!!!!
     *
     * @param table
     * @param oldName
     * @param newTitle
     */
    public void changeTableHeaderTitleOfOneColumn(String oldName, String newTitle) {
        JTableHeader th = getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(getColByName(oldName));
        tc.setHeaderValue(newTitle);
        th.repaint();
    }

    //
    public void paintTableHeaderBorderOneColumn(int column, final Color borederColor) {
        JTableHeader th = getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(column);
        //
        //
        TableCellRenderer renderer = new TableCellRenderer() {
            JLabel label = new JLabel();

            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                label.setOpaque(true);
                label.setText("" + value);
                label.setBorder(BorderFactory.createLineBorder(borederColor));
                return label;
            }
        };
        //
        tc.setHeaderRenderer(renderer);
        th.repaint();
    }

    public void resetTableHeaderPainting(int column) {
        //
        if (column == -1) {
            return;
        }
        //
        JTableHeader th = getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(column);
        //
        tc.setHeaderRenderer(null);
        th.repaint();
    }

    public static void clearAllRowsJTable(JTable table) {
        DefaultTableModel dm = (DefaultTableModel) table.getModel();
        //
        int rowCount = dm.getRowCount();
        //
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
    }

    public void removeRowJTable(int rowToRemove) {
        DefaultTableModel dm = (DefaultTableModel) getModel();
        dm.removeRow(rowToRemove);
    }

    public void addRowJTable() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(new Object[]{});
    }

    public int getRowByValue(String col_name, String row_value) {
        for (int i = 0; i < getColumnCount(); ++i) {
            if (getColumnName(i).equals(col_name)) {
                for (int y = 0; y < getRowCount(); ++y) {
                    String curr_row_value = "" + getValueAt(y, i);
                    //
                    if (curr_row_value == null) {
                        continue;
                    }
                    //
                    if (curr_row_value.equals(row_value)) {
                        return y;
                    }
                }
            }
        }
        return -1;
    }

    public void setValueGivenRow(int row, String colName, Object value) {
        setValueAt(value, row, getColByName(colName));
    }

    public boolean getIfAnyRowChosen() {
        if (getSelectedRow() == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getValueGivenRow(int row, String colName) {
        return "" + getValueAt(row, getColByName(colName));
    }

    public String getValueSelectedRow(String colName) {
        int selected_row = getSelectedRow();
        //
        try {
            return "" + getValueAt(selected_row, getColByName(colName));
        } catch (Exception ex) {
            return null;
        }
    }

    public int getColByName(String name) {
        for (int i = 0; i < getColumnCount(); ++i) {
            if (getColumnName(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public boolean hideColumnByName(String name) {
        for (int i = 0; i < getColumnCount(); ++i) {
            if (getColumnName(i).equals(name)) {
                getColumnModel().getColumn(i).setMinWidth(0);
                getColumnModel().getColumn(i).setMaxWidth(0);
                getColumnModel().getColumn(i).setWidth(0);
                return true;
            }
        }
        return false;
    }

    public int moveRowToEnd(int currRow) {
        DefaultTableModel dtm = (DefaultTableModel) getModel();
        dtm.moveRow(currRow, currRow, getRowCount() - 1);
        return getRowCount() - 1;
    }

    public void moveRowTo(int rowToMove, int rowToMoveTo) {
        DefaultTableModel dtm = (DefaultTableModel) getModel();
        dtm.moveRow(rowToMove, rowToMove, rowToMoveTo);
    }

    public void moveRowTo(String colName, String rowValue, int rowToMoveTo) {
        DefaultTableModel dtm = (DefaultTableModel) getModel();
        int rowToMove = getRowByValue(colName, rowValue);
        dtm.moveRow(rowToMove, rowToMove, rowToMoveTo);
    }

    public void selectNextRow() {
        try {
            setRowSelectionInterval(getSelectedRow() + 1, getSelectedRow() + 1);
        } catch (Exception ex) {
        }
    }

    public void selectPrevRow() {
        try {
            setRowSelectionInterval(getSelectedRow() - 1, getSelectedRow() - 1);
        } catch (Exception ex) {
        }
    }

    public void setSelectedRow(int rowNr) {
        setRowSelectionInterval(rowNr, rowNr);
    }

    public void markFirstRowJtable() {
        markGivenRow(0);
    }

    public void markLastRowJtable(JTable table) {
        markGivenRow(getRowCount() - 1);
    }

    public void markGivenRow(int row) {
        try {
            changeSelection(row, 0, false, false);
        } catch (Exception ex) {
        }
    }

    public int getNextRow(int previousRow) {
        int nextRow = previousRow++;
        if (nextRow < getRowCount()) {
            return nextRow;
        } else {
            return 0;
        }
    }

    public boolean isEmtyJTable() {
        if (getRowCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void stopEditJTable() {
        editCellAt(0, 0);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}
