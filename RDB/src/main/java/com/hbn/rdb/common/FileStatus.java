package com.hbn.rdb.common;

import org.apache.velocity.texen.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author wangheng
 * @create 2019-01-24 下午1:58
 * @desc
 *
 * 读取和 保存 中间文件的状态
 * 创建  文件
 * 只在 启动  读取
 * 关闭 时 写入文件
 * 其余时间的计算 在内存中进行
 *
 *
 **/
public class FileStatus {
    private static Logger  logger = LoggerFactory.getLogger(FileStatus. class);
    private static String  currentIndex ="currentIndex";
    private static String MIN = String.valueOf(Long.MIN_VALUE) ;

    private static  boolean isFileCreated(File  file) {

        return file.exists() && !file.isDirectory() ? true : false;
    }
    /**
     *
     * Create status file
     *
     */
    public static void createStatusFile( String filePath,String fileName) {
        File  file = new File(filePath,fileName);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }




    public static Long  getProperties(String  filePath,String fileName){

        String  value = MIN ;
        File  file = new File(filePath ,fileName);

        if(file.exists() && !file.isDirectory()){

            FileInputStream in = null;
            try{
                Properties properties = new Properties();
                in = new FileInputStream(file);
                properties.load(in);
                value = properties.getProperty(currentIndex,MIN);
                logger.info("read  properties ok ！");
                logger.info("currentIndex  is " +value);
            }catch(Exception e){
                e.printStackTrace();
                logger.error("read  properties failed");
            }finally{
                if(in != null){
                    try{
                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }else {
            createStatusFile(filePath,fileName);
        }

        return  Long.parseLong(value) ;

    }


    /**
     * 传递键值对的Map，更新properties文件
     *
     * @param filePath
     *            文件名(放在resource源包目录下)，需要后缀
     *
     */
    public static void updateProperties(String filePath, String fileName ,Long  value) {

        Map<String ,String>  keyValueMap = new HashMap();
        keyValueMap.put(currentIndex,String.valueOf(value));


        File file = new File(filePath,fileName);
        logger.info("propertiesPath:  " + filePath);
        Properties props = new Properties();
        //BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            // 从输入流中读取属性列表（键和元素对）
            //br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            //props.load(br);
            //br.close();

            // 写入属性文件
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            props.clear();// 清空旧的文件
            for (String keys : keyValueMap.keySet())
                props.setProperty(keys, keyValueMap.get(keys));
            props.store(bw, "");
            bw.close();
        } catch (IOException e) {
            logger.error("Visit " + filePath + fileName+ " for updating " + ""+ " value error");
        } finally {
            try {
                //br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
