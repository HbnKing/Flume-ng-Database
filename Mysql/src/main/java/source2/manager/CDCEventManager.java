package source2.manager;

import source2.CDCEvent;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author wangheng
 * @create 2019-02-17 下午4:23
 * @desc
 **/
public class CDCEventManager {
    public static final ConcurrentLinkedDeque<CDCEvent> queue = new ConcurrentLinkedDeque<>();
}