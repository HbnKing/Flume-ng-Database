package com.hbn.mongo.sink;

import com.hbn.mongo.common.MongoConfig;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.instrumentation.SinkCounter;
import org.apache.flume.sink.AbstractSink;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author wangheng
 * @create 2019-01-11 下午11:35
 * @desc
 *  这里 只做 insert  op
 *
 *
 **/




public class MongoSinkwh extends AbstractSink implements Configurable {
    private static Logger logger = LoggerFactory.getLogger(MongoSinkwh.class);

    private static AtomicInteger counter = new AtomicInteger();

    private Mongo mongo;
    private DB db;

    private String host;
    private int port;
    private boolean authentication_enabled;
    private String username;
    private String password;
    //private CollectionModel model;
    private String dbName;
    private String collectionName;
    private int batchSize;
    private boolean autoWrap;
    private String wrapField;
    private String timestampField;
    private final Map<String, String> extraInfos = new ConcurrentHashMap<String, String>();

    private MongoConfig  mongoConfig ;
    private MongoClient  mongoClient ;
    private SinkCounter sinkCounter;

    private MongodbSinkHelper mongodbSinkHelper;





    private MongoCollection<Document> mongoCollection ;

    @Override
    public void configure(Context context) {

        /**
         * 初始化 MongodbSinkHelper
         */
        mongodbSinkHelper = new MongodbSinkHelper(context ,getName());
        try {

            sinkCounter = new SinkCounter(getName());
            mongoCollection  = mongodbSinkHelper.getMongoCollection();
            if(mongoCollection !=null){
                logger.info("connect  to  collections  successful !");
            }else {
                logger.error("connect  to  collections  failed !");
                throw   new  IllegalArgumentException("connect  to  collections  failed !") ;
            }
        }catch (IllegalArgumentException  e){
            logger.error(e.toString());
        }

    }

    @Override
    public synchronized void start() {
        logger.info("Starting {}...", getName());

        super.start();
        logger.info("Started {}.", getName());

        sinkCounter.incrementConnectionCreatedCount();
        sinkCounter.start();
    }

    @Override
    public Status process() throws EventDeliveryException {
        logger.debug("{} start to process event", getName());

        Status status = Status.READY;
        Channel channel = getChannel();
        Transaction tx = null;
        try {
            tx = channel.getTransaction();
            tx.begin();
            Event event = channel.take();
            if(event !=null){
                String body = new String(event.getBody(), StandardCharsets.UTF_8);

                saveEventOneByOne(body);
            }
            status = Status.READY;
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            status = Status.BACKOFF;
            logger.error("can't process events", e);
        }finally {
            tx.close();
        }
        logger.debug("{} processed event", getName());
        return status;
    }

