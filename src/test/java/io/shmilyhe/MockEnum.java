package io.shmilyhe;

import java.util.Enumeration;
import java.util.Iterator;

public class MockEnum<E> implements Enumeration<E> {

	Iterator<E> it;
	public MockEnum(Iterator<E> it){
		this.it=it;
	}
	@Override
	public boolean hasMoreElements() {
		// TODO Auto-generated method stub
		return it.hasNext();
	}

	@Override
	public E nextElement() {
		// TODO Auto-generated method stub
		return it.next();
	}

}
