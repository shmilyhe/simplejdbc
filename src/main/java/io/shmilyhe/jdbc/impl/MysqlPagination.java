package io.shmilyhe.jdbc.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.shmilyhe.jdbc.api.IPagination;

/**
 * Pagination for mysql
 * @author eric
 *
 */
public class MysqlPagination implements IPagination {

	@Override
	public String fixSQl(String sql, int page, int pageSize) {
		if (pageSize < 1)pageSize = 10;
		if (page < 1)page = 1;
		int begin = (page - 1) * pageSize;
		sql = sql + " limit " + begin + "," + pageSize;
		return sql;

	}
	
	@Override
	public String fixCountSQL(String sql) {
		return "select count(*) from (" +removeOrderBy( sql) + ") a";
	}

	@Override
	public int extract(int row, int page, int pageSize) {
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
