package io.shmilyhe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.junit.Test;

import io.shmilyhe.ci.pojo.TbCdProblemRec;
import io.shmilyhe.jdbc.DB;
import io.shmilyhe.jdbc.SingleConnectionDataSource;

public class TestDB {

	@Test
	public void test1() throws SQLException{
		String driver ="org.sqlite.JDBC";
		String url="jdbc:sqlite:sample.db";
		
		SingleConnectionDataSource sds = new SingleConnectionDataSource();
		sds.setDriverClassName(driver);
		sds.setUrl(url);
		Connection conn =sds.getConnection();
		TbCdProblemRec tbCdProblemFeedback = new TbCdProblemRec();
		tbCdProblemFeedback.setCreateTime(new Date());
		tbCdProblemFeedback.setCreator(1);
		tbCdProblemFeedback.setCreatorName("sdfsdf");
		tbCdProblemFeedback.setProblemId(10);
	     DB db = new DB();
	    // Boolean status = db.update(tbCdProblemFeedback, "tb_cd_problem_feedback", "feedback_id");
	     Boolean status = db.update(tbCdProblemFeedback, "tb_cd_problem_rec", "problem_id");
	    }

}
