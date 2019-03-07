package hbn;

import com.hbn.rdb.common.DriverQuery;
import com.hbn.rdb.common.RDBconfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wangheng
 * @create 2019-03-07 下午1:09
 * @desc
 **/
public class JdbcConnectionTest {

    private static Logger logger = LoggerFactory.getLogger(JdbcConnectionTest.class);

    public static void main(String[] args) throws SQLException {
        JdbcConnectionTest.getConnection();


    }

    public static void  getConnection() throws SQLException {
        logger.info("started ");
        RDBconfig rdBconfig = new RDBconfig();
        rdBconfig.setUrl("jdbc:mysql://192.168.3.130:3306/test");
        rdBconfig.setDbpassword("root");
        rdBconfig.setDbuser("root");
        rdBconfig.setDriverclass("com.mysql.cj.jdbc.Driver");

        DriverQuery driverQuery = new DriverQuery();
        driverQuery.init(rdBconfig);
        driverQuery.createConnection();
        ResultSet resultSet = driverQuery.executeQuery("select * from class");
        while (resultSet.next()){
            System.out.println(resultSet.getString(1));
        }




    }
}
