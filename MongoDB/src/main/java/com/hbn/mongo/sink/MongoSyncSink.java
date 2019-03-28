package com.hbn.mongo.sink;

import com.hbn.mongo.common.MongoConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.conf.Configurable;
import org.apache.flume.instrumentation.SinkCounter;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.hbn.mongo.sink.MongoSinkConstants.DEFAULT_BATCH_SIZE;

/**
 * @author wangheng
 * @create 2019-01-15 下午3:55
 * @desc
 * mongodb  同步 插入
 * 每次插入 都要返回状态 且 设置 writeconcern
 * 并 获取状态值
 * 如果操作 没有指定 默认使用upsert
 *
 * 默认使用 upsert
 *
 **/
public class MongoSyncSink extends MongoSink implements Configurable  {

    private static final Logger logger = LoggerFactory.getLogger(MongoSyncSink.class);
    //private MongoClient client;
    private MongoCollection<Document> table;
    private List<ServerAddress> seeds;
    private MongoCredential credential;

    private String db_name;
    private String table_name;
    private boolean authentication_enabled;

    private int batchSize = DEFAULT_BATCH_SIZE;
    private boolean DEFAULT_AUTHENTICATION_ENABLED = false;

    private SinkCounter sinkCounter;

    private MongoConfig mongoConfig ;
    private MongoClient client;

    @Override
    public void configure(Context context) {


        try {
            logger.info("startted get configure ");
            if (sinkCounter == null) {
                sinkCounter = new SinkCounter(getName());
            }
            if(mongoConfig == null){
                mongoConfig = MongoConfig.getMongoConfig(context);
                logger.info("initialize  mongoconfig");
            }
            db_name = mongoConfig.getTABLENAME();
            logger.info(db_name);
            table_name = mongoConfig.getDATABASE();
            logger.info(table_name);
            batchSize = mongoConfig.getBatchSize();
            logger.info(String.valueOf(batchSize));

        }catch (Exception e){
            logger.error(e.toString());
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        logger.info("Starting MongoDB sink");
        sinkCounter.start();
        try {
            client = new MongoClient(new MongoClientURI(mongoConfig.getCONNECTIONSTR()));
            table =client.getDatabase(db_name).getCollection(table_name);
            sinkCounter.incrementConnectionCreatedCount();
        } catch (Exception e) {
            logger.error("Exception while connecting to MongoDB", e);
            sinkCounter.incrementConnectionFailedCount();
            if (client != null) {
                client.close();
                sinkCounter.incrementConnectionClosedCount();
            }
        }
        logger.info("MongoDB sink started");
    }

    @Override
    public Status process() throws EventDeliveryException {
        return null;
    }


    @Override
    public synchronized void stop() {
        super.stop();
    }


}
