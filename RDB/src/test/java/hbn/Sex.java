package hbn;

/**
 * @author wangheng
 * @create 2019-04-04 上午11:19
 * @desc
 **/
public enum Sex {
    MAN('m'),
    WOMAN('w');

    private char value;
    Sex(char value) {
        this.value = value;
    }
    public char getValue() {
        return value;
    }
}
