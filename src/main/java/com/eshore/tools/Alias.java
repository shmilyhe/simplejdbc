package com.eshore.tools;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class Alias {

	public static void main(String[] args) {
		
		System.out.println(db2pro("mq__admin__detail"));
		System.out.println(pro2db("mqABGFGdminDetail"));
		

		}
		
	
		static Map<Class,Map> alias= new HashMap();
	
		public static Map<String,String> getAlias(Class clazz) {
			Map<String,String> map = alias.get(clazz);
			if(map!=null)return map;
			map=	new HashMap<String,String>();
			alias.put(clazz,map);
			
			Method[] method = clazz.getMethods();
			/**
			 * 给实例填充
			 */

			for (int i = 0; i < method.length; i++) {
				Method meth = method[i];
				String name =meth.getName();
				/**
				 * 当不是set的
				 */
				if (!name.startsWith("set"))
					continue;
				name=name.substring(3);
				String db=pro2db(name);
				String pro=firstLower(name);
				if(!db.equals(pro))
				map.put(pro,db);
			}
			return map;
		}
		public static String db2pro(String str) {
			char[] cs= str.toCharArray();
			boolean up=false;
			int index=0;
			for(int i=0;i<cs.length;i++) {
				char c =cs[i];
				if(c=='_') {
					up=true;
				}else {
					if(up) {
						cs[index++]=toUpper(c);
						up=false;
					}else {
						cs[index++]=c;
					}
				}
			}
			return new String(cs,0,index);
		}
		public static String firstLower(String str) {
			if(str==null&&str.length()==0)return str;
			char[] cs= str.toCharArray();
			cs[0]=toLower(cs[0]);
			return new String(cs);
		}
		
		public static String firstUpper(String str) {
			if(str==null&&str.length()==0)return str;
			char[] cs= str.toCharArray();
			cs[0]=toUpper(cs[0]);
			return new String(cs);
		}
		
		
		public static String pro2db(String str) {
			char[] cs= str.toCharArray();
			char[] cs2= new char[cs.length+10];
			int index=0;
			char c1=0;
			for(int i=0;i<cs.length;i++) {
				char c =cs[i];
				if(isUpper(c)) {
					if(i==0) {
						cs2[index++]=toLower(c);
					}else if(isUpper(c1)) {
						cs2[index++]=c;
					}else {
						cs2[index++]='_';
						cs2[index++]=toLower(c);
					}
				}else {
					cs2[index++]=c;
				}
				c1=c;
			}
			return new String(cs2,0,index);
		}
		public static boolean isUpper(char c) {
			return c>64&&c<91;
		}
		
		public static char toUpper(char c) {
			if(c>96&&c<123)return (char)(c-32);
			return c;
		}
		
		public static char toLower(char c) {
			if(c>64&&c<91)return (char)(c+32);
			return c;
		}

}
