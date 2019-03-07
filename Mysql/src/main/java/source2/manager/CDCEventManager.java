package source2.manager;

/**
 * @author wangheng
 * @create 2019-02-17 下午4:23
 * @desc
 **/
public class CDCEventManager {
    public static final ConcurrentLinkedDeque<CDCEvent> queue = new ConcurrentLinkedDeque<>();
}