package source2.model;

/**
 * @author wangheng
 * @create 2019-02-17 下午4:19
 * @desc
 **/
public class BinlogMasterStatus {
    public String getBinlogName() {
        return binlogName;
    }

    public void setBinlogName(String binlogName) {
        this.binlogName = binlogName;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    private String binlogName;
    private long position;
    // 省略Getter和Setter
}
