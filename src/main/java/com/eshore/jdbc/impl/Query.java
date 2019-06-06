package com.eshore.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.eshore.tools.Log;
import com.eshore.tools.Logger;
import com.eshore.jdbc.SQLValueUtil;
import com.eshore.jdbc.api.IPagination;
import com.eshore.jdbc.api.IQuery;

public class Query implements IQuery {
	static Log log =Logger.getLogger(Query.class);
	Class<?> clazz;
	int page;
	int pageSize;
	Object[] params;
	 IPagination pagination;
	Connection conn=null;
	PreparedStatement statm=null;
	ResultSet rs =null;
	String sql;
	int rows;
	public Query(String sql,Connection conn, IPagination pagination){
		this.sql=sql;
		this.conn=conn;
		this.pagination=pagination;
	}
	
	private void query() throws SQLException{
			long bt =System.currentTimeMillis();
			String s=sql;
			if(pageSize!=0&&pagination!=null){
				 s=pagination.fixSQl(sql, page, pageSize);
				 String countSql=pagination.fixCountSQL(sql);
				 rows = new Query(countSql,new WrapperConnction(conn),null).param(params).asInt();
			}
			statm=conn.prepareStatement(s);
			if(params!=null){
				int i=1;
				for(Object o :params){
					SQLValueUtil.setParameterValue(statm, i, o);
					i++;
				}
			}
			rs =statm.executeQuery();

	} 
	
	public void close(){
		if(rs!=null)try{rs.close();rs=null;}catch(Exception e){log.warm(e);}
		if( statm!=null)try{ statm.close();statm=null;}catch(Exception e){log.warm(e);}
		if(conn!=null)try{conn.close();conn=null;}catch(Exception e){log.warm(e);}
	}
	@Override
	public IQuery page(int page, int pageSize) {
		this.page=page;
		this.pageSize=pageSize;
		return this;
	}

	@Override
	public IQuery param(Object... objs) {
		params=objs;
		return this;
	}

	@Override
	public IQuery clazz(Class<?> clazz) {
		this.clazz=clazz;
		return this;
	}

	@Override
	public List list() {
		List list =new ArrayList();
		Collection d=null;
		try {
			d=excute();
			list.addAll(d);
			d.clear();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			close();
		}
	}

	@Override
	public Object findOne() {
		Collection d;
		try {
			d = excute();
			Object rst=null;
			for(Object o:d){
				rst= o;
				break;
			}
			return rst;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			close();
		}
	}

	@Override
	public int asInt() {
		try {
			query();
			if(rs.next()){
				return rs.getInt(1);
			}
			return -1;
		} catch (SQLException e) {
			return -1;
		}finally{
			close();
		}
	}
	protected Collection excute() throws SQLException{
		query() ;
		RsList rlist =new RsList(rs,pagination,statm,clazz,this.page,this.pageSize,ig);
		rlist.setAlias(alias);
		return rlist;
	}

	@Override
	public int rows() {
		return rows;
	}

	@Override
	public int pages() {
		if(this.pageSize==0)return 0;
		int total =rows/pageSize;
		if(rows%pageSize>0)total+=1;
		return total;
	}

	String[] ig;
	@Override
	public IQuery igore(String... ig) {
		this.ig=ig;
		return this;
	}
     Map alias;
	@Override
	public IQuery alias(Map amap) {
		this.alias=amap;
		return this;
	}

}
