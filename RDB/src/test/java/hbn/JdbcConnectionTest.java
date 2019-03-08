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
 *
 * 一个专门测试 数据库 连接的  demo
 **/
public class JdbcConnectionTest {

    private static Logger logger = LoggerFactory.getLogger(JdbcConnectionTest.class);

    public static void main(String[] args) throws SQLException {
        int  row = JdbcConnectionTest.getConnection().getRow();
        System.out.println(row);


    }

    public static ResultSet  getConnection() throws SQLException {
        logger.info("started ");
        RDBconfig rdBconfig = new RDBconfig();
        rdBconfig.setUrl("jdbc:mysql://192.168.3.130:3306/test");
        rdBconfig.setDbpassword("root");
        rdBconfig.setDbuser("root");
        rdBconfig.setDriverclass("com.mysql.cj.jdbc.Driver");

        DriverQuery driverQuery = new DriverQuery();
        driverQuery.init(rdBconfig);
        driverQuery.createConnection();
        ResultSet resultSet = driverQuery.executeQuery("select * from newtable");
        return  resultSet ;
    }
}
