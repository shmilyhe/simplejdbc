package io.shmilyhe.jdbc.api;
/**
 * 分页接口
 * @author eric
 *
 */
public interface IPagination {

	//跳过
	public static int SKIP=0;
	//停止
	public static int STOP=1;
	//获取值
	public static int VALUE=2;
	
	/**
	 * 获取分页sql
	 * @param sql sql
	 * @param page page 页码
	 * @param pageSize pageSize 分页大小
	 * @return PaginationSQL 分页sql
	 */
	public String fixSQl(String sql,int page,int pageSize) ;
	
	/**
	 * 获取统计条数SQL
	 * @param sql sql
	 * @return countSQL 统计结果的SQL
	 */
	public String fixCountSQL(String sql) ;
	
	/**
	 * 是否取值
	 * @param row rowIndex 行号
	 * @param page page 页码
	 * @param pageSize pageSize 分页大小
	 * @return isExtract 是否取值
	 */
	public int extract(int row,int page,int pageSize);
	
	

}
