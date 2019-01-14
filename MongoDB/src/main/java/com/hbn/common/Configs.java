package com.hbn.common;

/**
 * configs
 * flume  配置的名字
 * 主要是配置文件的名字 先写死
 *
 * 名称都为 静态对象
 */
public class Configs {
    /**
     * 不可实例化
     */
    private Configs() {
    }


    public static final String HOST = "host";
    public static final String AUTHENTICATION_ENABLED = "authenticationEnabled";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MODEL = "model";
    public static final String DB_NAME = "db";
    public static final String CONNECTIONSTR = "connectionstr";
    public static final String COLLECTION = "collection";
    public static final String NAME_PREFIX = "MongSink_";
    public static final String BATCH_SIZE = "batch";
    public static final String AUTO_WRAP = "autoWrap";
    public static final String WRAP_FIELD = "wrapField";
    public static final String TIMESTAMP_FIELD = "timestampField";
    public static final String OPERATION = "op";
    public static final String PK = "_id";
    public static final String OP_INC = "$inc";
    public static final String OP_SET = "$set";
    public static final String OP_SET_ON_INSERT = "$setOnInsert";

    public static final boolean DEFAULT_AUTHENTICATION_ENABLED = false;
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 27017;
    public static final String DEFAULT_DB = "events";
    public static final String DEFAULT_COLLECTION = "events";
    public static final int DEFAULT_BATCH = 100;
    private static final Boolean DEFAULT_AUTO_WRAP = false;
    public static final String DEFAULT_WRAP_FIELD = "log";
    public static final String DEFAULT_TIMESTAMP_FIELD = null;
    public static final char NAMESPACE_SEPARATOR = '.';
    public static final String OP_UPSERT = "upsert";
    public static final String EXTRA_FIELDS_PREFIX = "extraFields.";

}