    /*private void saveEvents(Map<String, List<DBObject>> eventMap) {
        if (eventMap.isEmpty()) {
            logger.debug("eventMap is empty");
            return;
        }

        for (Map.Entry<String, List<DBObject>> entry : eventMap.entrySet()) {
            List<DBObject> docs = entry.getValue();
            if (logger.isDebugEnabled()) {
                logger.debug("collection: {}, length: {}", entry.getKey(), docs.size());
            }
            int separatorIndex = entry.getKey().indexOf(NAMESPACE_SEPARATOR);
            String eventDb = entry.getKey().substring(0, separatorIndex);
            String collectionNameVar = entry.getKey().substring(separatorIndex + 1);

            //Warning: please change the WriteConcern level if you need high datum consistence.
            DB dbRef = mongo.getDB(eventDb);
            if (authentication_enabled) {
                boolean authResult = dbRef.authenticate(username, password.toCharArray());
                if (!authResult) {
                    logger.error("Failed to authenticate user: " + username + " with password: " + password + ". Unable to write events.");
                    return;
                }
            }
            try {
                CommandResult result = dbRef.getCollection(collectionNameVar)
                        .insert(docs, WriteConcern.SAFE).getLastError();
                if (result.ok()) {
                    String errorMessage = result.getErrorMessage();
                    if (errorMessage != null) {
                        logger.error("can't insert documents with error: {} ",
                                errorMessage);
                        logger.error("with exception", result.getException());
                        throw new MongoException(errorMessage);
                    }
                } else {
                    logger.error("can't get last error");
                }
            } catch (Exception e) {
                if (!(e instanceof com.mongodb.MongoException.DuplicateKey)) {
                    logger.error("can't process event batch ", e);
                    logger.debug("can't process doc:{}", docs);
                }
                for (DBObject doc : docs) {
                    try {
                        dbRef.getCollection(collectionNameVar).insert(doc,
                                WriteConcern.SAFE);
                    } catch (Exception ee) {
                        if (!(e instanceof com.mongodb.MongoException.DuplicateKey)) {
                            logger.error(doc.toString());
                            logger.error("can't process events, drop it!", ee);
                        }
                    }
                }
            }
        }
    }
*/
    /*private Status parseEvents() throws EventDeliveryException {
        Status status = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = null;
        Map<String, List<DBObject>> eventMap = new HashMap<String, List<DBObject>>();
        Map<String, List<DBObject>> upsertMap = new HashMap<String, List<DBObject>>();
        try {
            transaction = channel.getTransaction();
            transaction.begin();

            for (int i = 0; i < batchSize; i++) {
                Event event = channel.take();
                if (event == null) {
                    status = Status.BACKOFF;
                    break;
                } else {
                    String operation = event.getHeaders().get(OPERATION);
                    if (logger.isDebugEnabled()) {
                        logger.debug("event operation is {}", operation);
                    }
                    if (OP_UPSERT.equalsIgnoreCase(operation)) {
                        processEvent(upsertMap, event);
                    } else if (StringUtils.isNotBlank(operation)) {
                        logger.error("non-supports operation {}", operation);
                    } else {
                        processEvent(eventMap, event);
                    }
                }
            }
            if (!eventMap.isEmpty()) {
                saveEvents(eventMap);
            }
            if (!upsertMap.isEmpty()) {
                doUpsert(upsertMap);
            }

            transaction.commit();
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
    }*/
/*

    private void doUpsert(Map<String, List<DBObject>> eventMap) {
        if (eventMap.isEmpty()) {
            logger.debug("eventMap is empty");
            return;
        }

        for (Map.Entry<String, List<DBObject>> entry : eventMap.entrySet()) {
            List<DBObject> docs = entry.getValue();
            if (logger.isDebugEnabled()) {
                logger.debug("collection: {}, length: {}", entry.getKey(), docs.size());
            }

            int separatorIndex = entry.getKey().indexOf(NAMESPACE_SEPARATOR);
            String eventDb = entry.getKey().substring(0, separatorIndex);
            String collectionName = entry.getKey().substring(separatorIndex + 1);

            //Warning: please change the WriteConcern level if you need high datum consistence.
            DB dbRef = mongo.getDB(eventDb);
            if (authentication_enabled) {
                boolean authResult = dbRef.authenticate(username, password.toCharArray());
                if (!authResult) {
                    logger.error("Failed to authenticate user: " + username + " with password: " + password + ". Unable to write events.");
                    return;
                }
            }
            DBCollection collection = dbRef.getCollection(collectionName);
            for (DBObject doc : docs) {
                if (logger.isDebugEnabled()) {
                    logger.debug("doc: {}", doc);
                }
                DBObject query = BasicDBObjectBuilder.start()
                        .add(PK, doc.get(PK)).get();
                BasicDBObjectBuilder doc_builder = BasicDBObjectBuilder.start();
                if (doc.keySet().contains(OP_INC)) {
                    doc_builder.add(OP_INC, doc.get(OP_INC));
                }
                if (doc.keySet().contains(OP_SET)) {
                    doc_builder.add(OP_SET, doc.get(OP_SET));
                }
                if (doc.keySet().contains(OP_SET_ON_INSERT)) {
                    doc_builder.add(OP_SET_ON_INSERT, doc.get(OP_SET_ON_INSERT));
                }
                doc = doc_builder.get();
                //logger.debug("query: {}", query);
                //logger.debug("new doc: {}", doc);
                CommandResult result = collection.update(query, doc, true,
                        false, WriteConcern.SAFE).getLastError();
                if (result.ok()) {
                    String errorMessage = result.getErrorMessage();
                    if (errorMessage != null) {
                        logger.error("can't upsert documents with error: {} ",
                                errorMessage);
                        logger.error("with exception", result.getException());
                        throw new MongoException(errorMessage);
                    }
                } else {
                    logger.error("can't get last error");
                }
            }
        }
    }
*/

