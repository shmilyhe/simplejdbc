package io.shmilyhe.jdbc.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import io.shmilyhe.jdbc.impl.SQLExecuter;

public interface ITransaction {
	void commit()throws SQLException;
	void rollback()throws SQLException;
	
	Savepoint setSavePiont() throws SQLException;
	
	void releaseSavepoint(Savepoint savepoint) throws SQLException;
	
	void rollback(Savepoint savepoint)throws SQLException;
	
	ISQLExecuter getSQLExecuter();
	ISQLQuery getISQLQuery();
	
	public Connection getConnection();
	
	void dispose()throws SQLException;
}
