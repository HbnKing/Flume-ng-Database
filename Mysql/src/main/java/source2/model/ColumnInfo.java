package source2.model;

/**
 * @author wangheng
 * @create 2019-02-17 下午4:22
 * @desc
 **/
public class ColumnInfo {
    public ColumnInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String name;
    private String type;
    // 省略Getter和Setter
}
