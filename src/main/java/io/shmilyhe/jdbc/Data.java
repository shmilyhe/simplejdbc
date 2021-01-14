package io.shmilyhe.jdbc;

import java.util.Collection;
import java.util.Iterator;

public class Data<T> implements Collection<T> {
	
	private Integer rows;
	private Integer page;
	private Integer pageSize;
	
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	private Collection<T> data;
	public Data(Collection<T> data) {
		this.data=data;
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data==null||data.size()==0;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		if(data==null)return null;
		return data.iterator();
	}

	@Override
	public Object[] toArray() {
		if(data==null)return null;
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if(data==null)return null;
		return data.toArray(a);
	}

	@Override
	public boolean add(T e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
		
	}

	

}
