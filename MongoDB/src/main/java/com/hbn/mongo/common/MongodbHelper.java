package com.hbn.mongo.common;

import com.hbn.mongo.sink.MongodbSinkHelper;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.apache.flume.Context;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.instrumentation.SinkCounter;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hbn.mongo.sink.MongoSinkConstants.DEFAULT_BATCH_SIZE;

/**
 * @author wangheng
 * @create 2019-03-14 下午8:49
 * @desc
 *
 * 1获取参数 并解析
 * 2连接
 * 3数据库写入 等工作
 *
 * 继承类 有
 * MongoDBSourceHelper
 * MongodbSinkHelper
 *
 *
 *
 **/
public class MongodbHelper {




    protected MongoClient client;
    private MongoCollection<Document> collection;
    private List<ServerAddress> seeds;
    private MongoCredential credential;

    private String databaseName;
    private String collectionName;
    private boolean authentication_enabled;

    private int batchSize = DEFAULT_BATCH_SIZE;
    private boolean DEFAULT_AUTHENTICATION_ENABLED = false;

    private SinkCounter sinkCounter;

    private MongodbSinkHelper mongodbSinkHelper ;
    private static final Logger logger = LoggerFactory.getLogger(MongodbHelper.class);

    //数据库 连接信息
    //没有的值 就设置为null
    private static String drivername = null ;
    private static String conectionurl = null ;
    private static String username = null ;
    private static String password = null ;
    //表库 级别 的相关信息
    //private static String dbname = null ;

    //个性化sql部分

    private static String customerquery = null ;

    private static long begin = 0l;
    //  select id,field   from  table name  where  id  >=begin ;
    //这里需要 查询列名  表明  自增列名 ,该列的起始值
    private static String columnsToSelect = null ;
    private static String table = null ;
    private static String autoIncrementField = null ;

    // 生成的sql
    private static String query = null ;


    //中间文件状态记录
    //文件位置 和文件名称
    private static String filePath = null ;
    private static String fileName = null ;

    //通用配置
    private static String Charset = null ;
    //当前最大值 currentIndex
    //比较 来源 1  begin  2  filestatus 3  内存迭代更新
    //修改频繁要求较高  可以优化

    private static Long currentIndex ;

    //其他
    private String sourceName ;

    private Context context ;

    private Map<String, String> statusFileJsonMap = new LinkedHashMap<String, String>();

    //每一个sqlsource 内设置一个内部对象 保存该类的 数据库级别的连接信息

    private static int batchsize;

    /**
     * Builds an SQLSourceHelper containing the configuration parameters and
     * usefull utils for SQL Source
     *
     * @param context    Flume source context, contains the properties from configuration file
     * @param sourceName source file name for store status
     *
     */
    /*public MongodbHelper(Context context, String sourceName) {
        logger.info("started to  init SQLSourceHelper");

        this.context = context;

        this.sourceName = sourceName;
        //起点
        begin = context.getLong(ConfigConstant.BEGINNING,0L);
        logger.info("begin  is  {}",begin );

        //自己写的sql
        customerquery = context.getString(ConfigConstant.CUSTOMQUERY);

        logger.info("customerquery  is  {}" ,customerquery);

        //多个参数配的sql
        table = context.getString(ConfigConstant.TABLE);
        columnsToSelect = context.getString(ConfigConstant.COLUMNS_TO_SELECT,ConfigConstant.DEFAULT_COLUMNS_TO_SELECT);
        autoIncrementField = context.getString(ConfigConstant.AUTOINCREMENTFIELD);

        logger.info("table  is  {}",table);
        logger.info("columnsToSelect  is  {}",columnsToSelect);
        logger.info("autoIncrementField  is  {}",autoIncrementField);


        Charset = context.getString(ConfigConstant.CHARSET_RESULTSET, ConfigConstant.DEFAULT_CHARSET_RESULTSET);
        logger.info("charset  is  {}",Charset);


        *//**
         * 数据库连接配置
         *//*

        drivername = context.getString(ConfigConstant.DRIVER);

        username = context.getString(ConfigConstant.USER);
        password = context.getString(ConfigConstant.PASSWORD);
        conectionurl = context.getString(ConfigConstant.URL);
        logger.info("connectionurl  is  {}",conectionurl);
        logger.info("driver  is  {}",drivername);



        checkMandatoryProperties();
        //封装到 RDB

        MongodbConfig.


        //对该对象初始化
        //查询的初始化
        //driverQuery.init(RDBconfig);

        //中间文件状态 资料
        filePath = context.getString(ConfigConstant.FILEPATH,ConfigConstant.DEFAULT_FILEPATH);
        fileName = context.getString(ConfigConstant.FILENAME,ConfigConstant.DEFAULT_FILENAME);

        logger.info("filePath is  {}",filePath);
        logger.info("fileName is  {}",fileName);


        //更新 currentIndex
        //比较 起点  和  当前值 的最大值
        currentIndex = Math.max(begin ,getCurrentIndexStatusFile());
        logger.info("Index  started  from  {}" ,currentIndex);

        //  分页信息
        batchsize = context.getInteger(ConfigConstant.BATCH_SIZE ,ConfigConstant.DEFAULT_BATCH_SIZE);

    }
*/



