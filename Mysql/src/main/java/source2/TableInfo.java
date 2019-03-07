package source2;

/**
 * @author wangheng
 * @create 2019-02-17 下午4:13
 * @desc
 **/
public class TableInfo {

    private String databaseName;
    private String tableName;
    private String fullName;
    // 省略Getter和Setter

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || this.getClass()!=o.getClass())
            return false;
        TableInfo tableInfo = (TableInfo)o;
        if(!this.databaseName.equals(tableInfo.getDatabaseName()))
            return false;
        if(!this.tableName.equals(tableInfo.getTableName()))
            return false;
        if(!this.fullName.equals(tableInfo.getFullName()))
            return false;
        return true;
    }

    @Override
    public int hashCode(){
        int result = this.tableName.hashCode();
        result = 31*result+this.databaseName.hashCode();
        result = 31*result+this.fullName.hashCode();
        return result;
    }
}
    