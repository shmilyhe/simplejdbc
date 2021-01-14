package io.shmilyhe.khala.utils;

import java.util.Collection;
import java.util.Iterator;


/**
 * 一个简单的固写长度的集合
 * @author eshore
 *FIFO
 * @param <T> 保存实列的类型
 */
public class FixedQueue<T> implements Collection<T>{
	private int max;//最大长度
	private T[] vals;//保存值
	public FixedQueue(){
		this(65535);
	}
	int size;//大小
	
	/**
	 * 
	 * @param max 因定的长度
	 */
	public FixedQueue(int max){
		this.max=max;
		vals=(T[]) new Object[max];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size<1;
	}

	@Override
	public boolean contains(Object o) {
		for(Object t :this){
			if(t==null)continue;
			if(t.equals(o))return true;
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		
		return new Iterator(){
			int s=start;
			int len=size;
	
			@Override
			public boolean hasNext() {
				return len>0;
			}

			@Override
			public Object next() {
				if(s>=size){
					s=s-size;
				}
				Object o=vals[s];
				s++;
				len--;
				return o;
			}
			
		};
	}

	@Override
	public Object[] toArray() {
		int i=0;
		Object [] os = new Object[size];
 		for(Object o:this){
			os[i++]=o;
		}
		return os;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		int i=0;
 		for(Object o:this){
 			if(i>=a.length)break;
			a[i++]=(T) o;
		}
 		return a;
	}

	int start;
	int end;
	
	private synchronized  int getAddIndex(){
		if(size<max){//append
			int e = end++;
			size++;
			if(e<max)return e;
			end=0;
			return  end++;
		}else{//move
			int s =start;
			int e=end;
			if(s>=max)s=s-max;
			if(e>=max)e=e-max;
			start=s+1;
			end=e+1;
			return e;
		}
	}
	@Override
	public boolean add(T e) {
		vals[getAddIndex()]=e;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		if(c==null||c.isEmpty())return false;
		for(Object o:c){
			if(!this.contains(o))return false;
		}
		return true;
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
		for(int i=0;i<vals.length;i++){
			vals[i]=null;
		}
		start=0;
		end=0;
		size=0;
	}

	public static void main(String[] args){
		FixedQueue fq = new FixedQueue(5);
		fq.add(1);
		fq.add(2);
		fq.add(3);
		fq.add(4);
		fq.add(5);
		fq.add(6);
		fq.add(7);
		fq.add(8);
		//System.out.println(JsonString.asJsonString(fq));
		for(Object o:fq){
			System.out.println(o);
		}
	}
	
}
