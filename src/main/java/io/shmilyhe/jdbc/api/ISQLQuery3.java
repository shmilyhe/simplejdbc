package io.shmilyhe.jdbc.api;

import java.util.List;

import javax.sql.DataSource;

/**

 * 查询数据库

 * 

 * @author eric

 *

 */

@SuppressWarnings("rawtypes")

public interface ISQLQuery3 {
	public IPagination getPagination();
	public void setPagination(IPagination page);
	public void setDataSource(DataSource ds);

	public abstract int asInt(String sql);

	public abstract int asInt(String sql, Object ...params);
	
	public Object findOne(String sql,Class<?> clazz);
	
	public Object findOne(String sql,Class<?> clazz,Object ...param);
	
	public abstract List query(String sql);

	public abstract List query(String sql, Object ...param);

	public abstract List query(String sql, int page,int pageSize);

	public abstract List query(String sql,  int page,int pageSize,Object ...param);

	public abstract <T> List query(String sql, Class<T> clazz,int page,int pageSize);

	public abstract <T> List query(String sql, Class<T> clazz,
			int page,int pageSize,Object ...param);

}