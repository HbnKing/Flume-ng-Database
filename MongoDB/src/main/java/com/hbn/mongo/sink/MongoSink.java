package com.hbn.mongo.sink;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.hbn.mongo.common.ConfigConstant;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.instrumentation.SinkCounter;
import org.apache.flume.sink.AbstractSink;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.mongodb.client.MongoCollection;


public class MongoSink extends AbstractSink implements Configurable {

	private static final Logger logger = LoggerFactory.getLogger(MongoSink.class);

	private SinkCounter sinkCounter;

	private int batchSize ;

	private MongoCollection<Document> mongoCollection;

	private MongodbSinkHelper  mongodbSinkHelper ;


	@Override
	public Status process() throws EventDeliveryException {
		logger.info("mongodb sink started  process ");
		Status status = Status.READY;
		List<Document> documents = new ArrayList<Document>(batchSize);
		logger.info("mongosink batchsize  is {}",batchSize);
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();
		try {
			transaction.begin();
			long count;
			for (count = 0; count < batchSize; ++count) {
				Event event = channel.take();
				if (event == null) {
					continue;
				}
				//String cuTime = getCurrentTime();
				String jsonEvent = new String(event.getBody(), StandardCharsets.UTF_8);
				//TODO 根据不同的JSON 指定severId 插入不同的数据库
				//logger.info(jsonEvent);

				//这里转化 可能会有异常  因为  给定的数据如果不是 json 格式 name转换就会有问题
				/**
				 * 如果一次转换不成功 ，继续转换也不会成功的 。
				 * 因为是格式问题
				 * 所以 直接 log
				 */

				try {
					Document doc= Document.parse(jsonEvent);

					//documents.add(doc);
					mongoCollection.insertOne(doc);
					//logger.info("mongo  insert doc is {}",doc);
				}catch(Exception e){
					logger.error("can't  parse  event  to  document {}  event is {} " ,e.toString(),event);
				}
			}

			/*logger.info("documents  size  is {}",documents.size()  );
			if(documents.size()>0){
				if(mongoCollection ==null  ){
					mongoCollection  = mongodbSinkHelper.getMongoCollection();
				}
				mongoCollection.insertMany(documents);
				logger.info("mongosink  insertMany ");
			}*/


			/*if (count <= 0) {
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
				mongoCollection.insertMany(documents);

			}*/
			/*事务的提交*/
			transaction.commit();
			sinkCounter.addToEventDrainSuccessCount(count);

		} catch (Throwable t) {
			logger.error("mongo sink  exception ");
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

			sinkCounter.incrementConnectionCreatedCount();
		} catch (Exception e) {
			logger.error("Exception while connecting to MongoDB", e);
			sinkCounter.incrementConnectionFailedCount();
			sinkCounter.incrementConnectionClosedCount();
		}
		super.start();
		logger.info("MongoDB sink started");
	}

	@Override
	public synchronized void stop() {
		logger.info("Stopping MongoDB sink");
		mongodbSinkHelper.stop();
		sinkCounter.incrementConnectionClosedCount();
		sinkCounter.stop();

		super.stop();

	}

	@Override
	public void configure(Context context) {
		try {
			System.out.println("系统 是否可以获取 " +context.getString(ConfigConstant.CONNECTIONSTR));
			if (this.sinkCounter == null) {
				this.sinkCounter = new SinkCounter(getName());
			}
			if(this.mongodbSinkHelper == null){
				this.mongodbSinkHelper = new MongodbSinkHelper(context ,getName());
			}

			this.mongoCollection = mongodbSinkHelper.getMongoCollection();
			this.batchSize = mongodbSinkHelper.getBatchSize();
			logger.info("already  init ,batchsize is {} mongoCollection is {} " ,batchSize ,mongoCollection.count());
		}catch (Exception e){
			logger.error(e.toString());
			logger.error("init  configure  failed");
		}

	}


}