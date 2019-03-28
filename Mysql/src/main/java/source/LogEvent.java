/*
package source;

import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.binlog.impl.event.AbstractRowEvent;
import com.google.code.or.binlog.impl.event.QueryEvent;
import com.google.code.or.binlog.impl.event.TableMapEvent;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

*/
/**
 * @author wangheng
 * @create 2019-02-16 下午5:32
 * @desc
 **//*

public class LogEvent implements Serializable {

    */
/**
     * 只针对delete、insert、update事件
     *//*

    private static final long serialVersionUID = 5503152746318421290L;

    private String eventId = null;
    private String databaseName = null;
    private String tableName = null;
    private String eventType = null;
    private Long timestamp = null;
    private Long timestampRecepite = null;
    private String binlogName = null;
    private Long position = null;
    private Long nextPosition = null;
    private Long serverId = null;
    private Map<String, String> before = null;
    private Map<String, String> after = null;

    public LogEvent() {

    }

    public LogEvent(final QueryEvent qe, String databaseName, String tableName) {
        this.init(qe);
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    public LogEvent(final AbstractRowEvent re) {
        this.init(re);
        TableMapEvent tableMapEvent = re.*/
/*getTme*//*
();
        this.databaseName = tableMapEvent.getDatabaseName().toString();
        this.tableName = tableMapEvent.getTableName().toString();
    }

    private void init(final BinlogEventV4 be) {
        this.eventId = UUID.randomUUID().toString();
        BinlogEventV4Header header = be.getHeader();
        this.binlogName = header.getBinlogName();
        this.position = header.getPosition();
        this.nextPosition = header.getNextPosition();
        this.timestamp = header.getTimestamp();
        this.timestampRecepite = header.getTimestampOfReceipt();
        this.serverId = header.getServerId();
        this.eventType = MySqlEventTypeIdToString.getInstance().get(header.getEventType());
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ eventId:").append(eventId);
        builder.append(",databaseName:").append(databaseName);
        builder.append(",tableName:").append(tableName);
        builder.append(",eventType:").append(eventType);
        builder.append(",timestamp:").append(timestamp);
        builder.append(",timestampRecepite:").append(timestampRecepite);
        builder.append(",binlogName:").append(binlogName);
        builder.append(",position:").append(position);
        builder.append(",nextPosition:").append(nextPosition);
        builder.append(",serverId:").append(serverId);
        builder.append(",before:").append(before);
        builder.append(",after:").append(after).append(" }");
        return builder.toString();
    }


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestampRecepite() {
        return timestampRecepite;
    }

    public void setTimestampRecepite(Long timestampRecepite) {
        this.timestampRecepite = timestampRecepite;
    }

    public String getBinlogName() {
        return binlogName;
    }

    public void setBinlogName(String binlogName) {
        this.binlogName = binlogName;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Long getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Long nextPosition) {
        this.nextPosition = nextPosition;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Map<String, String> getBefore() {
        return before;
    }

    public void setBefore(Map<String, String> before) {
        this.before = before;
    }

    public Map<String, String> getAfter() {
        return after;
    }

    public void setAfter(Map<String, String> after) {
        this.after = after;
    }

}*/
