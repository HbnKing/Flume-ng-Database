package hbn;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangheng
 * @date 2019-03-27 下午8:24
 * @description
 **/
public class TestJson {


    public static void main(String[] args) {

        JSONObject  jsonObject =new JSONObject();
        System.out.println(jsonObject.size());
        jsonObject.put("name","wh");
        System.out.println(jsonObject.size());
        System.out.println(jsonObject.toString());
    }


}
