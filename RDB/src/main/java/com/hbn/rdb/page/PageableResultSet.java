package com.hbn.rdb.page;

/**
 * @author wangheng
 * @create 2019-03-07 上午9:57
 * @desc
 *
 * 分页模型  自己
 *
 * 获得ResultSet的时候:
 *
 * pstm=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//这里的属性一定要记得修改
 *
 * Pageable rs=new PageableResultSet(pstm.executeQuery());//得到ResultSet
 *
 * 4.设置RS
 *
 * rs.setPageSize(pageSize);//设置每页显示数目
 *    rs.gotoPage(page);//设置当前选择第几页
 *    for (int i = 0; i < rs.getPageRowsCount(); i++) {
 *     /...进行取值.../
 *     rs.next();//不要忘记next()
 * }
 *
 *
 *
 **/


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class PageableResultSet implements Pageable {



    private static final Logger logger = LoggerFactory.getLogger(PageableResultSet.class);


    protected java.sql.ResultSet rs=null;
    protected int rowsCount;
    protected int pageSize;
    protected int curPage;


    public PageableResultSet(java.sql.ResultSet rs ,Boolean isOracle ,int rowsCount) throws java.sql.SQLException {

        // 传入 ResultSet rs 进行判断
        if(rs==null) throw new SQLException("given ResultSet is NULL","user");
        if(!isOracle){
            /**
             *
             * 跳到结尾
             * 查看长度
             * 返回到头部
             */
            rs.last();
            this.rowsCount=rs.getRow();
            rs.beforeFirst();
            this.rs =rs;
        }else {
            this.rowsCount =rowsCount ;
            this.rs=rs;
        }

        logger.info(" init PageableResultSet succuss ");
        logger.info("rowcount in PageableResultSet is {}",this.rowsCount);
        logger.info("rs  in PageableResultSet is ok ? {}",this.rs != null);

    }

    @Override
    public int getCurPage() {
        return curPage;
    }

    @Override
    public int getPageCount() {
        if(rowsCount==0) return 0;
        if(pageSize==0) return 1;
    //  calculate PageCount
        double tmpD=(double)rowsCount/pageSize;
        int tmpI=(int)tmpD;
        if(tmpD>tmpI) tmpI++;
        return tmpI;
    }

    @Override
    public int getPageRowsCount() {
        if(pageSize==0) return rowsCount;
        if(getRowsCount()==0) return 0;
        if(curPage!=getPageCount()) return pageSize;
        return rowsCount-(getPageCount()-1)*pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }

    @Override
    public void gotoPage(int page) {
        if (this.rs == null)
            return;
        if (page < 1)
            page = 1;
        if (page > getPageCount())
            page = getPageCount();
        int row = (page - 1) * pageSize + 1;
        try {
            this.rs.absolute(row);
            curPage = page;
        }
        catch (java.sql.SQLException e) {
        }


    }

    @Override
    public void pageFirst() throws SQLException {
        int row=(curPage-1)*pageSize+1;
        this.rs.absolute(row);

    }

    @Override
    public void pageLast() throws SQLException {
        int row=(curPage-1)*pageSize+getPageRowsCount();
        this.rs.absolute(row);


    }

    @Override
    public void setPageSize(int pageSize) {
        if(pageSize>=0){
            this.pageSize=pageSize;
            curPage=1;
        }
    }


    @Override
    public boolean next() throws SQLException {
        return this.rs.next();
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        return this.rs.absolute(row);
    }

    @Override
    public void afterLast() throws SQLException {
        this.rs.afterLast();

    }

    @Override
    public void beforeFirst() throws SQLException {
        this.rs.beforeFirst();

    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        this.rs.cancelRowUpdates();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.rs.clearWarnings();

    }

    @Override
    public void close() throws SQLException {
        this.rs.close();

    }

    @Override
    public void deleteRow() throws SQLException {
        this.rs.deleteRow();

    }

    @Override
    public int findColumn(String columnName) throws SQLException {
        return this.rs.findColumn(columnName);
    }

    @Override
    public boolean first() throws SQLException {
        return this.rs.first();
    }

    @Override
    public Array getArray(int i) throws SQLException {
        return this.rs.getArray(i);
    }

    @Override
    public Array getArray(String colName) throws SQLException {
        return this.rs.getArray(colName);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return this.rs.getAsciiStream(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return this.rs.getAsciiStream(columnName);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return this.rs.getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return this.rs.getBigDecimal(columnName);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return this.rs.getBigDecimal(columnIndex, scale);
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        return this.rs.getBigDecimal(columnName, scale);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return this.rs.getBinaryStream(columnIndex);
    }

    @Override
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return this.rs.getBinaryStream(columnName);
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        return this.rs.getBlob(i);
    }

    @Override
    public Blob getBlob(String colName) throws SQLException {
        return this.rs.getBlob(colName);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return this.rs.getBoolean(columnIndex);
    }

    @Override
    public boolean getBoolean(String columnName) throws SQLException {
        return this.rs.getBoolean(columnName);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return this.rs.getByte(columnIndex);
    }

    @Override
    public byte getByte(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getByte(columnName);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getBytes(columnIndex);
    }

    @Override
    public byte[] getBytes(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getBytes(columnName);
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getCharacterStream(columnIndex);
    }

    @Override
    public Reader getCharacterStream(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getCharacterStream(columnName);
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getClob(i);
    }

    @Override
    public Clob getClob(String colName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getClob(colName);
    }

    @Override
    public int getConcurrency() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getConcurrency();
    }

    @Override
    public String getCursorName() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getCursorName();
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getDate(columnIndex);
    }

    @Override
    public Date getDate(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getDate(columnName);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getDate(columnIndex, cal);
    }

    @Override
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getDate(columnName, cal);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getDouble(columnIndex);
    }

    @Override
    public double getDouble(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getDouble(columnName);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getFetchSize();
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getFloat(columnIndex);
    }

    @Override
    public float getFloat(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getFloat(columnName);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getInt(columnIndex);
    }

    @Override
    public int getInt(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getInt(columnName);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getLong(columnIndex);
    }

    @Override
    public long getLong(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getLong(columnName);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getMetaData();
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getObject(columnIndex);
    }


    @Override
    public Object getObject(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getObject(columnName);
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getObject(i, map);
    }



    @Override
    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getObject(colName, map);
    }


    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return null;
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return null;
    }

    @Override
    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        this.rs.updateObject(columnIndex,x,targetSqlType,scaleOrLength);
    }

    @Override
    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        this.rs.updateObject(columnLabel,x,targetSqlType,scaleOrLength);
    }

    @Override
    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
        this.rs.updateObject(columnIndex,x,targetSqlType);
    }

    @Override
    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
        this.rs.updateObject(columnLabel,x,targetSqlType);
    }



    @Override
    public Ref getRef(int i) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getRef(i);
    }

    @Override
    public Ref getRef(String colName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getRef(colName);
    }

    @Override
    public int getRow() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getRow();
    }

    @Override

    public short getShort(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getShort(columnIndex);
    }
    @Override
    public short getShort(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getShort(columnName);
    }
    @Override
    public Statement getStatement() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getStatement();
    }
    @Override
    public String getString(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getString(columnIndex);
    }
    @Override
    public String getString(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getString(columnName);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTime(columnIndex);
    }
    @Override
    public Time getTime(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTime(columnName);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTime(columnIndex, cal);
    }

    @Override
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTime(columnName, cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTimestamp(columnName);
    }
    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTimestamp(columnIndex, cal);
    }
    @Override
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getTimestamp(columnName, cal);
    }
    @Override
    public int getType() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getType();
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getURL(columnIndex);
    }

    @Override
    public URL getURL(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getURL(columnName);
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getUnicodeStream(columnIndex);
    }
    @Override
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getUnicodeStream(columnName);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getWarnings();
    }

    @Override
    public void insertRow() throws SQLException {
        // TODO 自动生成方法存根
        this.rs.insertRow();

    }

    @Override
    public boolean isAfterLast() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.isAfterLast();
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.isBeforeFirst();
    }

    @Override
    public boolean isFirst() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.isFirst();
    }

    @Override
    public boolean isLast() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.isLast();
    }

    @Override
    public boolean last() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.last();
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        // TODO 自动生成方法存根
        this.rs.moveToCurrentRow();
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        // TODO 自动生成方法存根
        this.rs.moveToInsertRow();
    }

    @Override
    public boolean previous() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.previous();
    }

    @Override
    public void refreshRow() throws SQLException {
        // TODO 自动生成方法存根
        this.rs.refreshRow();
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.relative(rows);
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.rowDeleted();
    }

    @Override
    public boolean rowInserted() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.rowInserted();
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.rowUpdated();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.setFetchDirection(direction);
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.setFetchSize(rows);
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateArray(columnIndex, x);
    }

    @Override
    public void updateArray(String columnName, Array x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateArray(columnName, x);
    }



    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateAsciiStream(columnIndex, x, length);
    }

    @Override
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateAsciiStream(columnName, x, length);
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBigDecimal(columnIndex, x);
    }

    @Override
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBigDecimal(columnName, x);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBinaryStream(columnIndex, x, length);
    }

    @Override
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBinaryStream(columnName, x, length);
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBlob(columnIndex, x);
    }

    @Override
    public void updateBlob(String columnName, Blob x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBlob(columnName, x);
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBoolean(columnIndex, x);
    }

    @Override
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBoolean(columnName, x);
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateByte(columnIndex, x);
    }

    @Override
    public void updateByte(String columnName, byte x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateByte(columnName, x);
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBytes(columnIndex, x);
    }

    @Override
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateBytes(columnName, x);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateCharacterStream(columnName, reader, length);
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateClob(columnIndex, x);
    }

    @Override
    public void updateClob(String columnName, Clob x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateClob(columnName, x);
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateDate(columnIndex, x);
    }

    @Override
    public void updateDate(String columnName, Date x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateDate(columnName, x);
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateDouble(columnIndex, x);
    }

    @Override
    public void updateDouble(String columnName, double x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateDouble(columnName, x);
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateFloat(columnIndex, x);
    }

    @Override
    public void updateFloat(String columnName, float x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateFloat(columnName, x);
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateInt(columnIndex, x);
    }

    @Override
    public void updateInt(String columnName, int x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateInt(columnName, x);
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateLong(columnIndex, x);
    }

    @Override
    public void updateLong(String columnName, long x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateLong(columnName, x);
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateNull(columnIndex);
    }

    @Override
    public void updateNull(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateNull(columnName);
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateObject(columnIndex, x);
    }

    @Override
    public void updateObject(String columnName, Object x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateObject(columnName, x);
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateObject(columnIndex, x, scale);
    }

    @Override
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateObject(columnName, x, scale);
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateRef(columnIndex, x);
    }

    @Override
    public void updateRef(String columnName, Ref x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateRef(columnName, x);
    }

    @Override
    public void updateRow() throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateRow();
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateShort(columnIndex, x);
    }

    @Override
    public void updateShort(String columnName, short x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateShort(columnName, x);
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateString(columnIndex, x);
    }

    @Override
    public void updateString(String columnName, String x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateString(columnName, x);
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateTime(columnIndex, x);
    }

    @Override
    public void updateTime(String columnName, Time x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateTime(columnName, x);
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateTimestamp(columnIndex, x);
    }

    @Override
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateTimestamp(columnName, x);
    }

    @Override
    public boolean wasNull() throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.wasNull();
    }



    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getRowId(columnIndex);
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        // TODO 自动生成方法存根
        return this.rs.getRowId(columnLabel);
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        // TODO 自动生成方法存根
        this.rs.updateRowId(columnIndex , x);
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {

        // TODO 自动生成方法存根
        this.rs.updateRowId(columnLabel , x);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.rs.getHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.rs.isClosed();
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {

        this.rs.updateNString(columnIndex,nString);
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {

        this.rs.updateNString(columnLabel,nString);
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {

        this.rs.updateNClob(columnIndex,nClob);
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {

        this.rs.updateNClob(columnLabel,nClob);
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return this.rs.getNClob(columnIndex);
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return this.rs.getNClob(columnLabel);
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return this.rs.getSQLXML(columnIndex);
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return this.rs.getSQLXML(columnLabel);
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        this.rs.updateSQLXML(columnIndex,xmlObject);
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {

        this.rs.updateSQLXML(columnLabel,xmlObject);
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return this.rs.getNString( columnIndex);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
            return this.rs.getNString(columnLabel);
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return this.rs.getNCharacterStream(columnIndex);
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return this.rs.getNCharacterStream(columnLabel);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

        this.rs.updateNCharacterStream(columnIndex,x,length);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        this.rs.updateNCharacterStream(columnLabel,reader,length);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {

        this.rs.updateAsciiStream(columnIndex,x,length);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {

        this.rs.updateBinaryStream(columnIndex,x,length);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

        this.rs.updateCharacterStream(columnIndex,x,length);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {

        this.rs.updateAsciiStream(columnLabel,x,length);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {

        this.rs.updateBinaryStream(columnLabel,x,length);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

        this.rs.updateCharacterStream(columnLabel,reader,length);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {

        this.rs.updateBlob(columnIndex,inputStream,length);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {

        this.rs.updateBlob(columnLabel,inputStream,length);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {

        this.rs.updateClob(columnIndex,reader,length);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {

        this.rs.updateClob(columnLabel,reader);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {

        this.rs.updateNClob(columnIndex,reader,length);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {

        this.rs.updateNClob(columnLabel,reader,length);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {

        this.rs.updateNCharacterStream(columnIndex,x);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {

        this.rs.updateNCharacterStream(columnLabel,reader);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {

        this.rs.updateAsciiStream(columnIndex,x);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {

        this.rs.updateBinaryStream(columnIndex,x);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {

        this.rs.updateCharacterStream(columnIndex,x);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {

        this.rs.updateAsciiStream(columnLabel,x);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {

        this.rs.updateBinaryStream(columnLabel,x);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {

        this.rs.updateCharacterStream(columnLabel,reader);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {

        this.rs.updateBlob(columnIndex,inputStream);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {

        this.rs.updateBlob(columnLabel,inputStream);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {

        this.rs.updateClob(columnIndex,reader);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {

        this.rs.updateClob(columnLabel,reader);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {

        this.rs.updateNClob(columnIndex,reader);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {

        this.rs.updateNClob(columnLabel,reader);
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.rs.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.rs.isWrapperFor(iface);
    }

}
