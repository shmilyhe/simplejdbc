package io.shmilyhe;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class MockHttpSession implements HttpSession {

	long createTime=System.currentTimeMillis();
	HashMap<String,Object> map = new HashMap<String,Object>();
	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return createTime;
	}

	String id=UUID.randomUUID().toString();
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return map.get(name);
	}

	@Override
	public Object getValue(String name) {
		// TODO Auto-generated method stub
		return map.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return new MockEnum<String>(map.keySet().iterator());
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		String [] vns= new String[map.keySet().size()];
		map.keySet().toArray(vns);
		return vns;
	}

	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub,
		map.put(name, value);

	}

	@Override
	public void putValue(String name, Object value) {
		// TODO Auto-generated method stub
		map.put(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		map.remove(name);
	}

	@Override
	public void removeValue(String name) {
		// TODO Auto-generated method stub
		map.remove(name);
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

}
