package com.hbn.rdb.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author wangheng
 * @create 2019-01-16 下午3:45
 * @desc
 * 请求数据获取 返回结果的类
 * 与 驱动相关 数据
 * 连接创建
 * query 请求
 * 关闭连接 释放 资源等
 *
 * 本类 只对sqlsourcehelper 内部对象
 *
 **/
public class DriverQuery {

    private  static  final Logger logger = LoggerFactory.getLogger(DriverQuery.class);

    private   Connection connection = null ;
    private   PreparedStatement statement = null ;
    private   ResultSet resultSet = null;




    public int getRowCount() {
        return rowCount;
    }

    private static int rowCount = 0 ;
    private static RDBconfig  rdBconfig=null ;
    public  void init(RDBconfig  RDBconfig){
        if(RDBconfig==null){
            logger.error(" init  failed   driver message  has some  error " );
        }else {
            this.rdBconfig = RDBconfig;
            logger.info("url is  {}", rdBconfig.getUrl());
            logger.info("Driver class  is  {}",rdBconfig.getDriverclass());
            logger.info("dbuser  is   {}" ,rdBconfig.getDbuser());
        }


    }
    public  void createConnection(){
        logger.info("start create connection");
        try {
            //反射方式获取驱动名称
            Class.forName(rdBconfig.getDriverclass());
            this.connection = DriverManager.getConnection(rdBconfig.getUrl(),rdBconfig.getDbuser(),rdBconfig.getDbpassword());
        } catch (ClassNotFoundException e) {
            logger.error("create connection failed  {}",e);
        } catch (SQLException e) {
            logger.error("connection url is  {}",rdBconfig.getUrl());
            logger.error("create connection failed {}",e);
        }
    }


    public  void  createPrepareStatement(String  sql) throws SQLException {
        this.statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY) ;
    }


    public   ResultSet executeQuerySQL(String  sql) {
        //String  sql = "select * from  class";
        ResultSet  result = null;
        try {
            statement =  connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY) ;

            resultSet = statement.executeQuery();


        } catch (SQLException e) {
            logger.error("  query failed and the query is  {}  ,the  error  is  {}",sql ,e);
            try {
                Statement  statementInner = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                resultSet = statementInner.executeQuery(sql);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            result = resultSet ;
            resultSet = null ;
        }



        return result ;
    }

    public ResultSet executeQuery(String sql ,boolean  flag){

        if( flag ){
        ResultSet  rs = executeQuerySQL(sql);
        int rowNum = 0 ;
        try {
            /* 获取行数 */
            ResultSet resultSetTemp = executeQuerySQL(" select count(*)  from ( " + sql + " )");
            if(resultSetTemp != null && resultSetTemp.next()){
                rowNum = resultSetTemp.getInt(1);
                resultSetTemp.close();
                resultSetTemp = null ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            rowCount = 0 ;
            if(rowNum >0){
                rowCount = rowNum ;
                logger.info("rowCount is {}" ,rowCount);
            }
        }

        return rs ;
        }else {
            ResultSet  rs = executeQuerySQL(sql);
            return rs ;
        }

    }


    public  void stop(){
        rdBconfig = null ;
        if(resultSet !=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                resultSet = null ;
            }
        }
        if(statement !=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                statement = null ;
            }
        }
        if(connection !=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                connection =null ;
            }
        }

    }





}