    public void checkMandatoryProperties() {

        if (conectionurl == null) {
            throw new ConfigurationException("database - connection - url property not set");
        }

        if (table == null && customerquery == null) {
            throw new ConfigurationException("property table not set");
        }

        if (username == null) {
            throw new ConfigurationException("database - connection - user property not set");
        }

        if (password == null) {
            throw new ConfigurationException("database - connection - password property not set");
        }
    }

    public String buildQuery() {

        if (customerquery == null) {
            // 如果 customerQuery 为null，就以 offsest 作为 id
            return "SELECT " + columnsToSelect + " FROM " + table +" where " +autoIncrementField + " > " +currentIndex ;
        } else {
            //如果 customerquery 不为 null，那么就要将其最后已存在的 offset 替换掉新查询出的 offset（！！！）
            //
            if (customerquery.contains("$@$")) {
                //直接修改  返回行新的 sql 语句
                return customerquery.replace("$@$", String.valueOf(currentIndex));
            } else {
                //如果没有设置
                // currentIndex  应该为 begin  默认值 Long.MIN_VALUE
                //还可以在修改一下
        /*if(currentIndex.equals(Long.MIN_VALUE)){
          return customerquery = customerquery.substring(0,customerquery.indexOf(">")+1)+currentIndex;
        }else return customerquery ;*/
                return customerquery ;

            }
        }
    }



    /**
     *
     * 返回内部对象
     *
     * 但是要注意 driverquery  内部的属性要 提前初始化
     * 不然返回时 空值
     *
     * @return
     */

    public void  updateStatusFile(Long  CurrentIndex){

        FileStatus.updateProperties(filePath ,fileName, CurrentIndex);
    }
    public void  updateStatusFile(){
        FileStatus.updateProperties(filePath ,fileName, currentIndex);
    }


    public long  getCurrentIndexStatusFile(){
        return  FileStatus.getProperties(filePath ,fileName);
    }


    public long  getCurrentIndex(){
        return currentIndex ;
    }

    public void   setCurrentIndex( long CurrentIndex ){
        this.currentIndex = CurrentIndex ;
    }


    public int getBatchsize() {
        return batchsize;
    }

    public void setBatchsize(int batchsize) {
        this.batchsize = batchsize;
    }

    public String getautoIncrementField(){
        return autoIncrementField ;
    }

    public boolean  isSelectContains(){
        //没有设置自增字段
        if(autoIncrementField ==null){
            return false ;
            //包含
        }else if(customerquery!=null && (customerquery.contains("*")||customerquery.contains(autoIncrementField))){
            return true ;

        }else if(columnsToSelect!=null && (columnsToSelect.contains("*")||columnsToSelect.contains(autoIncrementField))){
            return true ;
        }else return false;

    }


    public void stop(){
    }


    public void initDriver(){
    }

    public ResultSet executeQuery(){
        String sql  = buildQuery();

        return null;
    }

    public MongoCollection<Document> getMongoCollection() {
        return  null ;
    }

    public void init(Context context) {

    }

    public String getTABLENAME() {
        return null ;
    }

    public String getDATABASE() {
        return null ;
    }

    public int getBatchSize() {
        return 0;
    }
}

