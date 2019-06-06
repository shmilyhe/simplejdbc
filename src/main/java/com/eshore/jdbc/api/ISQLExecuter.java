package com.eshore.jdbc.api;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

public interface ISQLExecuter {
	void setDataSource(DataSource ds);
	ITransaction begingTransaction()throws SQLException;
	int execute(String sql,Object... param)throws SQLException;
	int execute(String sql)throws SQLException;
	
	int tryExecute(String sql,Object... param);
	int tryExecute(String sql);
	IPojoExeuter pojo(Object o);
	
}