package com.hbn.rdb.common;

/**
 * @author wangheng
 * @create 2019-01-22 上午10:19
 * @desc
 * 共用默认值
 * 1属性名称
 * 2属性默认值
 *
 **/
public class DefaultConfig {
    private DefaultConfig(){}
    //中间文件 保存位置
    public static final String FILEPATH = "filepath" ;
    public static final String FILENAME = "filename" ;
    public static final String DEFAULT_FILEPATH = "/var/lib/flume";
    public static final String DEFAULT_FILENAME = "SourceName";

    public static final int DEFAULT_QUERY_DELAY = 10000;
    public static final String BATCH_SIZE="batchsize";
    public static final int DEFAULT_BATCH_SIZE = 100;
    public static final int DEFAULT_MAX_ROWS = 10000;




    public static final String DEFAULT_INCREMENTAL_VALUE = "0";
    //默认分隔符
    public static final String DEFAULT_DELIMITER_ENTRY = ",";
    public static final Boolean DEFAULT_ENCLOSE_BY_QUOTES = true;


    public static final String URL_STATUS_FILE = "URL";

    public static final String LAST_INDEX_STATUS_FILE = "LastIndex";
    public static final String QUERY_STATUS_FILE = "Query";
    //默认字符集
    public static final String CHARSET_RESULTSET="charset";
    public static final String DEFAULT_CHARSET_RESULTSET = "UTF-8";

    //表级别的设置
    //默认表名



    //获取 连接的信息

    public static  final String URL = "connectionurl";
    public static final String DRIVER = "driverclass";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DBNAME = "db";

    // customquery
    public static final String CUSTOMQUERY = "customquery";
    //生成sql
    //默认列名
    public static final String COLUMNS_TO_SELECT = "columnstoselect";
    public static final String DEFAULT_COLUMNS_TO_SELECT = "*";
    public static final String TABLE = "table";
    public static final String DefaultTABLE = "table";

    public static final String AUTOINCREMENTFIELD = "autoincrementfield";
    //起始值  字段名 和 默认值
    public static final String BEGINNING = "begin";

    //默认起点
    //数值类型的默认起点
    public static final int DEFAULT_BEGINNING = 0 ;
    //时间类型的默认起点





}
