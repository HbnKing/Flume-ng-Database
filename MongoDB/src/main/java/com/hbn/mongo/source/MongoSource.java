package com.hbn.mongo.source;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;

/**
 * @author wangheng
 * @create 2019-01-09 下午7:29
 * @desc
 *
 *
 *
 **/
public class MongoSource  extends AbstractSource implements Configurable, PollableSource {

    @Override
    public Status process() throws EventDeliveryException {
        return null;
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    @Override
    public void configure(Context context) {

    }
}
