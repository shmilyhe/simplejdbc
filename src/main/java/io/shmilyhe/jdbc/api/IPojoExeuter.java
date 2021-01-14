package io.shmilyhe.jdbc.api;

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
	 * update
	 * @return IPojoExeuter
	 */
	boolean update(String sql,Object ...param);
	
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
	 * 在插入操作时，设置自增的id请使用generatedKey方法
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
	
	/**
	 * 设置是否获取自增ID
	 * @param id id
	 * @return IPojoExeuter
	 */
	IPojoExeuter generatedKey(String id);
	
	
	
}
