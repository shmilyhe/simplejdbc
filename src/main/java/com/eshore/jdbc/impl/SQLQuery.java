package com.eshore.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.eshore.jdbc.SingleConnectionDataSource;
import com.eshore.jdbc.api.IPagination;
import com.eshore.jdbc.api.IQuery;
import com.eshore.jdbc.api.ISQLQuery;

public class SQLQuery implements ISQLQuery {
	protected  IPagination page=new Pagination();
	
	public SQLQuery(){}
	public SQLQuery(Connection conn){
		ds= new SingleConnectionDataSource(conn);
	}
	DataSource ds;
	@Override
	public void setDataSource(DataSource ds) {
		this.ds =ds;
	}
	
	public DataSource getDataSource() {
		return ds;
	}
	@Override
	public IQuery query(String sql) {
		try {
			return new  Query(sql,getDataSource().getConnection(),page);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int asInt(String sql) {
		return query(sql).asInt();
	}

	@Override
	public int asInt(String sql, Object... params) {
		return query(sql).param(params).asInt();
	}

	@Override
	public Object findOne(String sql, Object... params) {
		return query(sql).param(params).findOne();
	}
	@Override
	public Object findOne(String sql,Class clazz, Object... params) {
		return query(sql).clazz(clazz).param(params).findOne();
	}

	@Override
	public Object findOne(String sql) {
		return query(sql).findOne();
	}

}
