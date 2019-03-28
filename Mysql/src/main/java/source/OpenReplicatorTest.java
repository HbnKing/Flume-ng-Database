package source;

import com.google.code.or.OpenReplicator;


/**
 * @author wangheng
 * @create 2019-02-16 下午5:56
 * @desc
 **/
public class OpenReplicatorTest {
    public static void main(String args[]) throws Exception {
        final OpenReplicator or = new OpenReplicator();
        or.setUser("root");
        or.setPassword("root");
        or.setHost("xx.xxx.xx.xx");
        or.setPort(3306);
        or.setServerId(23);
        or.setBinlogPosition(106);
        or.setBinlogFileName("mysql-bin.000001");

        //or.setBinlogEventListener();
        or.start();
    }
}

