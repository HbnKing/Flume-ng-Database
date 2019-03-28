package source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangheng
 * @create 2019-02-16 下午5:59
 * @desc
 **/
public class MysqlConnection {

    private static Connection conn;

    private static final Logger logger = LoggerFactory.getLogger(MysqlConnection.class);
    private static String host;
    private static Integer port;
    private static String user;
    private static String password;

    public static void setConnection(String mySQLHost, Integer mySQLPort, String mySQLUser,
                                     String mySQLPassword) {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection("jdbc:mysql://" + mySQLHost + ":" + mySQLPort
                        + "/", mySQLUser, mySQLPassword);
                logger.info("connected to mysql:{} : {}", mySQLHost, mySQLPort);
                host = mySQLHost;
                port = mySQLPort;
                user = mySQLUser;
                password = mySQLPassword;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                setConnection(host, port, user, password);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return conn;
    }

    public static Map<String, List<String>> getColumns(){
        Map<String, List<String>> cols = new HashMap<String, List<String>>();
        Connection conn = getConnection();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet r = metaData.getCatalogs();
            String tableType[] = { "TABLE" };
            while (r.next()) {
                String databaseName = r.getString("TABLE_CAT");
                ResultSet result = metaData.getTables(databaseName, null, null, tableType);
                while (result.next()) {
                    String tableName = result.getString("TABLE_NAME");
                    String key = databaseName + "." + tableName;
                    ResultSet colSet = metaData.getColumns(databaseName, null, tableName, null);
                    cols.put(key, new ArrayList<String>());
                    while (colSet.next()) {
                        String column = colSet.getString("COLUMN_NAME");
                        cols.get(key).add(column);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        return cols;
    }
}
