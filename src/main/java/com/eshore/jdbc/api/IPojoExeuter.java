package com.eshore.jdbc.api;

import java.util.Map;

/**
 * 
 * @author eric
 *
 */
public interface IPojoExeuter {
	/**
	 * insert data into db
	 * @return IPojoExeuter
	 */
	IPojoExeuter insert();
	
	/**
	 * update
	 * @return IPojoExeuter
	 */
	boolean update();
	
	/**
	 * 设置表名
	 * set table name
	 * @param name table name 表名
	 * @return IPojoExeuter
	 */
	IPojoExeuter table(String name);
	
	/**
	 * 设置忽略的字段
	 * @param ig igore columns
	 * @return IPojoExeuter
	 */
	IPojoExeuter igore(String ...ig);
	
	/**
	 * 设置id 字段
	 * @param id id
	 * @return IPojoExeuter
	 */
	IPojoExeuter id(String id);
	
	/**
	 * 设置使用空值，在update 或insert 时空值将更新到数据库
	 * @return IPojoExeuter
	 */
	IPojoExeuter useNullValue();
	
	/**
	 * 获取id 值
	 * @return id值
	 */
	Object idValue();
	
	/**
	 * 是否成功
	 * @return 是否成功
	 */
	boolean success();
	IPojoExeuter alias(Map amap);
}
