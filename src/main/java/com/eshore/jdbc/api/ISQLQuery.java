package com.eshore.jdbc.api;

import javax.sql.DataSource;

/**
 * 
 * @author eric
 *
 */
public interface ISQLQuery {
	
	public IQuery query(String sql);
	public int asInt(String sql);
	public int asInt(String sql, Object ...params);
	public Object findOne(String sql, Object ...params);
	public Object findOne(String sql);
	public void setDataSource(DataSource ds);
	Object findOne(String sql, Class clazz, Object[] params);
}
