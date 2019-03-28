package source;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangheng
 * @create 2019-02-16 下午5:59
 * @desc
 **/
public class MySqlEventTypeIdToString {
    public static MySqlEventTypeIdToString mySqlEventTypeIdToString = new MySqlEventTypeIdToString() ;
    private static Map<Integer, String> idToString = new HashMap<Integer, String>();
    private MySqlEventTypeIdToString() {
        Init();
    }
    public static MySqlEventTypeIdToString getInstance() {
        return mySqlEventTypeIdToString ;
    }
    private void Init() {
        idToString.put(0,"UNKNOWN_EVENT");
        idToString.put(1,"START_EVENT_V3");
        idToString.put(2,"QUERY_EVENT");
        idToString.put(3,"STOP_EVENT");
        idToString.put(4,"ROTATE_EVENT");
        idToString.put(5,"INTVAR_EVENT");
        idToString.put(6,"LOAD_EVENT");
        idToString.put(7,"SLAVE_EVENT");
        idToString.put(8,"CREATE_FILE_EVENT");
        idToString.put(9,"APPEND_BLOCK_EVENT");
        idToString.put(10,"EXEC_LOAD_EVENT");
        idToString.put(11,"DELETE_FILE_EVENT");
        idToString.put(12,"NEW_LOAD_EVENT");
        idToString.put(13,"RAND_EVENT");
        idToString.put(14,"USER_VAR_EVENT");
        idToString.put(15,"FORMAT_DESCRIPTION_EVENT");
        idToString.put(16,"XID_EVENT");
        idToString.put(17,"BEGIN_LOAD_QUERY_EVENT");
        idToString.put(18,"EXECUTE_LOAD_QUERY_EVENT");
        idToString.put(19,"TABLE_MAP_EVENT");
        idToString.put(20,"PRE_GA_WRITE_ROWS_EVENT");
        idToString.put(21,"PRE_GA_UPDATE_ROWS_EVENT");
        idToString.put(22,"PRE_GA_DELETE_ROWS_EVENT");
        idToString.put(23,"WRITE_ROWS_EVENT");
        idToString.put(24,"UPDATE_ROWS_EVENT");
        idToString.put(25,"DELETE_ROWS_EVENT");
        idToString.put(26,"INCIDENT_EVENT");
        idToString.put(27,"HEARTBEAT_LOG_EVENT");
        idToString.put(28,"IGNORABLE_LOG_EVENT");
        idToString.put(29,"ROWS_QUERY_LOG_EVENT");
        idToString.put(30,"WRITE_ROWS_EVENT_V2");
        idToString.put(31,"UPDATE_ROWS_EVENT_V2");
        idToString.put(32,"DELETE_ROWS_EVENT_V2");
        idToString.put(33,"GTID_LOG_EVENT");
        idToString.put(34,"ANONYMOUS_GTID_LOG_EVENT");
        idToString.put(35,"PREVIOUS_GTIDS_LOG_EVENT");
    }
    public String get(Integer eventId) {
        return idToString.get(eventId);
    }
}

