package com.github.cryboy007.httpclient.constant;

/**
 *@InterfaceName SqlEntityConstant
 *@Author HETAO
 *@Date 2022/5/14 1:47
 */
public interface SqlEntityConstant {
	interface MTBlog {
		 String MT_BLOG_INSERT = "INSERT INTO example_test.mt_blog (link, title, create_time, tag) VALUES(?, ?, ?, ?)";
		 String GET_UP_TO_DATE_LINK = "SELECT title FROM example_test.mt_blog x \n"
				 + "ORDER BY x.create_time DESC limit 1";
	}
}
