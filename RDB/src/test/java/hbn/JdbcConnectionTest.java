package hbn;

import com.hbn.rdb.common.DriverQuery;
import com.hbn.rdb.common.RDBconfig;
import com.hbn.rdb.page.PageableResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
       /* PageableResultSet pageableResultSet = new PageableResultSet(JdbcConnectionTest.getOrcaleConnection());
        pageableResultSet.setPageSize(1000);
        System.out.println(pageableResultSet.getPageCount());*/
       ResultSet  rs = JdbcConnectionTest.getOrcaleConnection();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()){

            for(int i = 1 ; i<=columnCount ; i ++){

                System.out.println(metaData.getColumnLabel(i)+" : "+i +" : "+ rs.getObject(i) );
            }
       }


    }

    public static ResultSet getOrcaleConnection() throws SQLException{
        RDBconfig rdBconfig = new RDBconfig();
        rdBconfig.setUrl("jdbc:oracle:thin:@//121.201.68.63:1521/orcl");
        rdBconfig.setDbpassword("yyj");
        rdBconfig.setDbuser("yyj");
        rdBconfig.setDriverclass("oracle.jdbc.OracleDriver");

        DriverQuery driverQuery = new DriverQuery();
        driverQuery.init(rdBconfig);
        driverQuery.createConnection();
        //ResultSet resultSet = driverQuery.executeQuerySQL("select count(*) from ( select a.id,a.COUPON_id,b.id from USER_COUPON_CODE_1 a ,COUPON_CODE b  where a.COUPON_id = b.id )");
        ResultSet resultSet = driverQuery.executeQuerySQL(" select a.id,a.COUPON_id,b.id from USER_COUPON_CODE_1 a ,COUPON_CODE b  where a.COUPON_id = b.id ");
        //ResultSet resultSet = driverQuery.executeQuery("select count(*) from COUPON_CODE ");
        //System.out.println(resultSet.next());


        if(resultSet.next()) {
            System.out.println("总数量为 " + resultSet.getObject(1));
        }

        return  resultSet ;
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
