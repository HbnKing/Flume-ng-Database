package com.hbn.mongo.common;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.hbn.mongo.sink.MongoSinkConstants.DATABASE;
import static com.hbn.mongo.sink.MongoSinkConstants.PASSWORD;
import static com.hbn.mongo.sink.MongoSinkConstants.USER;

/**
 * @author wangheng
 * @create 2019-03-14 下午8:52
 * @desc
 *
 * MongoDB 数据库的工具类
 * 创建 关闭  连接  等操作
 *
 **/
public class MongodbConfig {


    public static String filePath;

    /**
     * 解析 host String 的 一个方法
     * 传入 如下 字符串
     * 192.168.3.1:27017,192.168.3.2:27017
     * @param seedsString
     *
     * @return
     */
    private List<ServerAddress> getSeeds(String seedsString) {
        List<ServerAddress> seeds = new LinkedList<ServerAddress>();
        String[] seedStrings = StringUtils.deleteWhitespace(seedsString).split(",");
        for (String seed : seedStrings) {
            String[] hostAndPort = seed.split(":");
            String host = hostAndPort[0];
            int port;
            if (hostAndPort.length == 2) {
                port = Integer.parseInt(hostAndPort[1]);
            } else {
                port = 27017;
            }
            seeds.add(new ServerAddress(host, port));
        }

        return seeds;
    }


    private String getCurrentTime(){
        Date dt = new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CurrentTime = matter1.format(dt);

        return CurrentTime;
    }

    private MongoCredential getCredential(Context context) {
        String user = context.getString(USER);
        String database = context.getString(DATABASE);
        String password = context.getString(PASSWORD);
        return MongoCredential.createCredential(user, database,
                password.toCharArray());
    }
}
