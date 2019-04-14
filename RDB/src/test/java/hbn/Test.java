/**
 * @author wangheng
 * @date 2019-04-04 上午10:57
 * @description
 **/
package hbn;

/**
 * @author wangheng
 * @date 2019-04-04 上午10:46
 * @description
 **/


import java.sql.*;

/**
 * @author wangheng
 * @create 2019-03-07 下午1:09
 * @desc
 *
 * 一个专门测试 数据库 连接的  demo
 **/
public class Test {


    public static void main(String[] args) throws SQLException {
       /* PageableResultSet pageableResultSet = new PageableResultSet(JdbcConnectionTest.getOrcaleConnection());
        pageableResultSet.setPageSize(1000);
        System.out.println(pageableResultSet.getPageCount());*/
        ResultSet rs = Test.createConnection();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()){

            for(int i = 1 ; i<=columnCount ; i ++){

                System.out.println(metaData.getColumnLabel(i)+" : "+i +" : "+ rs.getObject(i)   +" class " );
                if(i == 12){
                    System.out.println(rs.getObject(i).getClass());
                }
            }
        }

    }


    public static ResultSet createConnection() throws SQLException {
        Connection connection = null;
        try {
            //反射方式获取驱动名称
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://121.201.68.63:13306/demo", "root", "root");
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }

        PreparedStatement preparedStatement = connection.prepareStatement("select * from new_table", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet ;
    }



}

