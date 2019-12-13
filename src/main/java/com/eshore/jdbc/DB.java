package com.eshore.jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;



import com.eshore.jdbc.api.IQuery;
import com.eshore.jdbc.impl.SQLExecuter;
import com.eshore.jdbc.impl.SQLQuery;
import com.eshore.tools.Alias;


public class DB {
	
	public DB() {}
	
	public DB(DataSource ds) {
		this.ds=ds;
	}
	
	DataSource ds;
	
	private SQLQuery query;
	private SQLExecuter executer;
	
	public SQLQuery query() {
		if(query==null) {
			query= new SQLQuery();
			query.setDataSource(ds);
		}
		
		return query;
	}
	
	public IQuery query(String sql) {
		return query().query(sql);
	}
	
	public SQLExecuter exec() {
		if(executer==null) {
			executer =new SQLExecuter();
			executer.setDataSource(ds);
		}
		return executer;
	}
	
	
	/**
	 * 
	 * @param <T> 实体类型
	 * @param sql 执行SQL （参数的地方用?点分符）
	 * @param clazz 类
	 * @param page 页码
	 * @param pageSize 每页大小
	 * @param param 参数 按SQL ？的顺序组成数组
	 * @return 查询的结果（只有当前页数据）
	 */
	public <T>  Data<T> list(String sql,Class<T> clazz,Integer page,Integer pageSize,Object ...param){
		Map  alias=Alias.getAlias(clazz);
		IQuery q = query(sql)
				.alias(alias)
				.clazz(clazz)
				.param(param)
				.page(page, pageSize);
		List<T> list = q.list();
		Data<T>  data = new Data(list);
		data.setPage(page);
		data.setPageSize(pageSize);
		data.setRows(q.rows());
		return data;
	}
	
	/**
	 * @param <T> 实体类型
	 * @param sql 执行SQL （参数的地方用?点分符）
	 * @param clazz 类
	 * @param param 参数 按SQL ？的顺序组成数组
	 * @return 查询的结果
	 */
	public <T>  Collection<T> raw(String sql,Class<T> clazz,Object ...param){
		Map  alias=Alias.getAlias(clazz);
		return (Collection<T>) query(sql)
				.alias(alias)
				.clazz(clazz)
				.param(param)
				.raw();
	}
	
	/**
	 * 插入主键为自增类型的数据
	 * @param pojo 实体类实例
	 * @param table 表名
	 * @param idName id的属性名
	 * @return 数据库的ID
	 */
	public Long insertWithAutoId(Object pojo,String table,String idName) {
		Map alias=Alias.getAlias(pojo.getClass());
		Long id = (Long) exec().pojo(pojo).table(table)
				.generatedKey(idName)
				.alias(alias)
				.insert()
				.idValue();
		return id;
	}
	
	/**
	 * 插入非自增主键的数据
	 * @param pojo 实体类实例
	 * @param table 表名
	 * @param idName id属性名
	 * @return 返回执行结果
	 */
	public boolean insert(Object pojo,String table,String idName) {
		Map alias=Alias.getAlias(pojo.getClass());
		return exec().pojo(pojo).table(table)
				.id(idName)
				.alias(alias)
				.insert().success();
	}
	
	/**
	 * 更新实体数据
	 * @param pojo 实体类实例
	 * @param table 表名
	 * @param idName id属性名
	 * @return 返回执行结果
	 */
	public boolean update(Object pojo,String table,String idName) {
		Map alias=Alias.getAlias(pojo.getClass());
		return exec().pojo(pojo).table(table)
				.id(idName)
				.alias(alias)
				.update();
	}
	
	public boolean update(Object pojo,String table,String where,Object ...params) {
		Map alias=Alias.getAlias(pojo.getClass());
		return exec().pojo(pojo).table(table)
				.alias(alias)
				.update(where,params);
	}
	
	public Object findOne(String sql,Class<?> clazz,Object ...param){
		Map  alias=Alias.getAlias(clazz);
		return query(sql)
				.alias(alias)
				.clazz(clazz)
				.param(param)
				.findOne();		
	}

	
	

}
