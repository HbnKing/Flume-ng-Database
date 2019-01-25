package com.hbn.mongo.sink;


import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.hbn.common.Configs;
import com.hbn.common.MongoConfig;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.instrumentation.SinkCounter;
import org.apache.flume.sink.AbstractSink;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;

import static com.hbn.mongo.sink.MongoSinkConstants.*;

public class MongoSink extends AbstractSink implements Configurable {

	private static final Logger logger = LoggerFactory.getLogger(MongoSink.class);

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

	private MongoConfig  mongoConfig ;


	@Override
	public Status process() throws EventDeliveryException {
		Status status = Status.READY;
		List<Document> documents = new ArrayList<Document>(batchSize);
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();
		try {
			transaction.begin();
			long count;
			for (count = 0; count < batchSize; ++count) {
				Event event = channel.take();
				if (event == null) {
					break;
				}
				//String cuTime = getCurrentTime();
				String jsonEvent = new String(event.getBody(), StandardCharsets.UTF_8);
				//TODO 根据不同的JSON 指定severId 插入不同的数据库
				logger.info(jsonEvent);

				//这里转化 可能会有异常  因为  给定的数据如果不是 json 格式 name转换就会有问题
				/**
				 * 如果一次转换不成功 ，继续转换也不会成功的 。
				 * 因为是格式问题
				 * 所以 直接 log
				 *
				 */
				Document sentEvent = null ;

				try {
					sentEvent= Document.parse(jsonEvent);
					System.out.println(sentEvent);
				}catch(Exception e){
					logger.error("can't  parse  event  to  document  " ,e.toString(),event);

				}

				documents.add(sentEvent);
			}
			if (count <= 0) {
				sinkCounter.incrementBatchEmptyCount();
				status = Status.BACKOFF;
			} else {
				if (count < batchSize) {
					sinkCounter.incrementBatchUnderflowCount();
					status = Status.BACKOFF;
				} else {
					sinkCounter.incrementBatchCompleteCount();
				}
				sinkCounter.addToEventDrainAttemptCount(count);
				collection.insertMany(documents);

			}
			transaction.commit();
			sinkCounter.addToEventDrainSuccessCount(count);
		} catch (Throwable t) {
			try {
				transaction.rollback();
			} catch (Exception e) {
				logger.error("Exception during transaction rollback.", e);
			}
			logger.error("Failed to commit transaction. Transaction rolled back.", t);
			if (t instanceof Error || t instanceof RuntimeException) {
				Throwables.propagate(t);
			} else {
				throw new EventDeliveryException("Failed to commit transaction. Transaction rolled back.", t);
			}
		} finally {
			if (transaction != null) {
				transaction.close();
			}
		}

		return status;
	}

	@Override
	public synchronized void start() {
		logger.info("Starting MongoDB sink");
		sinkCounter.start();
		try {
			client = new MongoClient(new MongoClientURI(mongoConfig.getCONNECTIONSTR()));
			collection =client.getDatabase(databaseName).getCollection(collectionName);


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
	}

	@Override
	public synchronized void stop() {
		logger.info("Stopping MongoDB sink");
		try{
			if (client != null) {
				client.close();
			}
			client = null ;
		}catch ( Exception e){
			throw new FlumeException("Error closing table.", e);
		}
		sinkCounter.incrementConnectionClosedCount();
		sinkCounter.stop();

	}

	@Override
	public void configure(Context context) {
		try {
			System.out.println("系统 是否可以获取 " +context.getString(Configs.CONNECTIONSTR));
			if (sinkCounter == null) {
				sinkCounter = new SinkCounter(getName());
			}
			if(mongoConfig == null){
				mongoConfig = MongoConfig.getMongoConfig(context);
			}
			collectionName = mongoConfig.getTABLENAME();
			databaseName = mongoConfig.getDATABASE();
			batchSize = mongoConfig.getBatchSize();
		}catch (Exception e){
			logger.error(e.toString());
		}

	}


}