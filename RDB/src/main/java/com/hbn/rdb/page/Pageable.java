package com.hbn.rdb.page;

import java.sql.ResultSet;

/**
 * @author wangheng
 * @create 2019-03-07 上午9:55
 * @desc
 **/
public interface Pageable extends ResultSet {


    /**
     * 返回总页数
     * @return
     */
    int getPageCount();
    /**
     * 返回当前页的记录条数
     * @return
     */
    int getPageRowsCount();
    /**
     * 返回分页大小
     * @return
     */
    int getPageSize();
    /**转到指定页
     */
    void gotoPage(int page) ;
    /**
     * 设置分页大小
     */
    void setPageSize(int pageSize);
    /**
     * 返回总记录行数
     * @return
     */
    int getRowsCount();
    /**
     * 转到当前页的第一条记录
     * @exception java.sql.SQLException 异常说明。
     */
    void pageFirst() throws java.sql.SQLException;
    /**
     * 转到当前页的最后一条记录
     *
     * @exception java.sql.SQLException 异常说明。
     */
    void pageLast() throws java.sql.SQLException;

    /**
     * 返回当前页号
     * @return
     */
    int getCurPage();

}






