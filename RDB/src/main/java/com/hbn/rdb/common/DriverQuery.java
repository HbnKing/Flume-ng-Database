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

    private  Logger logger = LoggerFactory.getLogger(DriverQuery.class);

    /*
    private String  driver = "" ;
    private String url ="" ;
    private String dbuser = "" ;
    private String password = "";
    */

    private   Connection connection = null ;
    private   Statement statement = null ;

    private  RDBconfig  rdBconfig=null ;
    public    void init(RDBconfig  RDBconfig){
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
            this.statement = connection.createStatement() ;
        } catch (ClassNotFoundException e) {
            logger.error("create connection failed  {}",e);
        } catch (SQLException e) {
            logger.error("connection url is  {}",rdBconfig.getUrl());
            logger.error("create connection failed {}",e);
        }
    }


    public   ResultSet executeQuery(String  sql) {
        ResultSet resultSet = null;
        logger.info(sql);
        //String  sql = "select * from  class";
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("  query failed and the query is  {}  ,the  error  is  {}",sql ,e);
        }

        return resultSet ;
    }


    public  void stop(){
        rdBconfig = null ;
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
