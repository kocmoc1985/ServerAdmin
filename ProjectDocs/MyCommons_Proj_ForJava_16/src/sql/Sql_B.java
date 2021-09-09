/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

//import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import other.ShowMessage;
import other.SimpleLoggerLight;

/**
 *
 * @author Administrator
 */
public class Sql_B implements SqlBasic
    {

    private Connection connection;
    private Statement statement;
    private Statement statement_2;
    private PreparedStatement p_statement;
    //
    private final boolean CREATE_STATEMENT_SIMPLE;
    private int SQL_LOGIN_TIME_OUT = 60;
    //
    private final boolean LOGG_CONNECTION_STRING;
    //
    public boolean ODBC_OR_MDB;
    //
    static public final String SQL_TYPE_MSSQL = "mssql";
    static public final String SQL_TYPE_TDS = "tds";
    static public final String SQL_TYPE_ORACLE = "oracle";
    static public final String SQL_TYPE_MYSQL = "mysql";
    static public final String SQL_TYPE_SQLLITE = "sqllite";
    static public final String SQL_TYPE_ODBC = "odbc";
    static public final String SQL_TYPE_MDB = "mdb";
    static public final String SQL_TYPE_DBF = "dbf";

    static public final String SQL_TYPE_MDB_OLD = "mdb_old";

    public Sql_B(boolean statementSimple, boolean loggConnectionStr)
        {
        this.CREATE_STATEMENT_SIMPLE = statementSimple;
        this.LOGG_CONNECTION_STRING = loggConnectionStr;
        }

    public Sql_B(boolean statementSimple, int loginTimeOut, boolean loggConnectionStr)
        {
        this.CREATE_STATEMENT_SIMPLE = statementSimple;
        this.SQL_LOGIN_TIME_OUT = loginTimeOut;
        this.LOGG_CONNECTION_STRING = loggConnectionStr;
        }

    @Override
    public Connection getConnection()
        {
        return this.connection;
        }

    public Statement getStatement()
        {
        return statement;
        }
    
    public Statement getStatement_2()
        {
        return statement_2;
        }
    

    public boolean isConnected()
        {
        //
        if (ODBC_OR_MDB)
            {
            if (statement != null)
                {
                return true;
                }
            else
                {
                return false;
                }
            }
        //
        boolean closed;
        try
            {
            closed = statement.isClosed();
            }
        catch (SQLException ex)
            {
            Logger.getLogger(Sql_B.class.getName()).log(Level.SEVERE, null, ex);
            return false;
            }
        //
        if (closed)
            {
            return false;
            }
        else
            {
            return true;
            }
        }

    public static boolean UCANACCESS_LOAD_TO_MEMORY_OPTION = true;

    public void connect(String sqlType, String host, String port, String dbNameOrPathToMdbFile, String userName, String password) throws ClassNotFoundException, SQLException, SqlTypeNotSpecifiedException
        {
        //
        if (sqlType == null)
            {
            throw new SqlTypeNotSpecifiedException("SQL type is null: " + sqlType);
            }
        if (dbNameOrPathToMdbFile == null)
            {
            throw new SQLException("dbNameOrPathToMdbFile is null: " + dbNameOrPathToMdbFile);
            }
        //
        if (sqlType.equals(SQL_TYPE_MSSQL))
            {
            connect_jdbc(host, port, dbNameOrPathToMdbFile, userName, password);
            }
        else if (sqlType.equals(SQL_TYPE_TDS))
            {
            connect_tds(host, port, dbNameOrPathToMdbFile, userName, password, false, "", "");
            }
        else if (sqlType.equals(SQL_TYPE_ORACLE))
            {
            connect_oracle(host, port, dbNameOrPathToMdbFile, userName, password);
            }
        else if (sqlType.equals(SQL_TYPE_MYSQL))
            {
            connect_mysql(host, port, dbNameOrPathToMdbFile, userName, password);
            }
        else if (sqlType.equals(SQL_TYPE_SQLLITE))
            {
            connect_sqllite(dbNameOrPathToMdbFile);
            }
        else if (sqlType.equals(SQL_TYPE_MDB))
            {
            connect_mdb_java_8(userName, password, dbNameOrPathToMdbFile);
            }
        else if (sqlType.equals(SQL_TYPE_ODBC))
            {
            connect_odbc(userName, password, dbNameOrPathToMdbFile);
            }
        else if (sqlType.equals(SQL_TYPE_MDB_OLD))
            {
            connect_mdb(userName, password, dbNameOrPathToMdbFile);
        } else if (sqlType.equals(SQL_TYPE_DBF)) {
            connect_dbf(userName, password, dbNameOrPathToMdbFile);
        } else {
            throw new SqlTypeNotSpecifiedException("SQL type not specified: " + sqlType);
            }
        }

    private void connect_oracle(String host, String port, String databaseName, String userName, String password) throws SQLException, ClassNotFoundException
        {
        //
        //Name of .jar = ojdbc6.jar
        //
        Class.forName("oracle.jdbc.OracleDriver");
        //OR
//        DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); 
        //
//        String exampleUrl = "jdbc:oracle:thin:user/pass@dbHost:1521/dbName";
        //
        //Default port = 1521
        String url = "jdbc:oracle:thin:" + userName + "/" + password + "@" + host + ":" + port + "/" + databaseName;
        //
        logg_connection_string(url);
        //
        connection = DriverManager.getConnection(url);
        //
        if (CREATE_STATEMENT_SIMPLE == true)
            {
            statement = connection.createStatement();
            statement_2 = connection.createStatement();
            }
        else
            {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement_2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
        //
        if (statement == null)
            {
            SimpleLoggerLight.logg("sql_conn.log", "Connection to: " + host + " / dbname: " + databaseName + " failed");
            }
        }

    /**
     *
     * @param host
     * @param port
     * @param databaseName
     * @param userName
     * @param password
     * @param useNamedPipes
     * @param domain - is used only if named pipe are used
     * @param instance - shall not be null, if not used use ""
     * @throws SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public void connect_tds(String host, String port, String databaseName,
            String userName, String password, boolean useNamedPipes,
            String domain, String instance) throws SQLException, ClassNotFoundException
        {
        //
        boolean connectionOk = true;
        //
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        //
        String port_ = "";
        if (port.isEmpty() == false)
            {
            port_ = ":" + port;
            }
        //
        String connectionUrl = "jdbc:jtds:sqlserver://" + host + port_ + ";"
                + "databaseName=" + databaseName + ";user=" + userName + ";password=" + password;//+ ";namedPipe=true" -> requires "domain=" paramter!
        //
        if (useNamedPipes)
            {
            connectionUrl += ";namedPipe=true;domain:" + domain;

            }
        //
        if (instance != null && instance.isEmpty() == false)
            {
            connectionUrl += ";instance=" + instance;
            }
        //
        logg_connection_string(connectionUrl);
        //
        DriverManager.setLoginTimeout(SQL_LOGIN_TIME_OUT);
        //
        connection = DriverManager.getConnection(connectionUrl);
        //
        if (connectionOk)
            {
            if (CREATE_STATEMENT_SIMPLE == true)
                {
                statement = connection.createStatement();
                statement_2 = connection.createStatement();
                }
            else
                {
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement_2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                }
            }
        //
        if (statement == null)
            {
            SimpleLoggerLight.logg("sql_conn.log", "Connection to: " + host + " / dbname: " + databaseName + " failed");
            }
        }

    private void logg_connection_string(String url)
        {
        if (LOGG_CONNECTION_STRING)
            {
            SimpleLoggerLight.logg("connection_string.log", url);
            }
        }

    private void connect_jdbc(String host, String port, String databaseName, String userName, String password) throws SQLException, ClassNotFoundException
        {

        //Class.forName("com.mysql.jdbc.Driver");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //
        String port_ = "";
        if (port.isEmpty() == false)
            {
            port_ = ":" + port;
            }
        //
        String connectionUrl = "jdbc:sqlserver://" + host + port_ + ";"
                + "databaseName=" + databaseName + ";user=" + userName + ";password=" + password;
        //
        logg_connection_string(connectionUrl);
        //
        //For Trelleborgs connection it seems to be important!!
        DriverManager.setLoginTimeout(SQL_LOGIN_TIME_OUT);
        //
        connection = DriverManager.getConnection(connectionUrl);
        //
        if (CREATE_STATEMENT_SIMPLE == false)
            {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement_2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
        else
            {
            statement = connection.createStatement();
            statement_2 = connection.createStatement();
            }
        //
        }

    /**
     * This one is for having access to .mdb and .accdb files with Java8 OBS!
     * For libraries look in folder lib -> mdbj8 (5 libraries) For the
     * distribution this 5 libraries must all be in the "lib" folder!! Skip
     * "mdbj8" folder!! As properties example see "freeq_mdbj8.properties"
     *
     * @param user
     * @param pass
     * @param pathToMdbFile
     * @param loadToMemory - true = loads to memory which may cause insufficient
     * Java Heap, false = means not load all db to memory which causes that's it
     * is loaded slower
     * @throws SQLException
     */
    private void connect_mdb_java_8(String user, String pass, String pathToMdbFile) throws SQLException
        {
        //
        ODBC_OR_MDB = true;
        //
        String connectionUrl;
        //
        if (UCANACCESS_LOAD_TO_MEMORY_OPTION)
            {
            connectionUrl = "jdbc:ucanaccess://" + pathToMdbFile;//jdbc:ucanaccess://C:/__tmp/test/zzz.accdb
            }
        else
            {
            connectionUrl = "jdbc:ucanaccess://" + pathToMdbFile + ";memory=false";//jdbc:ucanaccess://C:/__tmp/test/zzz.accdb
            }
        //
        logg_connection_string(connectionUrl);
        //
        connection = DriverManager.getConnection(connectionUrl, user, pass);
        //
        if (CREATE_STATEMENT_SIMPLE == false)
            { // 
            statement = connection.createStatement(MDB_J_8_SCROLL, MDB_J_8_READ_UPDATE_TYPE);
            statement_2 = connection.createStatement(MDB_J_8_SCROLL, MDB_J_8_READ_UPDATE_TYPE);
            }
        else
            {
            statement = connection.createStatement();
            statement_2 = connection.createStatement();
            }
        }

    public static int MDB_J_8_SCROLL = ResultSet.TYPE_SCROLL_SENSITIVE;
    public static int MDB_J_8_READ_UPDATE_TYPE = ResultSet.CONCUR_READ_ONLY;

    /**
     * For connecting with ODBC. Fits for ACCESS databases also!! OBS!.
     * sun.jdbc.odbc.JdbcOdbcDriver is not supported in Java 1.8 it will throw
     * "java.lang.ClassNotFoundException: sun.jdbc.odbc.JdbcOdbcDriver"
     *
     * @param user
     * @param pass
     * @param odbc
     * @deprecated
     * @throws SQLException
     */
    private void connect_odbc(String user, String pass, String odbc) throws SQLException, ClassNotFoundException
        {
        ODBC_OR_MDB = true;
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String connectionUrl = "jdbc:odbc:" + odbc;
        //
        logg_connection_string(connectionUrl);
        //
        connection = DriverManager.getConnection(connectionUrl, user, pass);
        //
        if (CREATE_STATEMENT_SIMPLE == false)
            {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement_2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
        else
            {
            statement = connection.createStatement();
            statement_2 = connection.createStatement();
            }
        }

    private void connect_dbf(String user, String pass, String path) throws SQLException, ClassNotFoundException {
        //
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String connectionUrl = "jdbc:odbc:Driver={Microsoft dBASE Driver (*.dbf)};DefaultDir=" + path; //E:\\db
        //
        logg_connection_string(connectionUrl);
        //
        connection = DriverManager.getConnection(connectionUrl, user, pass);
        //
        if (CREATE_STATEMENT_SIMPLE == false) {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement_2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } else {
            statement = connection.createStatement();
            statement_2 = connection.createStatement();
        }
        //
    }

    /**
     * OBS!. sun.jdbc.odbc.JdbcOdbcDriver is not supported in Java 1.8 it will
     * throw "java.lang.ClassNotFoundException: sun.jdbc.odbc.JdbcOdbcDriver"
     *
     * @param user
     * @param pass
     * @param pathToMdbFile
     * @deprecated
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void connect_mdb(String user, String pass, String pathToMdbFile) throws SQLException, ClassNotFoundException
        {
        ODBC_OR_MDB = true;

        //Class.forName("com.mysql.jdbc.Driver");
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String connectionUrl = "jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb)};DBQ=" + pathToMdbFile;
        //
        logg_connection_string(connectionUrl);
        //
        connection = DriverManager.getConnection(connectionUrl, user, pass);
        //
        if (CREATE_STATEMENT_SIMPLE == false)
            {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement_2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
        else
            {
            statement = connection.createStatement();
            statement_2 = connection.createStatement();
            }
        }
    

    /**
     * [2020-04-09]
     *
     * @param dataBasePath
     * @throws SQLException
     * @throws FileNotFoundException
     */

    private boolean connect_sqllite(String dataBasePath) throws SQLException
        {
        //
        File f = new File(dataBasePath);
        //

        if (f.exists() == false)
            {
            return false;
            }
        //
        connection = DriverManager.getConnection("jdbc:sqlite:" + dataBasePath);
        //
        statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement_2 = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        //
        return true;
        }

    //    
    private void connect_mysql(String host, String port, String databaseName, String userName, String password) throws SQLException, ClassNotFoundException
        {
        Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://195.178.232.239:3306/m09k2847","m09k2847","636363");
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName, userName, password);
        statement = connection.createStatement();
        statement_2 = connection.createStatement();
        }

    @Override
    public PreparedStatement prepareStatementB(String q) throws SQLException
        {
        return connection.prepareStatement(q);
        }

    @Override
    public void prepareStatement(String q) throws SQLException
        {
        p_statement = connection.prepareStatement(q);
        }

    @Override
    public PreparedStatement getPreparedStatement()
        {
        return p_statement;
        }

    @Override
    public int executeUpdatePreparedStatement() throws SQLException
        {
        return p_statement.executeUpdate();
        }

    public void loggSqlExceptionWithQuerry(String logFile, SQLException ex, String query)
        {
        if (ex.toString().contains("String or binary data would be truncated"))
            {
            SimpleLoggerLight.logg(logFile, "!IMPORTANT! Exeption: " + ex.toString() + "\nQuery: " + query);
            }
        else
            {
            SimpleLoggerLight.logg(logFile, "Exeption: " + ex.toString() + "\nQuery: " + query);
            }
        }

    @Override
    public ResultSet execute(String sql, ShowMessage sm) throws SQLException
        {
        //
        sm.showMessage(sql);
        //
        if (statement.execute(sql))
            {
            return statement.getResultSet();
            }
        //
        return null;
        }

    @Override
    public synchronized ResultSet execute(String sql) throws SQLException
        {
        if (statement.execute(sql))
            {
            return statement.getResultSet();
            }
        return null;
        }

    @Override
    public synchronized ResultSet execute_2(String sql) throws SQLException
        {
        if (statement_2.execute(sql))
            {
            return statement_2.getResultSet();
            }
        return null;
        }

    @Override
    public int update(String sql) throws SQLException
        {
        return statement.executeUpdate(sql);
        }

    public void close() throws SQLException
        {
        statement.close();
        connection.close();
        }
    }



