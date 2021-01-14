package io.shmilyhe.jdbc.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.shmilyhe.jdbc.api.IPagination;

public class Pagination implements IPagination {

	@Override
	public String fixSQl(String sql,int page,int pageSize) {
		return sql;
	}

	@Override
	public String fixCountSQL(String sql) {
		return "select count(1) from (" +removeOrderBy(sql) + ") a";
	}

	@Override
	public int extract(int row, int page, int pageSize) {
		long begin = (page - 1) * pageSize;
		long end = page * pageSize;
		if (row < begin)return SKIP;
		if(row < end == false)return STOP;
		return VALUE;
	}

	Pattern p=Pattern.compile(".*(ORDER +BY).*");
	private String removeOrderBy(String sql) {
		String sql2=sql.toUpperCase();
		Matcher m = p.matcher(sql2);
		if(m.matches()) {
			String or=m.group(1);
			 sql =sql.substring(0,sql2.indexOf(or));
		}
		return sql;
	}

}
