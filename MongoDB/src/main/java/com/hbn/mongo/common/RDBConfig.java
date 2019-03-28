package com.hbn.mongo.common;

/**
 * @author wangheng
 * @create 2019-01-16 下午4:02
 * @desc
 *
 * 关系型数据库连接 必备
 * 连接相关的一些配置
 *
 **/
public class RDBConfig {

    private  String url = null;
    private  String dbuser = null ;
    private  String dbpassword = null ;
    private  String driverclass = null ;
    private  String dbname = null ;
    private  String tablename = null ;

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }


    //一个默认的起点
    //private  long  IndexValue = Long.MIN_VALUE ;


    public   String getUrl(){
        return this.url ;
    }

    public  String getDbuser() {
        return this.dbuser;
    }

    public  String getDbpassword() {
        return this.dbpassword;
    }

    public  String getDriverclass() {
        return this.driverclass;
    }

    public  void setUrl(String url) {
        this.url = url;
    }

    public  void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public  void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public  void setDriverclass(String driverclass) {
        this.driverclass = driverclass;
    }
}
