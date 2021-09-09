/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import other.ShowMessage;

/**
 *
 * @author KOCMOC
 */
public interface SqlBasic {

    /**
     *
     * @param string
     * @return
     * @throws SQLException
     * @deprecated
     */
    public ResultSet execute(String string) throws SQLException;

    public ResultSet execute(String sql, ShowMessage sm) throws SQLException;

    public ResultSet execute_2(String sql) throws SQLException;

    public int update(String string) throws SQLException;

    public void prepareStatement(String q) throws SQLException;

    public int executeUpdatePreparedStatement() throws SQLException;

    public PreparedStatement getPreparedStatement();

    public PreparedStatement prepareStatementB(String q) throws SQLException;

    public Connection getConnection();
}
