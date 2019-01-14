package com.hbn.common;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.flume.Context;
import org.apache.flume.conf.Configurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.hbn.common.Configs.AUTHENTICATION_ENABLED;
import static com.hbn.common.Configs.HOST;
import static com.hbn.common.DefaultConfig.DEFAULT_AUTHENTICATION_ENABLED;
import static com.hbn.common.DefaultConfig.DEFAULT_HOST;
import static com.hbn.mongo.sink.MongoSinkConstants.*;
import static com.hbn.mongo.sink.MongoSinkConstants.BATCH_SIZE;
import static com.hbn.mongo.sink.MongoSinkConstants.DEFAULT_BATCH_SIZE;

/**
 * @author wangheng
 * @create 2019-01-11 下午4:58
 * @desc
 * 关于mongoDB 的 一些配置信息
 *
 **/
public class MongoConfig implements Configurable {
    //读取配置文件 单例模式
    private MongoConfig(){

    }

    private static final MongoConfig mongoConfig = new MongoConfig();
    public static MongoConfig getMongoConfig(Context context) {
        //先初始化 内部的参数
        mongoConfig.configure(context);
        return mongoConfig;
    }
    private static Logger logger = LoggerFactory.getLogger(MongoConfig.class);


    //hostname & port  like 192.168.3.1:27017,192.168.3.2:27017
    //多个使用逗号分隔
    /**
     * Comma-separated list of hostname:port. If the port is not present the
     * default port 27017 will be used.
     */
    private String HOSTNAME ;


    /**
     * Database name.
     */
    private static  String DATABASE ;

    /**
     * Collection name
     */
    private static  String COLLECTION ;

    /**
     * User name.
     */
    private static  String USERNAME ;

    /**
     * Password.
     */
    private static  String PASSWORD ;

    /**
     * Maximum number of events the sink should take from the channel per
     * transaction, if available. Defaults to 100.
     */
    private static  int BATCH_SIZE ;


    /**
     * authentication_enabled
     */
    private static  boolean AUTHENTICALTION;

    /**
     * sever-ID NAME
     */
    private static  String ServerName ;

    /**
     *
     */
    private static  String CONNECTIONSTR  = null;

    private static MongoClient MONGOCLIENT = null ;

    private static String TABLENAME = null;


    @Override
    public void configure(Context context) {

        //setName(NAME_PREFIX + counter.getAndIncrement());



        AUTHENTICALTION = context.getBoolean(AUTHENTICATION_ENABLED, DEFAULT_AUTHENTICATION_ENABLED);
        //开启认证 设置 用户名密码

        if (AUTHENTICALTION) {
            USERNAME = context.getString(Configs.USERNAME);
            PASSWORD = context.getString(Configs.PASSWORD);
        } else {
            USERNAME = "";
            PASSWORD = "";
        }
        //获取一些其他信息 完成Mongoconfig 的 数据初始化
        HOSTNAME = context.getString(HOST, DEFAULT_HOST);
        TABLENAME = context.getString(Configs.COLLECTION,DefaultConfig.DEFAULT_COLLECTION);
        //model = MongoSinks.CollectionModel.valueOf(context.getString(MODEL, MongoSinks.CollectionModel.SINGLE.name()));
        DATABASE = context.getString(Configs.DB_NAME, DefaultConfig.DEFAULT_DB);
        COLLECTION = context.getString(Configs.COLLECTION, DefaultConfig.DEFAULT_COLLECTION);
        BATCH_SIZE = context.getInteger(Configs.BATCH_SIZE, DefaultConfig.DEFAULT_BATCH);
        //autoWrap = context.getBoolean(AUTO_WRAP, DEFAULT_AUTO_WRAP);
       // wrapField = context.getString(WRAP_FIELD, DEFAULT_WRAP_FIELD);
        //timestampField = context.getString(TIMESTAMP_FIELD, DEFAULT_TIMESTAMP_FIELD);
        //extraInfos.putAll(context.getSubProperties(EXTRA_FIELDS_PREFIX));

        CONNECTIONSTR = context.getString(Configs.CONNECTIONSTR);

        logger.info(Configs.CONNECTIONSTR,CONNECTIONSTR);



    }

    /**
     * 获取该对象的属性
     * @return
     *
     */
    public String getHOSTNAME() {
        return mongoConfig.HOSTNAME;
    }

    public static String getDATABASE() {
        return mongoConfig.DATABASE;
    }

    public static String getCOLLECTION() {
        return mongoConfig.COLLECTION;
    }

    public static String getUSERNAME() {
        return mongoConfig.USERNAME;
    }

    public static String getPASSWORD() {
        return mongoConfig.PASSWORD;
    }

    public static int getBatchSize() {
        return mongoConfig.BATCH_SIZE;
    }

    public static boolean isAUTHENTICALTION() {
        return mongoConfig.AUTHENTICALTION;
    }

    public static String getServerName() {
        return mongoConfig.ServerName;
    }

    public static String getTABLENAME(){
        return mongoConfig.TABLENAME ;
    }

    public static String getCONNECTIONSTR(){
        return mongoConfig.CONNECTIONSTR;

    }


    /*public static MongoClient getMongoClient(){
        if(MONGOCLIENT !=null){
            return MONGOCLIENT;
        }else if(CONNECTIONSTR!= null){
            MONGOCLIENT = new MongoClient(new MongoClientURI(CONNECTIONSTR));
            return MONGOCLIENT;
        }else {


            //稍后 再深入研究一下
            //mongodb://kwiner:test123@127.0.0.1/test?authMechanism=MONGODB-CR&maxPoolSize=500
            StringBuffer sb = new StringBuffer();
            sb.append("mongodb://");
            if(USERNAME !="") {
                sb.append(USERNAME);
                sb.append(":");
                sb.append(PASSWORD);
                sb.append("@");
            }

            sb.append(HOSTNAMES);

            sb.append("/");
            sb.append(DATABASE);

            CONNECTIONSTR =sb.toString();
            MONGOCLIENT = new MongoClient(new MongoClientURI(CONNECTIONSTR));
            return MONGOCLIENT ;
        }

    }*/


    /**
     *
     */


  /*  public synchronized void start() {
        logger.info("Starting MongoDB sink");
        sinkCounter.start();
        try {
            if (authentication_enabled) {
                client = new MongoClient(seeds, Arrays.asList(credential));
                MongoDatabase database = client.getDatabase(databaseName);
                collection = database.getCollection(collectionName);
            } else {
                client = new MongoClient(seeds);
                MongoDatabase database = client.getDatabase(databaseName);
                collection = database.getCollection(collectionName);
            }
            sinkCounter.incrementConnectionCreatedCount();
        } catch (Exception e) {
            logger.error("Exception while connecting to MongoDB", e);
            sinkCounter.incrementConnectionFailedCount();
            if (client != null) {
                client.close();
                sinkCounter.incrementConnectionClosedCount();
            }
        }
        super.start();
        logger.info("MongoDB sink started");
    }*/
}
