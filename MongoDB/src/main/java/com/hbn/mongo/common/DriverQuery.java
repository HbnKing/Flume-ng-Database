package com.hbn.mongo.common;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wangheng
 * @create 2019-01-16 下午3:45
 * @desc
 * 请求数据获取 返回结果的类
 * 与 驱动相关 数据
 * 连接创建
 * query 请求
 * 关闭连接 释放 资源等
 *
 * 本类 只对sqlsourcehelper 内部对象
 *
 *
 *
 **/
public class DriverQuery {

    private  Logger logger = LoggerFactory.getLogger(DriverQuery.class);



    private  RDBConfig  rdBconfig=null ;
    private  MongoCollection<Document> collection = null ;
    private  MongoClient mongoClient = null ;
    private  MongoCursor<ChangeStreamDocument<Document>> iterator = null ;

    //初始化参数
    public   void init(RDBConfig  RDBconfig){
        if(RDBconfig==null){
            logger.error(" init  failed   driver message  has some  error " );
        }else {
            this.rdBconfig = RDBconfig;
            logger.info("url is  {}", rdBconfig.getUrl());
            logger.info("dbuser  is   {}" ,rdBconfig.getDbname());
            logger.info("dbuser  is   {}" ,rdBconfig.getTablename());
        }
    }


    //创建数据库 连接
    public  void createConnection(){
        logger.info("start create connection");
        try {
            //反射方式获取驱动名称
            MongoClient mongoClient = new MongoClient(new MongoClientURI(rdBconfig.getUrl()));
            collection = mongoClient.getDatabase(rdBconfig.getDbname()).getCollection(rdBconfig.getTablename());
            iterator = collection.watch().fullDocument(FullDocument.DEFAULT).iterator();
        }catch (Exception e){

            logger.error(e.toString());
        }finally {
            if(collection !=null){
                logger.info("createConnection  success ! ");
            }else {
                logger.error("createConnection  failed ! ");
            }
        }
    }




   public ChangeStreamDocument<Document> getnext(){
       ChangeStreamDocument<Document> next = iterator.next();
       return next;
   }



    public MongoCollection getMongoCollection(){
        return collection ;
    }


    public  void stop(){
        rdBconfig = null ;
        if(collection !=null){
            try {
                collection = null;
            } catch (Exception e) {
               logger.error(e.toString());
            }finally {
                collection = null ;
            }
        }
        if( mongoClient !=null){
            try {
                mongoClient.close();
            } catch (Exception e) {
                logger.error(e.toString());
            }finally {
                mongoClient =null ;
            }
        }

    }





}
