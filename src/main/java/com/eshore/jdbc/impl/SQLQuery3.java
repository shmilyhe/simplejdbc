package com.eshore.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.eshore.jdbc.SQLValueUtil;
import com.eshore.jdbc.api.IPagination;
import com.eshore.jdbc.api.ISQLQuery;
import com.eshore.jdbc.api.ISQLQuery3;

public class SQLQuery3 implements ISQLQuery3 {

	protected  IPagination page=new Pagination();
	public SQLQuery3(){}
	public SQLQuery3(Connection conn){}
	DataSource ds;
	@Override
	public void setDataSource(DataSource ds) {
		this.ds =ds;
	}

	@Override
	public int asInt(String sql) {
		Connection conn=null;
		PreparedStatement statm=null;
		ResultSet rs =null;
		try {
			statm = conn.prepareStatement(sql);
			long bt =System.currentTimeMillis();
			rs =statm.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
			return -1;
		} catch (SQLException e) {
			return -1;
		}finally{
			if(statm!=null)try {statm.close();} catch (SQLException e) {}
			if(conn!=null)try {conn.close();} catch (SQLException e) {}
			if(rs!=null)	try {rs.close();} catch (SQLException e) {}
			
		}

	}

	@Override
	public int asInt(String sql, Object... params) {
		Connection conn=null;
		PreparedStatement statm=null;
		ResultSet rs =null;
		try {
			statm = conn.prepareStatement(sql);
			long bt =System.currentTimeMillis();
			int i=1;
			for(Object o :params){
				SQLValueUtil.setParameterValue(statm, i, o);
				i++;
			}
			rs =statm.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
			return -1;
		} catch (SQLException e) {
			return -1;
		}finally{
			if(statm!=null)try {statm.close();} catch (SQLException e) {}
			if(conn!=null)try {conn.close();} catch (SQLException e) {}
			if(rs!=null)	try {rs.close();} catch (SQLException e) {}
			
		}
	}

	@Override
	public Object findOne(String sql, Class<?> clazz) {
		List list =new ArrayList();
		Connection c=null;
		Collection d=null;
		try {
			c = ds.getConnection();
			d=excute(clazz,c,0,0,sql);
			Object rst=null;
			for(Object o:d){
				rst= o;
				break;
			}
			return rst;
		} catch (SQLException e) {
		
		}finally{
			if(d!=null)d.clear();
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return null;
	}

	@Override
	public Object findOne(String sql, Class<?> clazz, Object... param) {
		List list =new ArrayList();
		Connection c=null;
		Collection d=null;
		try {
			c = ds.getConnection();
			d=excute(clazz,c,0,0,sql,param);
			Object rst=null;
			for(Object o:d){
				rst= o;
				break;
			}
			return rst;
		} catch (SQLException e) {
		
		}finally{
			if(d!=null)d.clear();
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return null;
	}

	@Override
	public List query(String sql) {
		return query(sql,null,0,0);
	}

	@Override
	public List query(String sql, Object... param) {
		return query(sql,null,0,0,param);
	}

	@Override
	public List query(String sql, int page, int pageSize) {
		return query(sql,null,page,pageSize);
	}

	@Override
	public List query(String sql, int page, int pageSize, Object... param) {
		return query(sql,null,page,pageSize,param);
	}

	@Override
	public <T> List query(String sql, Class<T> clazz, int page, int pageSize) {
		List list =new ArrayList();
		Connection c=null;
		Collection d=null;
		try {
			c = ds.getConnection();
			d=excute(clazz,c,page,pageSize,sql);
			list.addAll(d);
			return list;
		} catch (SQLException e) {
		
		}finally{
			if(d!=null)d.clear();
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return null;
	}

	@Override
	public <T> List query(String sql, Class<T> clazz, int page, int pageSize, Object ... param) {
		List list =new ArrayList();
		Connection c=null;
		Collection d=null;
		try {
			c = ds.getConnection();
			d=excute(clazz,c,page,pageSize,sql,param);
			list.addAll(d);
			return list;
		} catch (SQLException e) {
		
		}finally{
			if(d!=null)d.clear();
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return null;
	}
	
	
	protected Collection excute(Class<?> clazz,Connection conn,int page,int pageSize,String sql,Object... param) throws SQLException{
		PreparedStatement statm= conn.prepareStatement(sql);
		int i=1;
		for(Object o :param){
			SQLValueUtil.setParameterValue(statm, i, o);
			i++;
		}
		long bt =System.currentTimeMillis();
		ResultSet rs =statm.executeQuery();
		RsList rlist =new RsList(rs,this.page,statm,clazz,page,pageSize,null);
		long time=System.currentTimeMillis()-bt;
		return rlist;
	}
	
	protected Collection excute(Class<?> clazz,Connection conn,int page,int pageSize,String sql) throws SQLException{
		PreparedStatement statm= conn.prepareStatement(sql);
		long bt =System.currentTimeMillis();
		ResultSet rs =statm.executeQuery();
		RsList rlist =new RsList(rs,this.page,statm,clazz,page,pageSize,null);
		long time=System.currentTimeMillis()-bt;
		return rlist;
	}
	
	public IPagination getPagination() {
		return page;
	}
	public void setPagination(IPagination page) {
		this.page=page;
	}

}
