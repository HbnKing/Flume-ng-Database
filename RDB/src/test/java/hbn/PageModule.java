package hbn;

import com.alibaba.fastjson.JSONObject;

import com.hbn.rdb.page.PageableResultSet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author wangheng
 * @create 2019-03-07 下午1:23
 * @desc
 *
 * 测试分页模型 demo
 *
 *
 **/
public class PageModule {

    public static void main(String[] args) throws SQLException {

        PageModule.PageTest();

    }


    public static void PageTest() throws SQLException {

        ResultSet resultSet = JdbcConnectionTest.getConnection();
        PageableResultSet pageableResultSet = new PageableResultSet(resultSet);

        pageableResultSet.setPageSize(1000);

        pageableResultSet.gotoPage(1);

        int  data = 0 ;

        com.alibaba.fastjson.JSONObject jsonObj = new JSONObject();
        for(int  i = 0 ; i<pageableResultSet.getPageRowsCount();i++){
            ResultSetMetaData metaData = pageableResultSet.getMetaData();
            for (int j = 1; j <= 3; j++) {
                String columnName = metaData.getColumnLabel(j);
                String value = pageableResultSet.getString(columnName);
                //将 columnName  和 value  放置在 json  中
                jsonObj.put(columnName,value);
            }
            System.out.println(jsonObj);
            pageableResultSet.next();
        }

        System.out.println(data);

        System.out.println(pageableResultSet.getPageCount());

        pageableResultSet.getCurPage();

        for(int  i = 0 ; i<pageableResultSet.getPageRowsCount();i++){
            if(i == pageableResultSet.getPageRowsCount()-1) System.out.println(pageableResultSet.getString(1));
            data++ ;
        }

        System.out.println(data);

        }

    }


