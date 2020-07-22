package com.eshore.jdbc;

import java.sql.Blob;

import java.sql.Clob;

import java.sql.ResultSet;

import java.sql.ResultSetMetaData;

import java.sql.SQLException;

import java.util.HashMap;

import java.util.Map;

import com.eshore.tools.JBeans;


public class BeanRowMapper {
	private Class<?>clazz;

	public BeanRowMapper(Class<?>type){

		clazz=type;

	}





	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData();

		int columnCount = rsmd.getColumnCount();

		Map<String, Object> mapOfColValues = new HashMap();

		for (int i = 1; i <= columnCount; i++) {

			String key = lookupColumnName(rsmd, i);

			key=key.toUpperCase();

			Object obj = getResultSetValue(rs, i);

			mapOfColValues.put(key, obj);

		}

		

		try {

			return JBeans.getBean(mapOfColValues, clazz);

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

		}

		else if (obj instanceof Clob) {

			obj = rs.getString(index);

		}

		else if (className != null &&

				("oracle.sql.TIMESTAMP".equals(className) ||

				"oracle.sql.TIMESTAMPTZ".equals(className))) {

			obj = rs.getTimestamp(index);

		}

		else if (className != null && className.startsWith("oracle.sql.DATE")) {

			String metaDataClassName = rs.getMetaData().getColumnClassName(index);

			if ("java.sql.Timestamp".equals(metaDataClassName) ||

					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {

				obj = rs.getTimestamp(index);

			}

			else {

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

}
