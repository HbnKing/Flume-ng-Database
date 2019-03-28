package com.hbn.mongo.source;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MongoStreamSource extends AbstractSource implements Configurable, PollableSource {

    //currentIndex  保存当前最大值
    private  static  volatile  long  currentIndex  = Long.MIN_VALUE ;
    private static volatile String autoIncrementField = null ;

    private static final Logger logger = LoggerFactory.getLogger(MongoStreamSource.class);
    private MongoSourceHelper MongoSourceHelper;


    MongoCollection<Document> collection =  null ;
    //private static AtomicLong resultsize = new AtomicLong(0);

    @Override
    public long getBackOffSleepIncrement() {
        return 10;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 100;
    }


    /**
     * Configure the source, load configuration properties and establish connection with database
     */
    @Override
    public void configure(Context context) {

        logger.info("started configure() ");

        logger.info("Reading and processing configuration values for source " + getName());
		
    	/* Initialize configuration parameters */
    	MongoSourceHelper = new MongoSourceHelper(context, this.getName());

    	//driverclass = context.getString("driver_class") ;
        currentIndex = MongoSourceHelper.getCurrentIndex();

        autoIncrementField = MongoSourceHelper.getautoIncrementField();

    	/* Initialize metric counters */

        /* Establish connection with database */


    }
    
    /**
     * Process a batch of events performing SQL Queries
     */
	@Override
	public Status process() throws EventDeliveryException {
        Status status = null;
        // This try clause includes whatever Channel/Event operations you want to do
        ChangeStreamDocument<Document> result = MongoSourceHelper.getnext();
        if(result !=null){
            Event event = new SimpleEvent();
            event.setBody(result.getFullDocument().toJson().getBytes(Charset.forName("UTF-8")));
            logger.info(new String( event.getBody(),Charset.forName("UTF-8")));
            this.getChannelProcessor().processEvent(event);
            return status.READY ;
        }
        return  null ;


    }
 
	/**
	 * Starts the source. Starts the metrics counter.
	 */
	@Override
    public void start() {
        MongoSourceHelper.initDriver();
        logger.info("Starting sql source {} ...", getName());

        super.start();
    }

	/**
	 * Stop the source. Close database connection and stop metrics counter.
     *
	 */
    @Override
    public void stop() {

        logger.info("Stopping sql source {} ...", getName());
        /**
         * 当关闭的时候还应该修改上次index  值
         *
         */
        MongoSourceHelper.updateStatusFile(currentIndex);
        MongoSourceHelper.stop();

    }
    
    private class ChannelWriter extends Writer{
        private List<Event> events = new ArrayList<>();

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            Event event = new SimpleEvent();
            
            String s = new String(cbuf);
            //event.setBody(s.substring(off, len-1).getBytes(Charset.forName(MongoSourceHelper.getDefaultCharsetResultSet())));
            
            Map<String, String> headers;
            headers = new HashMap<String, String>();
			headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
			event.setHeaders(headers);
			
            events.add(event);

            flush();
        }

        @Override
        public void flush() throws IOException {
            getChannelProcessor().processEventBatch(events);
            events.clear();
        }

        @Override
        public void close() throws IOException {
            flush();
        }
    }
}
