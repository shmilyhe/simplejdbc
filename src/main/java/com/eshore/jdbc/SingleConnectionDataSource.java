package com.eshore.jdbc;

import javax.sql.DataSource;

import com.eshore.jdbc.impl.WrapperConnction;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class SingleConnectionDataSource implements DataSource {

		//驱动名
		private String driverClassName="com.mysql.jdbc.Driver";
		//连接路径
		private String url="jdbc:mysql://localhost:3306/vote?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
		//链接用户名
		private String username=null;
		//连接密码
		private String password=null;

		boolean sigle;
		Connection conn;

		
	public	SingleConnectionDataSource(Connection conn){
		this.conn=notCloseConnction(conn);
	}
	
	long lastTime=System.currentTimeMillis();


	public void setDriverClassName(String driverClassName){
		this.driverClassName=driverClassName;
	}

	public void setUsername(String username){
		this.username=username;
	}

	public void setUrl(String url){
		this.url=url;
	}

	public void setPassword(String password){
		this.password=password;
	}

	public boolean isSigle() {
			return sigle;
		}

		public void setSigle(boolean sigle) {
			this.sigle = sigle;
		}

	public SingleConnectionDataSource(String userName, String password, String url, String driver){
		this.url=url;
		this.username=userName;
		this.password=password;
		this.driverClassName=driver;
	}

	public SingleConnectionDataSource(){}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	private Connection notCloseConnction(Connection co){
		Connection c = new WrapperConnction(co){
			@Override
			public void close() throws SQLException {
				// not thing todo
			}
			
		};
		return c;
	}
	public Connection getConnection() throws SQLException {
		if(System.currentTimeMillis()-lastTime>6000000){
			conn.close();
			conn=null;
			lastTime=System.currentTimeMillis();
		}
		if(conn!=null) return conn;
		try {
			Class.forName(this.driverClassName);
		} catch (ClassNotFoundException e) {
			//System.out.println("找不到驱动程序类 ，加载驱动失败！");
			throw new RuntimeException(e);
		}
		
		if(conn==null)
		try {
			{
			conn =notCloseConnction(DriverManager.getConnection(this.url,
					this.username, this.password));
			}

		} catch (SQLException se) {
			System.out.println("数据库连接失败！");
			throw new RuntimeException(se);
		}


		return conn;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return getConnection();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}