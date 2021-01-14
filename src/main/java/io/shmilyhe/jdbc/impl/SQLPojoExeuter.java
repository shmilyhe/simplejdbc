package io.shmilyhe.jdbc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import io.shmilyhe.jdbc.api.IPojoExeuter;
import io.shmilyhe.tools.JBeans;

public class SQLPojoExeuter implements  IPojoExeuter{
	private Object pojo;
	private SQLExecuter exeuter;
	String table;
	String[] ig;
	String id;
	Object idValue;
	boolean useNullValue=false;
	boolean success;
	private boolean ig(String col){
		if(ig==null)return false;
		for(String s :ig){
			if(s.equalsIgnoreCase(col))return true;
		}
		return false;
	}
	public SQLPojoExeuter(SQLExecuter exeuter,Object o){
		this.exeuter=exeuter;
		pojo=o;
	}

	@Override
	public IPojoExeuter insert() {
		StringBuilder sql = new StringBuilder();
		StringBuilder sqlvs = new StringBuilder();
		ArrayList plist = new ArrayList();
		try {
			Map dbmap =JBeans.toDBMap(pojo);
			sql.append("insert into ").append(table).append("(");
			Set<Map.Entry<String, Object>> es = dbmap.entrySet();
			boolean isFirst=true;
			if(es!=null)for(Map.Entry<String, Object> e:es){
				String col=e.getKey();
				if(col==null)continue;
				if(ig(col))continue;
				Object value=e.getValue();
				if(col.equalsIgnoreCase(id)&&value==null){
					if(gnerateId)continue;
					value=UUID.randomUUID().toString().replaceAll("\\-", "");
					idValue=value;
				}
				if(value==null&&!useNullValue)continue;
				if(isFirst){isFirst=false;}else{sql.append(',');sqlvs.append(',');}
				plist.add(value);
				col=getColumnName(col);
				sql.append(col);
				sqlvs.append("?");
				
			}
			sql.append(") values(").append(sqlvs).append(")");
			if(plist.size()==0)return this;
			if(gnerateId) {
				idValue=exeuter.insertReturnKey(sql.toString(), plist.toArray());
			}else {
				exeuter.execute(sql.toString(), plist.toArray());
			}
			success=true;
			return this;
		} catch (Exception e) {
			throw new RuntimeException(e);
			//e.printStackTrace();
		}
	}

	@Override
	public boolean update() {
		StringBuilder sql = new StringBuilder();
		ArrayList plist = new ArrayList();
		try {
			Map dbmap =JBeans.toDBMap(pojo);
			sql.append("update  ").append(table).append(" set ");
			Set<Map.Entry<String, Object>> es = dbmap.entrySet();
			boolean isFirst=true;
			if(es!=null)for(Map.Entry<String, Object> e:es){
				String col=e.getKey();
				if(col==null)continue;
				if(ig(col))continue;
				Object value=e.getValue();
				if(col.equalsIgnoreCase(id)||id.equalsIgnoreCase(getColumnName(col))){
					if(value==null)return false;
					idValue=value;
					continue;
				}
				if(value==null&&!useNullValue)continue;
				
				if(isFirst){isFirst=false;}else{sql.append(',');}
				plist.add(value);
				col=getColumnName(col);
				sql.append(col).append("=? ");
			}
			sql.append(" where ").append(id).append("=?");
			if(plist.size()==0)return false;
			plist.add(idValue);
			exeuter.execute(sql.toString(), plist.toArray());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public IPojoExeuter table(String name) {
		table=name;
		return this;
	}

	@Override
	public IPojoExeuter igore(String... ig) {
		this.ig=ig;
		return this;
	}

	@Override
	public IPojoExeuter id(String id) {
		this.id=id;
		return this;
	}
	@Override
	public IPojoExeuter useNullValue() {
		 useNullValue=true;
		 return this;
	}
	@Override
	public Object idValue() {
		return idValue;
	}
	@Override
	public boolean success() {
		return success;
	}
	
	Map alias;
	@Override
	public IPojoExeuter alias(Map amap) {
		this.alias=k2Upper(amap);
		return this;
	}
	
	private Map k2Upper(Map m){
		if(m==null)return null;
		Map nmap = new HashMap();
		for(Object o : m.entrySet()){
			Entry<String,String> e = (Entry<String, String>) o;
			String k=e.getKey();
			String v=e.getValue();
			
				k=k.toUpperCase();
				v=v.toUpperCase();
			
			nmap.put(k, v);
		}
		return nmap;
	}
	
	private String getColumnName(String name){
		if(alias==null)return name;
		String n=(String) alias.get(name);
		if(n!=null&&n.trim().length()>0)return n;
		return name;
	}
	
	boolean gnerateId=false;
	@Override
	public IPojoExeuter generatedKey(String id) {
		this.id=id;
		gnerateId=true;
		return this;
	}
	@Override
	public boolean update(String where, Object... param) {
		StringBuilder sql = new StringBuilder();
		ArrayList plist = new ArrayList();
		try {
			Map dbmap =JBeans.toDBMap(pojo);
			sql.append("update  ").append(table).append(" set ");
			Set<Map.Entry<String, Object>> es = dbmap.entrySet();
			boolean isFirst=true;
			if(es!=null)for(Map.Entry<String, Object> e:es){
				String col=e.getKey();
				if(col==null)continue;
				if(ig(col))continue;
				Object value=e.getValue();
				if(id!=null && (col.equalsIgnoreCase(id)||id.equalsIgnoreCase(getColumnName(col)))){
					if(value==null)return false;
					idValue=value;
					continue;
				}
				if(value==null&&!useNullValue)continue;
				
				if(isFirst){isFirst=false;}else{sql.append(',');}
				plist.add(value);
				col=getColumnName(col);
				sql.append(col).append("=? ");
			}
			sql.append(" where ");//.append(id).append("=?");
			sql.append(where);
			if(param!=null&&param.length>0) {
				for(Object o:param) {
					plist.add(o);
				}
			}
			exeuter.execute(sql.toString(), plist.toArray());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
