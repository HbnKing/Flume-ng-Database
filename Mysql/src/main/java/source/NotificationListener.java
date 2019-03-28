/*
package source;


import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.impl.event.AbstractRowEvent;
import com.google.code.or.binlog.impl.event.DeleteRowsEvent;
import com.google.code.or.binlog.impl.event.UpdateRowsEvent;
import com.google.code.or.binlog.impl.event.WriteRowsEvent;
import com.google.code.or.common.glossary.Column;
import com.google.code.or.common.glossary.Pair;
import com.google.code.or.common.glossary.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * @author wangheng
 * @create 2019-02-16 下午5:57
 * @desc
 **//*

public class NotificationListener implements BinlogEventListener {

    private static Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private String host="xx.xx.xx.xx";
    private Integer port=3306;
    private String username="root";
    private String password="root";

    public void onEvents(BinlogEventV4 event) {
        if(event==null){
            logger.error("binlog event is null");
            return;
        }

        if(event instanceof UpdateRowsEvent){
            UpdateRowsEvent updateRowsEvent = (UpdateRowsEvent)event;
            LogEvent logEvent = new LogEvent(updateRowsEvent);

            List<Pair<Row>> rows = updateRowsEvent.getRows();
            List<Column> cols_after = null;
            List<Column> cols_before = null;
            for(Pair<Row> p : rows){
                Row after = p.getAfter();
                Row before = p.getBefore();
                cols_after = after.getColumns();

                cols_before = before.getColumns();
                break;
            }
            logEvent.setBefore(getMap(cols_before, updateRowsEvent.getTme().getDatabaseName().toString(), updateRowsEvent.getTme().getTableName().toString()));
            logEvent.setAfter(getMap(cols_after, updateRowsEvent.getTme().getDatabaseName().toString(), updateRowsEvent.getTme().getTableName().toString()));
            logger.info("update event is:"+logEvent);
        }else if(event instanceof DeleteRowsEvent){
            DeleteRowsEvent deleteRowsEvent = (DeleteRowsEvent)event;
            LogEvent logEvent = new LogEvent(deleteRowsEvent);
            List<Row> rows = deleteRowsEvent.getRows();
            List<Column> before = null;
            for(Row row:rows){
                before = row.getColumns();
                break;
            }
            logEvent.setBefore(getMap(before, deleteRowsEvent.getTme().getDatabaseName().toString(), deleteRowsEvent.getTme().getTableName().toString()));
            logger.info("delete event is:"+logEvent);

        }else if(event instanceof WriteRowsEvent){
            WriteRowsEvent wrtiteRowsEvent = (WriteRowsEvent)event;
            LogEvent logEvent = new LogEvent(wrtiteRowsEvent);
            List<Row> rows = wrtiteRowsEvent.getRows();
            List<Column> before = null;
            for(Row row:rows){
                before = row.getColumns();
                break;
            }
            logEvent.setAfter(getMap(before, wrtiteRowsEvent.getTme().getDatabaseName().toString(), wrtiteRowsEvent.getTme().getTableName().toString()));
            logger.info("write event is:"+logEvent);

        }
    }

    private Map<String, String> getMap(List<Column> cols, String databaseName, String tableName){
        if(cols==null||cols.size()==0){
            return null;
        }
        List<String> columnNames = new TableInfo(host,username,password, port).getColumns(databaseName, tableName);
        if(columnNames==null){
            return null;
        }
        if(columnNames.size()!=cols.size()){
            logger.error("the size does not match...");
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        for(int i=0;i<columnNames.size();i++){
            if(cols.get(i).getValue()==null){
                map.put(columnNames.get(i).toString(),"");
            }else{
                map.put(columnNames.get(i).toString(),cols.get(i).toString());
            }

        }
        return map;
    }
*/
