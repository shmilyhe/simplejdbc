package com.eshore.jdbc.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author eric
 *
 */
public interface IQuery {
	/**
	 * 设置分页
	 * @param page 分页
	 * @param pageSize 分页大小 
	 * @return IQuery
	 */
	IQuery page(int page,int pageSize);
	
	/**
	 * 设置SQL参数
	 * @param objs params
	 * @return IQuery
	 */
	IQuery param(Object ...objs);
	
	/**
	 * 设置查询Pojo类型
	 * @param clazz 类型
	 * @return IQuery
	 */
	IQuery clazz(Class<?> clazz);
	
	/**
	 *  设置忽略的字段
	 * @param ig 忽略的字段
	 * @return IQuery
	 */
	IQuery igore(String ...ig);
	
	/**
	 * 获取查询结果
	 * @return 查询结果
	 */
	List list();
	
	
	/**
	 * 设置返回结果为数组，如果设置
	 * @return
	 */
	IQuery array();
	
	/**
	 * 设置查询的list是按游标读取的list,实际上是resultSet 的封装，
	 * connection 并没有关闭，请务必手动调用list.clear()方法释放连接资源。
	 * 这个方法适合目标查询的数据行数很多，需要逐条处理的这样的批处理情景。
	 * 重要的事情说三遍，记得clear()，记得clear()，记得clear()
	 * @return collection 
	 */
	Collection<Object> raw();
	
	/**
	 * 获取第一个值
	 * @return 第一个值
	 */
	Object findOne();
	
	/**
	 * 获取第一行第一列值的整数值
	 * @return 整数值
	 */
	int asInt();
	/**
	 * 获取总行数
	 * @return 总行数
	 */
	int rows();
	
	/**
	 * 获取总页数
	 * @return 总页数
	 */
	int pages();
	
	IQuery alias(Map amap);

}
