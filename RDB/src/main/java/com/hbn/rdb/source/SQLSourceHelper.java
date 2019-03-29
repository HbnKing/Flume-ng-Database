package com.hbn.rdb.source;

import com.hbn.rdb.common.ConfigConstant;
import com.hbn.rdb.common.DriverQuery;
import com.hbn.rdb.common.FileStatus;
import com.hbn.rdb.common.RDBconfig;
import com.hbn.rdb.page.PageableResultSet;
import org.apache.flume.Context;
import org.apache.flume.conf.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;


public class SQLSourceHelper {

  private static final Logger logger = LoggerFactory.getLogger(SQLSourceHelper.class);

  private static Boolean isOracle = false ;
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

  private static com.hbn.rdb.common.RDBconfig RDBconfig = new RDBconfig();

  private static DriverQuery driverQuery = new DriverQuery() ;

  private static PageableResultSet  pageableResultSet = null ;



  private static int batchsize;

  /**
   * Builds an SQLSourceHelper containing the configuration parameters and
   * usefull utils for SQL Source
   *
   * @param context    Flume source context, contains the properties from configuration file
   * @param sourceName source file name for store status
   *
   * 构造方法
   * 1.解析 context  参数
   * 2.根据内容 获取必要信息 判断结果流等
   *
   */
  public SQLSourceHelper(Context context, String sourceName) {
    logger.info("started to  init SQLSourceHelper");

    this.context = context;

    this.sourceName = sourceName;
    //起点
    this.begin = context.getLong(ConfigConstant.BEGINNING,0L);
    this.logger.info("begin  is  {}",begin );

    //自己写的sql 如果有定义 取其值 否则为 null
    this.customerquery = context.getString(ConfigConstant.CUSTOMQUERY);

    logger.info("customerquery  is  {}" ,customerquery);

    //多个参数配的sql
    this.table = context.getString(ConfigConstant.TABLE);
    this.columnsToSelect = context.getString(ConfigConstant.COLUMNS_TO_SELECT,ConfigConstant.DEFAULT_COLUMNS_TO_SELECT);
    this.autoIncrementField = context.getString(ConfigConstant.AUTOINCREMENTFIELD);

    logger.info("table  is  {}",table);
    logger.info("columnsToSelect  is  {}",columnsToSelect);
    logger.info("autoIncrementField  is  {}",autoIncrementField);


    this.Charset = context.getString(ConfigConstant.CHARSET_RESULTSET, ConfigConstant.DEFAULT_CHARSET_RESULTSET);
    logger.info("charset  is  {}",Charset);

    //query = buildQuery();
    /**
     * 数据库连接配置
     */

    this.drivername = context.getString(ConfigConstant.DRIVER);

    this.username = context.getString(ConfigConstant.USER);
    this.password = context.getString(ConfigConstant.PASSWORD);
    this.conectionurl = context.getString(ConfigConstant.URL);
    logger.info("connectionurl  is  {}",conectionurl);
    logger.info("driver  is  {}",drivername);



    checkMandatoryProperties();
    //封装到 RDB
    RDBconfig.setDbpassword(password);
    RDBconfig.setDbuser(username);
    RDBconfig.setDriverclass(drivername);
    RDBconfig.setUrl(conectionurl);

    //对该对象初始化
    //查询的初始化
    //driverQuery.init(RDBconfig);

    //中间文件状态 资料
    this.filePath = context.getString(ConfigConstant.FILEPATH,ConfigConstant.DEFAULT_FILEPATH);
    this.fileName = context.getString(ConfigConstant.FILENAME,ConfigConstant.DEFAULT_FILENAME);

    logger.info("filePath is  {}",filePath);
    logger.info("fileName is  {}",fileName);


    //更新 currentIndex
    //比较 起点  和  当前值 的最大值
    this.currentIndex = Math.max(begin ,getCurrentIndexStatusFile());
    logger.info("Index  started  from  {}" ,currentIndex);

    //  分页信息
    this.batchsize = context.getInteger(ConfigConstant.BATCH_SIZE ,ConfigConstant.DEFAULT_BATCH_SIZE);


    this.isOracle = drivername.toLowerCase().contains("oracle");

    logger.info( "db  is oracle {}",isOracle);


  }


  public void checkMandatoryProperties() {

    if (this.conectionurl == null) {
      throw new ConfigurationException("database - connection - url property not set");
    }

    if (this.table == null && this.customerquery == null) {
      throw new ConfigurationException("property table not set");
    }

    if (this.username == null) {
      throw new ConfigurationException("database - connection - user property not set");
    }

    if (this.password == null) {
      throw new ConfigurationException("database - connection - password property not set");
    }
  }

  public String buildQuery() {

    if (this.customerquery == null) {
      // 如果 customerQuery 为null，就以 offsest 作为 id
      return "SELECT " + this.columnsToSelect + " FROM " + this.table +" where " +this.autoIncrementField + " > " +this.currentIndex ;
    } else {
      //如果 customerquery 不为 null，那么就要将其最后已存在的 offset 替换掉新查询出的 offset（！！！）
      //
      if (this.customerquery.contains("$@$")) {
        //直接修改  返回行新的 sql 语句
        return this.customerquery.replace("$@$", String.valueOf(currentIndex));
      } else {
        //如果没有设置
        // currentIndex  应该为 begin  默认值 Long.MIN_VALUE
        //还可以在修改一下
        /*if(currentIndex.equals(Long.MIN_VALUE)){
          return customerquery = customerquery.substring(0,customerquery.indexOf(">")+1)+currentIndex;
        }else return customerquery ;*/
        return this.customerquery ;

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


  public int getBatchsize() {
    return batchsize;
  }

  public void setBatchsize(int batchsize) {
    SQLSourceHelper.batchsize = batchsize;
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

    return driverQuery.executeQuery(sql , this.isOracle);
  }



  public int getRowCount(){
    return driverQuery.getRowCount();
  }




  private PageableResultSet getPageableResultSet() throws SQLException {
    // 第一次请求
    if(this.pageableResultSet==null){
      this.pageableResultSet = new PageableResultSet(executeQuery(),this.isOracle,getRowCount());
      this.pageableResultSet.setPageSize(this.batchsize);
    }
    return this.pageableResultSet;

  }


  private void closePageableResultSet() {
    try {
      pageableResultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      pageableResultSet = null ;
    }

  }




  //当前页面
  private static  int  curPage = 1 ;

  /**
   * 获取初始页面
   * 1.当前页
   * 2.当前页 用完 用下一页
   * 3.最后一页用完 重新生成 一本书
   *
   */
  public PageableResultSet getNextPageableResultSet() throws SQLException {

      // 没有页面 的时候
      if(pageableResultSet == null){
        getPageableResultSet();
        pageableResultSet.gotoPage(curPage);
        curPage++;
      }else if(curPage <=pageableResultSet.getPageCount()){
        pageableResultSet.gotoPage(curPage);
        curPage++;
      }else {
        closePageableResultSet();
        curPage = 1 ;
        getNextPageableResultSet();
      }

      return this.pageableResultSet ;

    }


  /**
   * 重置当前页面
   */
  public void resetCurPage(){

    if(curPage <=1){
      curPage = 1 ;
    }else {
      curPage -=1 ;
    }

  }

}

