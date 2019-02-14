package hbn;

import com.google.gson.JsonObject;
import com.hbn.rdb.common.DriverQuery;
import com.hbn.rdb.common.RDBconfig;
import com.hbn.rdb.source.SQLSourceHelper;
import org.apache.flume.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wangheng
 * @create 2019-02-02 上午10:54
 * @desc
 **/
public class RDBTest {

    private static Logger  logger = LoggerFactory.getLogger(RDBTest.class);
    public static void main(String[] args) throws SQLException {

        JsonObject  jsonObject = new JsonObject();
        System.out.println(""+jsonObject.get("key"));
        Context  context = new Context();
        getConnection();
        logger.info("--------");
        SQLSourceHelper  sqlSourceHelper = new SQLSourceHelper(context,"name");

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
