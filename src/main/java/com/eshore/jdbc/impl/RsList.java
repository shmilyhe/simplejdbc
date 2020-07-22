package com.eshore.jdbc.impl;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.eshore.tools.JBeans;
import com.eshore.jdbc.api.IPagination;

/**
 * 
 * @author eric
 *一个RestultSet 只读列表
 *查询的RestultSet封装成一个list
 * @param <E>
 */
public class RsList<E> implements List<E> {
	
	ResultSet rs;
	IPagination p;
	PreparedStatement statm;
	int page;
	int pageSize;
	String[] ig;
	Class<?> clazz;
	Map alias;
	
	public Map getAlias() {
		return alias;
	}

	private Map v2k(Map m,boolean upper){
		if(m==null)return null;
		Map nmap = new HashMap();
		for(Object o : m.entrySet()){
			Entry<String,String> e = (Entry<String, String>) o;
			String k=e.getValue();
			String v=e.getKey();
			if(upper){
				k=k.toUpperCase();
				v=v.toUpperCase();
			}
			nmap.put(k, v);
		}
		return nmap;
	}
	
	public void setAlias(Map alias) {
		this.alias =v2k(alias,true);
	}

	/**
	 * 
	 * @param rs RestultSet
	 * @param p Pagination 分页器
	 * @param statm PreparedStatement
	 * @param clazz 转换类，如果为空则转换为Map
	 * @param page 页码
	 * @param pageSize 分页大小
	 * @param ig 不处理的字段
	 */
	public RsList(ResultSet rs,IPagination p,PreparedStatement statm,Class<?> clazz,int page,int pageSize,String[] ig){
		this.rs=rs;
		this.p=p;
		this.statm=statm;
		this.page=page;
		this.pageSize=pageSize;
		this.clazz=clazz;
		this.ig=ig;
	}

	private boolean igore(String key){
		if(ig==null)return false;
		for(String s:ig){
			if(s==null)continue;
			if(s.equalsIgnoreCase(key))return true;
		}
		return false;
	}
	
	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		throw new RuntimeException("method not support!");
	}

	@Override
	public boolean contains(Object o) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public Iterator<E> iterator() {
		return new  Iterator(){
			int row=0;
			@Override
			public boolean hasNext() {
				if(p==null||pageSize==0)
					try {
						return rs.next();
					} catch (SQLException e1) {
						return false;
					}
				int status = p.extract(row, page, pageSize);
				if(status==IPagination.SKIP){
					for(;p.extract(row, page, pageSize)==IPagination.SKIP;row++){
						try {
							if(!rs.next()){
								return false;
							}
						} catch (SQLException e) {
						}
					}
					status = p.extract(row, page, pageSize);
				}
				if(status==IPagination.VALUE)
					try {
						return rs.next();
					} catch (SQLException e) {
						return false;
					}
				return false;
			}
			

			@Override
			public Object next() {
				try {
					row++;
					return mapRow(rs,clazz);
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			
		};
	}

	@Override
	public Object[] toArray() {
		ArrayList list = new ArrayList();
		for(Object o:this){
			list.add(o);
		}
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return  (T[]) toArray();
	}

	@Override
	public boolean add(E e) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public boolean remove(Object o) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public void clear() {
		if(q!=null)
			try {
				q.close();
				q=null;
			}catch(Exception e) {}
		if(rs!=null)
		try {
			if(!rs.isClosed())
			rs.close();
			rs=null;
		} catch (SQLException e) {
			rs=null;
		}
		if(statm!=null)
		try {
			if(!statm.isClosed())
			statm.close();
			statm=null;
		} catch (SQLException e) {
			statm=null;
		}
	}
	
	public Object mapRow(ResultSet rs, Class<?>clazz) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> mapOfColValues = new HashMap();
		for (int i = 1; i <= columnCount; i++) {
			String key = lookupColumnName(rsmd, i);
			if(igore(key))continue;
			key=key.toUpperCase();
			if(alias!=null){
				String k=(String)alias.get(key);
				if(k!=null&&k.trim().length()>0){
					key=k;
				}
			}
			Object obj = getResultSetValue(rs, i);
			mapOfColValues.put(key, obj);
		}


		if(clazz==null)return mapOfColValues;
		try {
			return  JBeans.getBean(mapOfColValues, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 

	}



	public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {

		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}

		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		}else if (obj instanceof Clob) {
			obj = rs.getString(index);
		}else if (className != null &&
				("oracle.sql.TIMESTAMP".equals(className) ||
				"oracle.sql.TIMESTAMPTZ".equals(className))) {
			obj = rs.getTimestamp(index);
		}else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) ||
					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			}else {
				obj = rs.getDate(index);
			}

		}

		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
	}



	public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1) {
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;

	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public E get(int index) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public E set(int index, E element) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public void add(int index, E element) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public E remove(int index) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public int indexOf(Object o) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new RuntimeException("method not support!");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new RuntimeException("method not support!");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("method not support!");
	}

	public Query getQ() {
		return q;
	}

	public void setQ(Query q) {
		this.q = q;
	}

	private Query q;
	
}
