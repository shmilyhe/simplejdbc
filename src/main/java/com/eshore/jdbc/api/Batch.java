package com.eshore.jdbc.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.eshore.jdbc.SQLValueUtil;

/**
 * 
 * @author eric
 *
 */
public class Batch {
	PreparedStatement ps;
	String sql;
	Connection conn;
	
	public Batch(String sql,Connection conn) {
		try {
			this.ps=conn.prepareStatement(sql);
			this.sql=sql;
		} catch (SQLException e) {
			close();
			throw new RuntimeException(sql,e);
		}
		
	}
	
	public void close() {
		try {
			if(conn!=null)
			conn.close();
			conn=null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		conn=null;
	}
	
	public void add(Object ...param) {
		int i=1;
		try {
		if(param!=null&&param.length>0)
			for(Object o :param){
				SQLValueUtil.setParameterValue(ps, i, o);
				i++;
			}
		}catch(Exception e) {
			close();
			throw new RuntimeException(sql,e);
		}
	}
	
	public int [] exec() {
		try {
			return ps.executeBatch();
		} catch (SQLException e) {
			close();
			throw new RuntimeException(sql,e);
		}
	}
	
	protected void finalize(){
		close();
	}
	
}
