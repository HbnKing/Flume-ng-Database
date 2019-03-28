package com.hbn.mongo.common;

import com.mongodb.MongoClient;

/**
 * @author wangheng
 * @create 2019-03-22 下午5:21
 * @desc
 *
 * 获取环境变量的一些东西
 * 参数名 和 默认值
 **/
public class ConfigConstant {
    private ConfigConstant(){}

    public static final String COLUMNS_TO_SELECT = "";
    public static final String TABLE = "";
    public static final String AUTOINCREMENTFIELD = "";
    public static final String CHARSET_RESULTSET = "";
    public static final String DEFAULT_CHARSET_RESULTSET = "";
    public static final String DEFAULT_COLUMNS_TO_SELECT = "";
    public static final String DRIVER = "";
    public static final String USER = "";
    public static final String URL = "";
    public static final String FILEPATH ="" ;
    public static final String FILENAME ="" ;
    public static final String DEFAULT_FILEPATH ="" ;
    public static final String DEFAULT_FILENAME = "";
    public static final String BEGINNING ="";
    public static final String CUSTOMQUERY="";



    /*需要读取的 参数 的名称*/
    public static final String CONNECTIONSTR  = "connectionstr";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String AUTHENTICATION_ENABLED = "authenticationEnabled";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MODEL = "model";
    public static final String DB_NAME = "db";
    public static final String COLLECTION = "collection";
    public static final String NAME_PREFIX = "MongSink_";
    public static final String BATCH_SIZE = "batchsize";
    public static final String AUTO_WRAP = "autoWrap";
    public static final String WRAP_FIELD = "wrapField";
    public static final String TIMESTAMP_FIELD = "timestampField";
    public static final String OPERATION = "op";
    public static final String PK = "_id";
    public static final String OP_INC = "$inc";
    public static final String OP_SET = "$set";
    public static final String OP_SET_ON_INSERT = "$setOnInsert";

    /* 默认值常用的默认值 */
    public static final boolean DEFAULT_AUTHENTICATION_ENABLED = false;
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 27017;
    public static final String DEFAULT_DB = "events";
    public static final String DEFAULT_COLLECTION = "events";
    public static final int DEFAULT_BATCH_SIZE = 100;
    private static final Boolean DEFAULT_AUTO_WRAP = false;
    public static final String DEFAULT_WRAP_FIELD = "log";
    public static final String DEFAULT_TIMESTAMP_FIELD = null;
    public static final char NAMESPACE_SEPARATOR = '.';
    public static final String OP_UPSERT = "upsert";
    public static final String EXTRA_FIELDS_PREFIX = "extraFields.";


}
