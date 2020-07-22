package com.eshore.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import com.eshore.tools.Log;
import com.eshore.tools.Logger;
import com.eshore.jdbc.SQLValueUtil;
import com.eshore.jdbc.SingleConnectionDataSource;
import com.eshore.jdbc.api.IPojoExeuter;
import com.eshore.jdbc.api.IQuery;
import com.eshore.jdbc.api.ISQLExecuter;
import com.eshore.jdbc.api.ISQLQuery;
import com.eshore.jdbc.api.ITransaction;

public class SQLExecuter implements ISQLExecuter {
	static Log log =Logger.getLogger(SQLExecuter.class);
	protected DataSource ds;
	//protected TransactionImpl tx;
	private boolean isOpenTransaction;
	private boolean isAutoCommit;
	Connection conn;
	long time;
	ThreadLocal<ITransaction> tx = new ThreadLocal<ITransaction>();


	@Override
	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public ITransaction begingTransaction() throws SQLException {
		ITransaction itx =null;
		if(tx.get()==null){
			itx = new TransactionImpl(ds.getConnection());
			tx.set(itx);
		}else{
			return tx.get();
		}
		return itx;
	}

	@Override
	public int execute(String sql, Object... param) throws SQLException {
		Connection conn= begigExecute();
		int rows =0;
		try{
		PreparedStatement statm= conn.prepareStatement(sql);
		int i=1;
		if(param!=null&&param.length>0)
		for(Object o :param){
			SQLValueUtil.setParameterValue(statm, i, o);
			i++;
		}
		//long bt =System.currentTimeMillis();
		 rows = statm.executeUpdate();
		//time=System.currentTimeMillis()-bt;
		statm.close();
		}catch(Exception e){
			throw new SQLException(e);
		}finally{
			endExecute(conn);
		}
		return rows;
	}
	
	@Override
	public long insertReturnKey(String sql, Object... param) throws SQLException {
		Connection conn= begigExecute();
		long id =-1;
		try{
		PreparedStatement statm= conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		int i=1;
		if(param!=null&&param.length>0)
		for(Object o :param){
			SQLValueUtil.setParameterValue(statm, i, o);
			i++;
		}
		//long bt =System.currentTimeMillis();
		statm.executeUpdate();
		ResultSet rst = statm.getGeneratedKeys();
        if(rst.next()) {
        	id = rst.getLong(1);
        }
		//time=System.currentTimeMillis()-bt;
		statm.close();
		}catch(Exception e){
			throw new SQLException(e);
		}finally{
			endExecute(conn);
		}
		return id;
	}
	
	private Connection begigExecute() throws SQLException{
		ITransaction itx=tx.get();
		if(itx!=null&&this.isOpenTransaction){
			return itx.getConnection();
		}else{
			conn=ds.getConnection();
			isAutoCommit=conn.getAutoCommit();
			conn.setAutoCommit(true);
			return conn;
		}
	}
	
	private void endExecute(Connection conn) throws SQLException{
		if(!this.isOpenTransaction){
			conn.setAutoCommit(isAutoCommit);
			conn.close();
			this.conn=null;
		}
	}

	@Override
	public int execute(String sql) throws SQLException {
		return execute(sql,null);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	class TransactionImpl implements ITransaction {
		private Connection conn;
		private boolean autoCommit;
		
		
		public Connection getConnection(){
			return conn;
		}

		public TransactionImpl(Connection conn) throws SQLException {
			this.conn = conn;
			autoCommit = conn.getAutoCommit();
			isOpenTransaction=true;
			conn.setAutoCommit(false);
		}

		private void revertConnection() throws SQLException {
			conn.setAutoCommit(autoCommit);
			isOpenTransaction=false;
			tx.remove();
		}

		@Override
		public void commit() throws SQLException {
			conn.commit();
			revertConnection();
		}

		public Savepoint setSavePiont() throws SQLException {

			return conn.setSavepoint();
		}

		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			conn.releaseSavepoint(savepoint);
		}

		public void rollback(Savepoint savepoint) throws SQLException {
			conn.rollback(savepoint);
			revertConnection();
		}

		@Override
		public void rollback() throws SQLException {
			conn.rollback();
			revertConnection();
		}

		@Override
		public ISQLQuery getISQLQuery() {
			SQLQuery imp = new SQLQuery();
			imp.setDataSource(new SingleConnectionDataSource(conn));
			return imp;
		}
		
		@Override
		public void dispose() throws SQLException {
			if(conn!=null){
				conn.close();
				conn=null;
			}
		}

		@Override
		public ISQLExecuter getSQLExecuter() {
			SQLExecuter exe = new SQLExecuter();
			exe.setDataSource(new SingleConnectionDataSource(conn));
			return exe;
		}
	}

	@Override
	public int tryExecute(String sql, Object... param) {
		try{
			return execute(sql,param);
		}catch(Exception e){
			e.printStackTrace();
			log.warn("tryExecute",sql,e,param);
		}
		return 0;
	}

	@Override
	public int tryExecute(String sql) {
		return tryExecute(sql,null);
	}

	@Override
	public IPojoExeuter pojo(Object o) {
		return new SQLPojoExeuter(this,o);
	}
	
	

}
