package com.hbn.rdb.source;

import com.hbn.rdb.common.DefaultConfig;
import com.hbn.rdb.common.DriverQuery;
import com.hbn.rdb.common.FileStatus;
import com.hbn.rdb.common.RDBconfig;
import org.apache.flume.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;


public class SQLSourceHelper {

  private static final Logger logger = LoggerFactory.getLogger(SQLSourceHelper.class);

  //数据库 连接信息
  //没有的值 就设置为null
  private static String drivername = null ;
  private static String conectionurl = null ;
  private static String username = null ;
  private static String password = null ;
  //表库 级别 的相关信息
  //private static String dbname = null ;

  //个性化sql部分
  
  private static String customerquery = null ;

  private static long begin = 0l;
  //  select id,field   from  table name  where  id  >=begin ;
  //这里需要 查询列名  表明  自增列名 ,该列的起始值
  private static String columnsToSelect = null ;
  private static String table = null ;
  private static String autoIncrementField = null ;



  // 生成的sql
  private static String query = null ;


  //中间文件状态记录
  //文件位置 和文件名称
  private static String filePath = null ;
  private static String fileName = null ;



  //通用配置
  private static  String Charset = null ;
  //当前最大值 currentIndex
  //比较 来源 1  begin  2  filestatus 3  内存迭代更新
  //修改频繁要求较高  可以优化

  private static Long currentIndex ;

  //其他
  private String sourceName ;

  private Context context ;

  private Map<String, String> statusFileJsonMap = new LinkedHashMap<String, String>();

  //每一个sqlsource 内设置一个内部对象 保存该类的 数据库级别的连接信息

  com.hbn.rdb.common.RDBconfig RDBconfig = new RDBconfig();

  DriverQuery driverQuery = new DriverQuery() ;

  /**
   * Builds an SQLSourceHelper containing the configuration parameters and
   * usefull utils for SQL Source
   *
   * @param context    Flume source context, contains the properties from configuration file
   * @param sourceName source file name for store status
   *
   */
  public SQLSourceHelper(Context context, String sourceName) {
    logger.info("started to  init SQLSourceHelper");

    this.context = context;

    this.sourceName = sourceName;
    //起点
    begin = context.getLong(DefaultConfig.BEGINNING,0L);
    logger.info("begin  is  {}",begin );

    //自己写的sql
    customerquery = context.getString(DefaultConfig.CUSTOMQUERY);

    logger.info("customerquery  is  {}" ,customerquery);

    //多个参数配的sql
    table = context.getString(DefaultConfig.TABLE);
    columnsToSelect = context.getString(DefaultConfig.COLUMNS_TO_SELECT,DefaultConfig.DEFAULT_COLUMNS_TO_SELECT);
    autoIncrementField = context.getString(DefaultConfig.AUTOINCREMENTFIELD);

    logger.info("table  is  {}",table);
    logger.info("columnsToSelect  is  {}",columnsToSelect);
    logger.info("autoIncrementField  is  {}",autoIncrementField);


    Charset = context.getString(DefaultConfig.CHARSET_RESULTSET, DefaultConfig.DEFAULT_CHARSET_RESULTSET);
    logger.info("charset  is  {}",Charset);

    //query = buildQuery();
    /**
     * 数据库连接配置
     */

    drivername = context.getString(DefaultConfig.DRIVER);

    username = context.getString(DefaultConfig.USER);
    password = context.getString(DefaultConfig.PASSWORD);
    conectionurl = context.getString(DefaultConfig.URL);
    logger.info("connectionurl  is  {}",conectionurl);
    logger.info("driver  is  {}",drivername);



    //封装到 RDB
    RDBconfig.setDbpassword(password);
    RDBconfig.setDbuser(username);
    RDBconfig.setDriverclass(drivername);
    RDBconfig.setUrl(conectionurl);

    //对该对象初始化
    //查询的初始化
    //driverQuery.init(RDBconfig);

    //中间文件状态 资料
    filePath = context.getString(DefaultConfig.FILEPATH,DefaultConfig.DEFAULT_FILEPATH);
    fileName = context.getString(DefaultConfig.FILENAME,DefaultConfig.DEFAULT_FILENAME);

    logger.info("filePath is  {}",filePath);
    logger.info("fileName is  {}",fileName);


    //更新 currentIndex
    //比较 起点  和  当前值 的最大值
    currentIndex = Math.max(begin ,getCurrentIndexStatusFile());
    logger.info("Index  started  from  {}" ,currentIndex);

  }

  public String buildQuery() {

    if (customerquery == null) {
      // 如果 customerQuery 为null，就以 offsest 作为 id
      return "SELECT " + columnsToSelect + " FROM " + table +" where " +autoIncrementField + " > " +currentIndex ;
    } else {
      //如果 customerquery 不为 null，那么就要将其最后已存在的 offset 替换掉新查询出的 offset（！！！）
      //
      if (customerquery.contains("$@$")) {
        //直接修改  返回行新的 sql 语句
        return customerquery.replace("$@$", String.valueOf(currentIndex));
      } else {
        //如果没有设置
        // currentIndex  应该为 begin  默认值 Long.MIN_VALUE
        //还可以在修改一下
        /*if(currentIndex.equals(Long.MIN_VALUE)){
          return customerquery = customerquery.substring(0,customerquery.indexOf(">")+1)+currentIndex;
        }else return customerquery ;*/
        return customerquery ;

      }
    }
  }



  /**
   *
   * 返回内部对象
   *
   * 但是要注意 driverquery  内部的属性要 提前初始化
   * 不然返回时 空值
   *
   * @return
   */
  private DriverQuery getDriverQuery() {
    return driverQuery ;
  }

  public void  updateStatusFile(Long  CurrentIndex){

    FileStatus.updateProperties(filePath ,fileName, CurrentIndex);
  }
  public void  updateStatusFile(){
    FileStatus.updateProperties(filePath ,fileName, currentIndex);
  }


  public long  getCurrentIndexStatusFile(){
    return  FileStatus.getProperties(filePath ,fileName);
  }


  public long  getCurrentIndex(){
    return currentIndex ;
  }

  public void   setCurrentIndex( long CurrentIndex ){
    this.currentIndex = CurrentIndex ;
  }


  public String getautoIncrementField(){
    return autoIncrementField ;
  }

  public boolean  isSelectContains(){
    //没有设置自增字段
    if(autoIncrementField ==null){
      return false ;
    //包含
    }else if(customerquery!=null && (customerquery.contains("*")||customerquery.contains(autoIncrementField))){
      return true ;

    }else if(columnsToSelect!=null && (columnsToSelect.contains("*")||columnsToSelect.contains(autoIncrementField))){
      return true ;
    }else return false;


  }


  public void stop(){
    driverQuery.stop();
    RDBconfig = null ;

  }


  public void initDriver(){
    driverQuery.init(RDBconfig);
    driverQuery.createConnection();
  }

  public ResultSet executeQuery(){
    String sql  = buildQuery();
    return driverQuery.executeQuery(sql);

  }
}
