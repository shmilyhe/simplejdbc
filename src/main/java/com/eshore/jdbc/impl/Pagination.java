package com.eshore.jdbc.impl;

import com.eshore.jdbc.api.IPagination;

public class Pagination implements IPagination {

	@Override
	public String fixSQl(String sql,int page,int pageSize) {
		return sql;
	}

	@Override
	public String fixCountSQL(String sql) {
		return "select count(1) from (" + sql + ") a";
	}

	@Override
	public int extract(int row, int page, int pageSize) {
		long begin = (page - 1) * pageSize;
		long end = page * pageSize;
		if (row < begin)return SKIP;
		if(row < end == false)return STOP;
		return VALUE;
	}

}
