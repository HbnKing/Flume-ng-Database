package hbn;

import com.hbn.rdb.common.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangheng
 * @create 2019-01-25 下午2:04
 * @desc
 **/
public class Test {

    private static  Logger  logger  = LoggerFactory.getLogger(Test.class);




    public static void main(String[] args) {

        String str = "111";
        System.out.println(str);

        logger.info("started ");
        logger.info("内容是  {} --",str);





        ///------
        FileStatus  fileStatus = new FileStatus();
        //FileStatus.getProperties();
        String filepath = "./src";
        String filename = "ab";
        FileStatus.createStatusFile(filepath,filename);
        System.out.println("_______");


        FileStatus.updateProperties(filepath,filename,12L);
        System.out.println(FileStatus.getProperties(filepath,filename));
    }
}
