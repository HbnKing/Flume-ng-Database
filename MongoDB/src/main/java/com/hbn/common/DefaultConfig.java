package com.hbn.common;

import com.hbn.mongo.sink.MongoSink;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangheng
 * @create 2019-01-11 下午5:29
 * @desc
 * 如果配置文件里没有 设置的话
 * mongodb 读取的默认设置
 *
 *
 *
 **/
public class DefaultConfig {
    private static Logger logger = LoggerFactory.getLogger(MongoSink.class);

    private static DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("yyyy-MM-dd").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssz").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz").getParser(),
    };
    public static DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();



    //默认不开启 权限认证
    public static final boolean DEFAULT_AUTHENTICATION_ENABLED = false;
    //默认hostname  localhost 端口 不带 的话是 27017
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 27017;
    //默认数据库名字 和 collectionname
    public static final String DEFAULT_DB = "flume";
    public static final String DEFAULT_COLLECTION = "mongosink";

    //批插入 每批 插入处理数据量
    public static final int DEFAULT_BATCH = 100;

    private static final Boolean DEFAULT_AUTO_WRAP = false;
    public static final String DEFAULT_WRAP_FIELD = "log";
    public static final String DEFAULT_TIMESTAMP_FIELD = null;
    public static final char NAMESPACE_SEPARATOR = '.';
    public static final String OP_UPSERT = "upsert";
    public static final String EXTRA_FIELDS_PREFIX = "extraFields.";
    /**
     * The default batch size.
     */
    private static  int DEFAULT_BATCH_SIZE = 100;
}
