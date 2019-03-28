package com.hbn.mongo.sink;

import com.hbn.mongo.common.ConfigConstant;
import com.hbn.mongo.common.MongodbHelper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import org.apache.flume.Context;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangheng
 * @create 2019-03-22 下午10:15
 * @desc
 *
 * 定义 mongodb  sink 的 几种方式
 * 举例如下
 * 1.单条 逐条插入  /upsert
 * 2.批量插入  /upsert
 * 3.文档删除
 * 4.嵌套文档的更新
 *
 *
 **/
public class MongodbSinkHelper  {

    private static final Logger logger = LoggerFactory.getLogger(MongodbSinkHelper.class);
    /**
     * Builds an SQLSourceHelper containing the configuration parameters and
     * usefull utils for SQL Source
     *
     * @param context    Flume source context, contains the properties from configuration file
     * @param sourceName source file name for store status
     */


    private static Context context ;
    private static String sinkname ;
    private static String connectionstr ;
    private static String db ;
    private static String collection ;
    private static int batchSize ;
    private static MongoCollection<Document> mongoCollection ;

    public MongodbSinkHelper(Context context, String sinkName) {

        this.context = context ;
        this.sinkname = sinkname ;
        this.connectionstr = context.getString(ConfigConstant.CONNECTIONSTR);
        this.db = context.getString(ConfigConstant.DB_NAME);
        this.collection = context.getString(ConfigConstant.COLLECTION);
        this.batchSize = context.getInteger(ConfigConstant.BATCH_SIZE,ConfigConstant.DEFAULT_BATCH_SIZE);
        if(batchSize <=0){
            batchSize = ConfigConstant.DEFAULT_BATCH_SIZE;
        }

        logger.info("connectionstr is  {}" ,connectionstr);
        logger.info("db is  {}" ,db);
        logger.info("collection is  {}" ,collection);
        logger.info("batchSize is  {}" ,batchSize);

        mongoCollection = new MongoClient(new MongoClientURI(connectionstr)).getDatabase(db).getCollection(collection);



    }


    public MongoCollection<Document>  getMongoCollection(){
        if(mongoCollection == null){
            MongoClient mongoClient = new MongoClient(new MongoClientURI(connectionstr));
            mongoCollection = mongoClient.getDatabase(db).getCollection(collection);
        }

        logger.info("  return  mongocollection");
        return mongoCollection;
    }

    public int getBatchSize(){
        return batchSize ;
    }


    public void stop() {
    }
}
