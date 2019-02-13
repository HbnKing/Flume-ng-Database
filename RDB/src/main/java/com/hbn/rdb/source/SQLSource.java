package com.hbn.rdb.source;

import com.alibaba.fastjson.JSONObject;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class SQLSource extends AbstractSource implements Configurable, PollableSource {

    //currentIndex  保存当前最大值
    private  static  volatile  long  currentIndex  = Long.MIN_VALUE ;
    private static volatile String autoIncrementField = null ;

    private static final Logger logger = LoggerFactory.getLogger(SQLSource.class);
    private SQLSourceHelper sqlSourceHelper;


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
    	sqlSourceHelper = new SQLSourceHelper(context, this.getName());

    	//driverclass = context.getString("driver_class") ;
        currentIndex = sqlSourceHelper.getCurrentIndex();

        autoIncrementField = sqlSourceHelper.getautoIncrementField();

    	/* Initialize metric counters */

        /* Establish connection with database */


    }
    
    /**
     * Process a batch of events performing SQL Queries
     */
	@Override
	public Status process() throws EventDeliveryException {
        Status status = null;
        try {
            // This try clause includes whatever Channel/Event operations you want to do
            ResultSet result = sqlSourceHelper.executeQuery();
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (result.next()) {

                // Receive new data
                Event event = new SimpleEvent();
                JSONObject jsonObj = new JSONObject();

                // 遍历每一列 的值 获取 出来

                for (int i = 1; i <= columnCount; i++) {

                    String columnName = metaData.getColumnLabel(i);
                    String value = result.getString(columnName);
                    //将 columnName  和 value  放置在 json  中
                    jsonObj.put(columnName, value);

                }

                //一次完成之后是该行生成的一个的  一个json 文件
                event.setBody(jsonObj.toString().getBytes(Charset.forName("UTF-8")));

                //logger.info(jsonObj.toJSONString());
                logger.info(new String( event.getBody(),Charset.forName("UTF-8")));



                //单个发送  或者批次发送？这个可以在考量一下
                //以及 增量算法 都还有优化空间
                this.getChannelProcessor().processEvent(event);

                // Store the Event into this Source's associated Channel(s)
                //getChannelProcessor().processEvent(event);



                /*if(false){
                    //long  tmpIdValue = jsonObj.getLong(fieldName);
                    // currentIndex = Math.max(currentIndex,tmpIdValue);

                }else {
                    //不包含自增值
                    resultsize.incrementAndGet();
                }*/
                //currentIndex++;
                try {
                    currentIndex = Math.max(jsonObj.getLong(autoIncrementField),currentIndex);
                }catch (Exception e){
                    currentIndex++;
                }



            }
            //currentIndex = currentIndex
            //currentIndex = Math.max(currentIndex,currentIndex+resultsize.longValue());
            status = Status.READY;
        } catch (Throwable t) {
            // Log exception, handle individual exceptions as needed

            status = Status.BACKOFF;

            // re-throw all Errors
            if (t instanceof Error) {
                throw (Error) t;
            }
        } finally {
            //修改 index  使其自增
            sqlSourceHelper.setCurrentIndex(currentIndex);

        }
        return status;
    }
 
	/**
	 * Starts the source. Starts the metrics counter.
	 */
	@Override
    public void start() {
        sqlSourceHelper.initDriver();
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
        sqlSourceHelper.updateStatusFile(currentIndex);
        sqlSourceHelper.stop();

    }
    
    private class ChannelWriter extends Writer{
        private List<Event> events = new ArrayList<>();

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            Event event = new SimpleEvent();
            
            String s = new String(cbuf);
            //event.setBody(s.substring(off, len-1).getBytes(Charset.forName(sqlSourceHelper.getDefaultCharsetResultSet())));
            
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