  /*  private void processEvent(Map<String, List<DBObject>> eventMap, Event event) {
        switch (model) {
            case SINGLE:
                putSingleEvent(eventMap, event);

                break;
            case DYNAMIC:
                putDynamicEvent(eventMap, event);

                break;
            default:
                logger.error("can't support model: {}, please check configuration.", model);
        }
    }*/

    private  void saveEventOneByOne(String jsonStr) throws Exception{

        Document doc = Document.parse(jsonStr);
        if(doc !=null){
            logger.info("insert  document is {}" ,doc);
            mongoCollection.insertOne(doc);
        }


    }

   /* private void putDynamicEvent(Map<String, List<DBObject>> eventMap, Event event) {
        String eventCollection;
        Map<String, String> headers = event.getHeaders();
        String eventDb = headers.get(DB_NAME);
        eventCollection = headers.get(COLLECTION);

        if (!StringUtils.isEmpty(eventDb)) {
            eventCollection = eventDb + NAMESPACE_SEPARATOR + eventCollection;
        } else {
            eventCollection = dbName + NAMESPACE_SEPARATOR + eventCollection;
        }

        if (!eventMap.containsKey(eventCollection)) {
            eventMap.put(eventCollection, new ArrayList<DBObject>());
        }

        List<DBObject> documents = eventMap.get(eventCollection);
        addEventToList(documents, event);
    }

    private void putSingleEvent(Map<String, List<DBObject>> eventMap, Event event) {
        String eventCollection;
        eventCollection = dbName + NAMESPACE_SEPARATOR + collectionName;
        if (!eventMap.containsKey(eventCollection)) {
            eventMap.put(eventCollection, new ArrayList<DBObject>());
        }

        List<DBObject> docs = eventMap.get(eventCollection);
        addEventToList(docs, event);
    }

    private List<DBObject> addEventToList(List<DBObject> documents, Event event) {
        if (documents == null) {
            documents = new ArrayList<DBObject>(batchSize);
        }

        DBObject eventJson;
        byte[] body = event.getBody();
        if (autoWrap) {
            eventJson = new BasicDBObject(wrapField, new String(body));
        } else {
            try {
                eventJson = (DBObject) JSON.parse(new String(body));
            } catch (Exception e) {
                logger.error("Can't parse events: " + new String(body), e);
                return documents;
            }
        }
        if (!event.getHeaders().containsKey(OPERATION) && timestampField != null) {
            Date timestamp = new Date();
            if (eventJson.containsField(TIMESTAMP_FIELD)) {
                try {
                    String dateText = (String) eventJson.get(TIMESTAMP_FIELD);
                    //timestamp = dateTimeFormatter.parseDateTime(dateText).toDate();
                    eventJson.removeField(TIMESTAMP_FIELD);
                } catch (Exception e) {
                    logger.error("can't parse date ", e);

                    timestamp = new Date();
                }
            } else {
                timestamp = new Date();
            }
            eventJson.put(TIMESTAMP_FIELD, timestamp);
        }

        for(Map.Entry<String, String> entry : extraInfos.entrySet()) {
            eventJson.put(entry.getKey(), entry.getValue());
        }

        documents.add(eventJson);

        return documents;
    }



    public enum CollectionModel {
        DYNAMIC, SINGLE
    }*/
}
