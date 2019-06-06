package com.eshore.jdbc.impl;

import com.eshore.jdbc.api.IPagination;

/**
 * Pagination for oracle
 * @author eric
 *
 */
public class OraclePagination implements IPagination {

	
	@Override
	public String fixSQl(String sql, int page, int pageSize) {
		if (pageSize < 1)pageSize = 10;
		if (page < 1)page = 1;
		int begin = (page - 1) * pageSize;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (");
		sb.append(sql);
		sb.append(") A WHERE ROWNUM <= ").append(begin+pageSize).append (" ) WHERE RN >=").append(begin);
		return sb.toString();
	}
	
	@Override
	public String fixCountSQL(String sql) {
		return "select count(1) from (" + sql + ") a";
	}

	@Override
	public int extract(int row, int page, int pageSize) {
		return VALUE;
	}
}
