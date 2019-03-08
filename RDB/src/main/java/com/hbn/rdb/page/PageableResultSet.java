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
 *    }
 *
 *
 *
 **/


import com.hbn.rdb.common.DriverQuery;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class PageableResultSet implements Pageable {



    protected java.sql.ResultSet rs=null;
    protected int rowsCount;
    protected int pageSize;
    protected int curPage;
    protected String command = "";


    public PageableResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        if(rs==null) throw new SQLException("given ResultSet is NULL","user");
        rs.last();
        rowsCount=rs.getRow();
        System.out.println(rowsCount);
        rs.beforeFirst();
        this.rs=rs;

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
        if (rs == null)
            return;
        if (page < 1)
            page = 1;
        if (page > getPageCount())
            page = getPageCount();
        int row = (page - 1) * pageSize + 1;
        try {
            rs.absolute(row);
            curPage = page;
        }
        catch (java.sql.SQLException e) {
        }


    }

    @Override
    public void pageFirst() throws SQLException {
        int row=(curPage-1)*pageSize+1;
        rs.absolute(row);

    }

    @Override
    public void pageLast() throws SQLException {
        int row=(curPage-1)*pageSize+getPageRowsCount();
        rs.absolute(row);


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
        return rs.next();
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        return rs.absolute(row);
    }

    @Override
    public void afterLast() throws SQLException {
        rs.afterLast();

    }

    @Override
    public void beforeFirst() throws SQLException {
        rs.beforeFirst();

    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        rs.cancelRowUpdates();
    }

    @Override
    public void clearWarnings() throws SQLException {
        rs.clearWarnings();

    }

    @Override
    public void close() throws SQLException {
        rs.close();

    }

    @Override
    public void deleteRow() throws SQLException {
        rs.deleteRow();

    }

    @Override
    public int findColumn(String columnName) throws SQLException {
        return rs.findColumn(columnName);
    }

    @Override
    public boolean first() throws SQLException {
        return rs.first();
    }

    @Override
    public Array getArray(int i) throws SQLException {
        return rs.getArray(i);
    }

    @Override
    public Array getArray(String colName) throws SQLException {
        return rs.getArray(colName);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return rs.getAsciiStream(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return rs.getAsciiStream(columnName);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return rs.getBigDecimal(columnName);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return rs.getBigDecimal(columnIndex, scale);
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        return rs.getBigDecimal(columnName, scale);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return rs.getBinaryStream(columnIndex);
    }

    @Override
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return rs.getBinaryStream(columnName);
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        return rs.getBlob(i);
    }

    @Override
    public Blob getBlob(String colName) throws SQLException {
        return rs.getBlob(colName);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }

    @Override
    public boolean getBoolean(String columnName) throws SQLException {
        return rs.getBoolean(columnName);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }

    @Override
    public byte getByte(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getByte(columnName);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getBytes(columnIndex);
    }

    @Override
    public byte[] getBytes(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getBytes(columnName);
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getCharacterStream(columnIndex);
    }

    @Override
    public Reader getCharacterStream(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getCharacterStream(columnName);
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getClob(i);
    }

    @Override
    public Clob getClob(String colName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getClob(colName);
    }

    @Override
    public int getConcurrency() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getConcurrency();
    }

    @Override
    public String getCursorName() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getCursorName();
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getDate(columnIndex);
    }

    @Override
    public Date getDate(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getDate(columnName);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getDate(columnIndex, cal);
    }

    @Override
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getDate(columnName, cal);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getDouble(columnIndex);
    }

    @Override
    public double getDouble(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getDouble(columnName);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getFetchSize();
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getFloat(columnIndex);
    }

    @Override
    public float getFloat(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getFloat(columnName);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getInt(columnIndex);
    }

    @Override
    public int getInt(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getInt(columnName);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getLong(columnIndex);
    }

    @Override
    public long getLong(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getLong(columnName);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getMetaData();
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getObject(columnIndex);
    }


    @Override
    public Object getObject(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getObject(columnName);
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getObject(i, map);
    }



    @Override
    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getObject(colName, map);
    }


    //--------











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

    }

    @Override
    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {

    }

    @Override
    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {

    }

    @Override
    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {

    }

    //------



    @Override
    public Ref getRef(int i) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getRef(i);
    }

    @Override
    public Ref getRef(String colName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getRef(colName);
    }

    @Override
    public int getRow() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getRow();
    }

    @Override

    public short getShort(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getShort(columnIndex);
    }
    @Override
    public short getShort(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getShort(columnName);
    }
    @Override
    public Statement getStatement() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getStatement();
    }
    @Override
    public String getString(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getString(columnIndex);
    }
    @Override
    public String getString(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getString(columnName);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTime(columnIndex);
    }
    @Override
    public Time getTime(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTime(columnName);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTime(columnIndex, cal);
    }

    @Override
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTime(columnName, cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTimestamp(columnName);
    }
    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTimestamp(columnIndex, cal);
    }
    @Override
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getTimestamp(columnName, cal);
    }
    @Override
    public int getType() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getType();
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getURL(columnIndex);
    }

    @Override
    public URL getURL(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getURL(columnName);
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getUnicodeStream(columnIndex);
    }
    @Override
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getUnicodeStream(columnName);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        // TODO 自动生成方法存根
        return rs.getWarnings();
    }

    @Override
    public void insertRow() throws SQLException {
        // TODO 自动生成方法存根
        rs.insertRow();

    }

    @Override
    public boolean isAfterLast() throws SQLException {
        // TODO 自动生成方法存根
        return rs.isAfterLast();
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        // TODO 自动生成方法存根
        return rs.isBeforeFirst();
    }

    @Override
    public boolean isFirst() throws SQLException {
        // TODO 自动生成方法存根
        return rs.isFirst();
    }

    @Override
    public boolean isLast() throws SQLException {
        // TODO 自动生成方法存根
        return rs.isLast();
    }

    @Override
    public boolean last() throws SQLException {
        // TODO 自动生成方法存根
        return rs.last();
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        // TODO 自动生成方法存根
        rs.moveToCurrentRow();
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        // TODO 自动生成方法存根
        rs.moveToInsertRow();
    }

    @Override
    public boolean previous() throws SQLException {
        // TODO 自动生成方法存根
        return rs.previous();
    }

    @Override
    public void refreshRow() throws SQLException {
        // TODO 自动生成方法存根
        rs.refreshRow();
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        // TODO 自动生成方法存根
        return rs.relative(rows);
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        // TODO 自动生成方法存根
        return rs.rowDeleted();
    }

    @Override
    public boolean rowInserted() throws SQLException {
        // TODO 自动生成方法存根
        return rs.rowInserted();
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        // TODO 自动生成方法存根
        return rs.rowUpdated();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        // TODO 自动生成方法存根
        rs.setFetchDirection(direction);
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        // TODO 自动生成方法存根
        rs.setFetchSize(rows);
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateArray(columnIndex, x);
    }

    @Override
    public void updateArray(String columnName, Array x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateArray(columnName, x);
    }



    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateAsciiStream(columnIndex, x, length);
    }

    @Override
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateAsciiStream(columnName, x, length);
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBigDecimal(columnIndex, x);
    }

    @Override
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBigDecimal(columnName, x);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBinaryStream(columnIndex, x, length);
    }

    @Override
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBinaryStream(columnName, x, length);
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBlob(columnIndex, x);
    }

    @Override
    public void updateBlob(String columnName, Blob x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBlob(columnName, x);
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBoolean(columnIndex, x);
    }

    @Override
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBoolean(columnName, x);
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateByte(columnIndex, x);
    }

    @Override
    public void updateByte(String columnName, byte x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateByte(columnName, x);
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBytes(columnIndex, x);
    }

    @Override
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateBytes(columnName, x);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateCharacterStream(columnName, reader, length);
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateClob(columnIndex, x);
    }

    @Override
    public void updateClob(String columnName, Clob x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateClob(columnName, x);
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateDate(columnIndex, x);
    }

    @Override
    public void updateDate(String columnName, Date x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateDate(columnName, x);
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateDouble(columnIndex, x);
    }

    @Override
    public void updateDouble(String columnName, double x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateDouble(columnName, x);
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateFloat(columnIndex, x);
    }

    @Override
    public void updateFloat(String columnName, float x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateFloat(columnName, x);
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateInt(columnIndex, x);
    }

    @Override
    public void updateInt(String columnName, int x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateInt(columnName, x);
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateLong(columnIndex, x);
    }

    @Override
    public void updateLong(String columnName, long x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateLong(columnName, x);
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateNull(columnIndex);
    }

    @Override
    public void updateNull(String columnName) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateNull(columnName);
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateObject(columnIndex, x);
    }

    @Override
    public void updateObject(String columnName, Object x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateObject(columnName, x);
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateObject(columnIndex, x, scale);
    }

    @Override
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateObject(columnName, x, scale);
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateRef(columnIndex, x);
    }

    @Override
    public void updateRef(String columnName, Ref x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateRef(columnName, x);
    }

    @Override
    public void updateRow() throws SQLException {
        // TODO 自动生成方法存根
        rs.updateRow();
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateShort(columnIndex, x);
    }

    @Override
    public void updateShort(String columnName, short x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateShort(columnName, x);
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateString(columnIndex, x);
    }

    @Override
    public void updateString(String columnName, String x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateString(columnName, x);
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateTime(columnIndex, x);
    }

    @Override
    public void updateTime(String columnName, Time x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateTime(columnName, x);
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateTimestamp(columnIndex, x);
    }

    @Override
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateTimestamp(columnName, x);
    }

    @Override
    public boolean wasNull() throws SQLException {
        // TODO 自动生成方法存根
        return rs.wasNull();
    }



    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getRowId(columnIndex);
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        // TODO 自动生成方法存根
        return rs.getRowId(columnLabel);
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        // TODO 自动生成方法存根
        rs.updateRowId(columnIndex , x);
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {

        // TODO 自动生成方法存根
        rs.updateRowId(columnLabel , x);
    }

    @Override
    public int getHoldability() throws SQLException {
        return rs.getHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return rs.isClosed();
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {

        rs.updateNString(columnIndex,nString);
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {

        rs.updateNString(columnLabel,nString);
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {

        rs.updateNClob(columnIndex,nClob);
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {

        rs.updateNClob(columnLabel,nClob);
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return rs.getNClob(columnIndex);
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return rs.getNClob(columnLabel);
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return rs.getSQLXML(columnLabel);
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        rs.updateSQLXML(columnIndex,xmlObject);
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {

        rs.updateSQLXML(columnLabel,xmlObject);
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return rs.getNString( columnIndex);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
            return rs.getNString(columnLabel);
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return rs.getNCharacterStream(columnIndex);
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return rs.getNCharacterStream(columnLabel);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

        rs.updateNCharacterStream(columnIndex,x,length);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        rs.updateNCharacterStream(columnLabel,reader,length);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {

        rs.updateAsciiStream(columnIndex,x,length);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {

        rs.updateBinaryStream(columnIndex,x,length);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

        rs.updateCharacterStream(columnIndex,x,length);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {

        rs.updateAsciiStream(columnLabel,x,length);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {

        rs.updateBinaryStream(columnLabel,x,length);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

        rs.updateCharacterStream(columnLabel,reader,length);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {

        rs.updateBlob(columnIndex,inputStream,length);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {

        rs.updateBlob(columnLabel,inputStream,length);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {

        rs.updateClob(columnIndex,reader,length);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {

        rs.updateClob(columnLabel,reader);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {

        rs.updateNClob(columnIndex,reader,length);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {

        rs.updateNClob(columnLabel,reader,length);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {

        rs.updateNCharacterStream(columnIndex,x);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {

        rs.updateNCharacterStream(columnLabel,reader);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {

        rs.updateAsciiStream(columnIndex,x);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {

        rs.updateBinaryStream(columnIndex,x);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {

        rs.updateCharacterStream(columnIndex,x);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {

        rs.updateAsciiStream(columnLabel,x);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {

        rs.updateBinaryStream(columnLabel,x);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {

        rs.updateCharacterStream(columnLabel,reader);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {

        rs.updateBlob(columnIndex,inputStream);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {

        rs.updateBlob(columnLabel,inputStream);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {

        rs.updateClob(columnIndex,reader);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {

        rs.updateClob(columnLabel,reader);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {

        rs.updateNClob(columnIndex,reader);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {

        rs.updateNClob(columnLabel,reader);
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return rs.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return rs.isWrapperFor(iface);
    }

}
