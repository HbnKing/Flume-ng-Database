package com.hbn.rdb.source;

import com.alibaba.fastjson.JSONObject;
import com.hbn.rdb.metrics.SqlSourceCounter;
import com.hbn.rdb.page.PageableResultSet;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLSource extends AbstractSource implements Configurable, PollableSource {
    private static final Logger logger = LoggerFactory.getLogger(SQLSource.class);

    //currentIndex  保存当前最大值
    private static  volatile  long  currentIndex  = Long.MIN_VALUE ;
    //自增字段 名称
    private static  volatile  String autoIncrementField = null ;
    //sqlsource helper
    private SQLSourceHelper sqlSourceHelper;
    //sqlSourceCounter
    private SqlSourceCounter sqlSourceCounter  ;

    @Override
    public long getBackOffSleepIncrement() {
        return 100;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 1000;
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
        /* get  currentIndex */
        currentIndex = sqlSourceHelper.getCurrentIndex();
        /* get  IncrementField */
        autoIncrementField = sqlSourceHelper.getautoIncrementField();

    	/* Initialize metric counters */
        sqlSourceCounter = new SqlSourceCounter(this.getName());

        /* Establish connection with database ,done  in sqlSourceHelper */
    }
    
    /**
     *
     * Process a batch of events performing SQL Queries
     *
     *
     */
	@Override
	public Status process() throws EventDeliveryException {
        Status status = null;

        logger.info("started  process ");

        try {
            /*start  sqlSourceCounter*/
            sqlSourceCounter.startProcess();
            // This try clause includes whatever Channel/Event operations you want to do
            PageableResultSet pageableResultSet = sqlSourceHelper.getNextPageableResultSet();


            //  send  batch  or  send  One  by  One  welcome  to  contribute

            ResultSetMetaData metaData = pageableResultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int end = pageableResultSet.getPageRowsCount();


            logger.info("column  count is {}",columnCount);
            logger.info("page row count is  {}",end);

            Event event = null ;
            JSONObject jsonObj = null ;

            long  id = 0L ;
            int counter = 0 ;
            for(int rowNum = 0 ; rowNum < end ; rowNum++){

                // Receive new data
                event = new SimpleEvent();
                jsonObj = new JSONObject();

                // 遍历每一列 的值 获取 出来  封装到一个 jsonObj 中
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = pageableResultSet.getObject(columnName);
                    //将 columnName  和 value  放置在 json  中
                    //jsonObj.put(columnName, value);
                    if(jsonObj.containsKey(columnName)){
                        jsonObj.put(columnName+"_"+i,value);
                    }else {
                        jsonObj.put(columnName,value);
                    }
                }

                if(!jsonObj.isEmpty()){
                    //一次完成之后是该行生成的一个的  一个json 文件
                    String  str = jsonObj.toJSONString();
                    event.setBody(str.getBytes(Charset.forName("UTF-8")));


                    this.getChannelProcessor().processEvent(event);

                    //记录当前id
                    id = jsonObj.getLongValue(autoIncrementField);

                    counter++;
                }

                pageableResultSet.next();

            }


            //修改当前指针使其自增
            if(id>0){
                currentIndex = id ;
            }else {
                currentIndex += counter;
            }

            logger.info("send  event size by  source  is {}",counter);

            /*counter*/
            sqlSourceCounter.incrementEventCount(counter);
            sqlSourceCounter.endProcess(counter);

        } catch (Throwable t) {
            // Log exception, handle individual exceptions as needed
            status = Status.BACKOFF;
            // re-throw all Errors
            if (t instanceof Error) {
                throw (Error) t;
            }
            // 出现异常时候 重新获取数据 这个时候 页面数 要 恢复原来值
            sqlSourceHelper.resetCurPage();

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

        sqlSourceCounter.start();
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
        sqlSourceCounter.stop();
        super.stop();

    }


    /**
     *
     * @param pageableResultSet
     * @return
     * @throws SQLException
     * 逐个 发送
     * 1.当前发送的内容
     * 2.当前发送的页面 pagenum  rownum
     * 3.当前发送的index
     * 4.每条记录的事务性
     *
     * 未完待续
     *
     */
    private  Status  sendEventOneByOne(PageableResultSet pageableResultSet) throws SQLException {

        Status status = null;
        Event event = null ;
        JSONObject jsonObj = null ;

        ResultSetMetaData metaData = pageableResultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        int end = pageableResultSet.getPageRowsCount();



        for(int rowNum = 0 ; rowNum < end ; rowNum++){

            // Receive new data
            event = new SimpleEvent();
            jsonObj = new JSONObject();

            // 遍历每一列 的值 获取 出来  封装到一个 jsonObj 中

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = pageableResultSet.getString(columnName);
                //将 columnName  和 value  放置在 json  中
                jsonObj.put(columnName, value);
            }

            if(jsonObj.size() >0){
                //一次完成之后是该行生成的一个的  一个json 文件
                event.setBody(jsonObj.toString().getBytes(Charset.forName("UTF-8")));
                //logger.info(jsonObj.toJSONString());
                //logger.info(new String( event.getBody(),Charset.forName("UTF-8")));

                //单个发送  或者批次发送？这个可以在考量一下
                //以及 增量算法 都还有优化空间
                this.getChannelProcessor().processEvent(event);
                /*counter*/
                sqlSourceCounter.incrementEventCount(1);

                //修改当前指针使其自增
                currentIndex = jsonObj.getLong(autoIncrementField) ==null ? currentIndex++ : Math.max(jsonObj.getLong(autoIncrementField),currentIndex);

            }





            pageableResultSet.next();

        }
        status = Status.READY;
        return status ;
    }

    /**
     *
     * @param pageableResultSet
     * @return
     * @throws SQLException
     *
     * 一个 批量插入 的方法
     */
    private  Status  sendEventBatch(PageableResultSet pageableResultSet) throws SQLException {


        Status status = Status.BACKOFF;

        ResultSetMetaData metaData = pageableResultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        int end = pageableResultSet.getPageRowsCount();

        Event event = null ;
        JSONObject jsonObj = null ;

        long  id = 0L ;



        List<Event> events = new ArrayList<Event>(sqlSourceHelper.getBatchsize());
        for(int rowNum = 0 ; rowNum < end ; rowNum++){

            // Receive new data
            event = new SimpleEvent();
            jsonObj = new JSONObject();

            // 遍历每一列 的值 获取 出来  封装到一个 jsonObj 中
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = pageableResultSet.getString(columnName);
                //将 columnName  和 value  放置在 json  中
                jsonObj.put(columnName, value);
            }

            if(jsonObj.size()>0){
                //一次完成之后是该行生成的一个的  一个json 文件
                String  str = jsonObj.toJSONString();
                event.setBody(str.getBytes(Charset.forName("UTF-8")));

                //logger.info(" send event is {} ",str);
                //这里 封装到 list 里面 再批量发送
                events.add(event);

                //记录当前id
                id = jsonObj.getLongValue(autoIncrementField);
            }

            pageableResultSet.next();

        }

        int  eventssize = events.size();
        //修改当前指针使其自增
        if(id>0){
            currentIndex = id ;
        }else {
            currentIndex += eventssize;
        }

        if(eventssize>0){

            /*batch  events send  to channel  */
            this.getChannelProcessor().processEventBatch(events);
            //events.clear();

            logger.info("send  event size by  source  is {}",eventssize);
        }

        /*counter*/
        sqlSourceCounter.incrementEventCount(eventssize);
        sqlSourceCounter.endProcess(eventssize);

        status = Status.READY;

        return  status ;
    }



    private  Status  sendEvent(PageableResultSet pageableResultSet) throws SQLException {


        Status status = Status.BACKOFF;

        ResultSetMetaData metaData = pageableResultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        int end = pageableResultSet.getPageRowsCount();

        Event event = null ;
        JSONObject jsonObj = null ;

        long  id = 0L ;

        int counter = 0 ;



        List<Event> events = new ArrayList<Event>(sqlSourceHelper.getBatchsize());
        for(int rowNum = 0 ; rowNum < end ; rowNum++){

            // Receive new data
            event = new SimpleEvent();
            jsonObj = new JSONObject();

            // 遍历每一列 的值 获取 出来  封装到一个 jsonObj 中
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = pageableResultSet.getString(columnName);
                //将 columnName  和 value  放置在 json  中
                jsonObj.put(columnName, value);
            }

            if(jsonObj.size()>0){
                //一次完成之后是该行生成的一个的  一个json 文件
                String  str = jsonObj.toJSONString();
                event.setBody(str.getBytes(Charset.forName("UTF-8")));

                //logger.info(" send event is {} ",str);
                //这里 封装到 list 里面 再批量发送
                //events.add(event);
                getChannelProcessor().processEvent(event);
                counter++;

                //记录当前id
                id = jsonObj.getLongValue(autoIncrementField);
            }

            pageableResultSet.next();

        }





        /*counter*/
        sqlSourceCounter.incrementEventCount(counter);
        sqlSourceCounter.endProcess(counter);

        status = Status.READY;

        return  status ;
    }
    

}
