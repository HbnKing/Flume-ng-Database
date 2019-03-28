package com.hbn.rdb.sink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangheng
 * @create 2019-03-22 下午10:51
 * @desc
 **/
public class OracleSink extends AbstractSink implements Configurable {

    private Logger logger = LoggerFactory.getLogger(OracleSink.class);
    private String driverClassName;
    private String url;
    private String user;
    private String password;
    private PreparedStatement preparedStatement;
    private Connection conn;
    private int batchSize;
    private String regex;
    private String sql;
    public OracleSink() {
        logger.info("---start---");
    }

    /*与 列 相关的一些属性 列的数量 列的名字 */
    private String[] columns ;
    private int columsNum ;

    private SQLSinkHelper sqlSinkHelper ;


    @Override
    public void configure(Context context) {
        url = context.getString("url");
        logger.info(url + "---url---");
        Preconditions.checkNotNull(url, "url must be set!!");
        driverClassName = context.getString("driverClassName");
        logger.info(driverClassName + "---driverClassName---");
        Preconditions.checkNotNull(driverClassName, "driverClassName must be set!!");
        user = context.getString("user");
        logger.info(user + "---user---");
        Preconditions.checkNotNull(user, "user must be set!!");
        password = context.getString("password");
        logger.info(password + "---password---");
        Preconditions.checkNotNull(password, "password must be set!!");
        regex = context.getString("regex");
        logger.info(regex + "---regex---");
        Preconditions.checkNotNull(regex, "regex must be set!!");
        sql = context.getString("sql");
        logger.info(sql + "---sql---");
        Preconditions.checkNotNull(sql, "sql must be set!!");
        batchSize = context.getInteger("batchSize", 100);
        logger.info(batchSize + "---batchSize---");
        Preconditions.checkNotNull(batchSize > 0, "batchSize must be a positive number!!");



    }

    @Override
    public void start() {
        super.start();
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        String url = "jdbc:oracle:thin:@" + hostname + ":" + port + ":" + databaseName;
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            //创建一个Statement对象
            preparedStatement = conn.prepareStatement(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void stop() {
        logger.info("----执行结束---");
        super.stop();
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event;

        List<JSONObject> infos = new ArrayList<JSONObject>();
        transaction.begin();
        try {
            for (int i = 0; i < batchSize; i++) {
                event = channel.take();
                if (event != null) {//对事件进行处理
                    logger.info("---读取数据---");
                    logger.info("--数据库连接是否关闭--" + conn.isClosed());
                    //获取 数据内容
                    String content = new String(event.getBody());

                    if(content != null && content != ""){

                        JSONObject jsonObject = JSON.parseObject(content);
                        infos.add(jsonObject);
                    }
                } else {
                    result = Status.BACKOFF;
                    break;
                }
            }

            if (infos.size() > 0) {
                logger.info("infos的长度==" + infos.size());
                preparedStatement.clearBatch();

                for (JSONObject temp : infos) {
                    //设置 每一列的 值
                    for(int col = 1 ;col<=columsNum ;col ++){
                        Object  value = temp.get(columns[col-1]);
                        preparedStatement.setObject(col,value);
                    }
                    preparedStatement.addBatch();
                }
                try{
                    preparedStatement.executeBatch();
                }catch(SQLException e){
                    logger.error("------批量执行sql错误");
                    e.printStackTrace();
                }


                conn.commit();
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                transaction.rollback();
            } catch (Exception e2) {
                logger.error("Exception in rollback. Rollback might not have been" +
                        "successful.", e2);
            }
            logger.error("Failed to commit transaction." +
                    "Transaction rolled back.", e);
            Throwables.propagate(e);
        } finally {
            transaction.close();
        }
        return result;
    }

}