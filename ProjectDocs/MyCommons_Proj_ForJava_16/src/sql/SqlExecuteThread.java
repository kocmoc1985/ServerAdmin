/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * With help of this class you can speed up the executing of queries with about 100 times.
 * For each execute a separate thread is created. 
 * Very important to mention that NO NEW CONNECTION is created but instead 
 * a new statement is created to perform a particular "execute"
 * @author KOCMOC
 */
public class SqlExecuteThread implements Runnable {

    private final String tableName;
    private final String q;
    private Statement statement;
    private final Sql_B sql_execute;
    private static final boolean MDB_J_8_UCANACCESS_DRIVER_USED = false;

    public SqlExecuteThread(String tableName, String q, Sql_B sql_execute) {
        //
        this.tableName = tableName;
        this.q = q;
        this.sql_execute = sql_execute;
        //
        try {
            this.statement
                    = sql_execute.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException ex) {
            Logger.getLogger(SqlExecuteThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            // The "ucanaccess" mdb driver is working, but as it has to be 
            // synchronized, the speed is like in sync mode
            if (MDB_J_8_UCANACCESS_DRIVER_USED) {
                synchronized (sql_execute) {
                    statement.execute(q);
                }
            } else {
                // Not really sure that this "synchronized" block makes some sence
                synchronized (statement) {
                    statement.execute(q);
                }
            }
            //
            System.out.println("Execute ok_______________________: " + this.tableName);
            //
        } catch (Exception ex) {
            System.out.println("EXECUTE_ERROR_________________: " + q);
            Logger.getLogger(SqlExecuteThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger(SqlExecuteThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
