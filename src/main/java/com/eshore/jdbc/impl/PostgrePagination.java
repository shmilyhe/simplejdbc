package com.eshore.jdbc.impl;

import com.eshore.jdbc.api.IPagination;

/**
 * Pagination for Postgre
 * @author eric
 *
 */
public class PostgrePagination implements IPagination {

	@Override
	public String fixSQl(String sql, int page, int pageSize) {
		if (pageSize < 1)pageSize = 10;
		if (page < 1)page = 1;
		int begin = (page - 1) * pageSize;
		sql = sql + " limit " +pageSize + " offset "+ begin;
		return sql;
	}
	
	@Override
	public String fixCountSQL(String sql) {
		return "select count(*) from (" + sql + ") a";
	}

	@Override
	public int extract(int row, int page, int pageSize) {
		return VALUE;
	}

}