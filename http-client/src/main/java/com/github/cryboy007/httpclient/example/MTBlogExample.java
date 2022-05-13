package com.github.cryboy007.httpclient.example;

import java.io.IOException;
import java.text.ParseException;
import java.util.stream.Collectors;

import com.github.cryboy007.httpclient.constant.SqlEntityConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.github.cryboy007.httpclient.constant.URLConstant.MEITUAN_BLOG;

/**
 * @ClassName MTBlogExample
 * @Author tao.he
 * @Since 2022/5/13 18:02
 */
@RequiredArgsConstructor
@Service
public class MTBlogExample {
	private final RestTemplate restTemplate;
	private final JdbcTemplate jdbcTemplate;

	public void getBlog() {
		recursion(1);
	}

	public void recursion(int page) {
		String url = MEITUAN_BLOG;
		if (page > 1) url = MEITUAN_BLOG + "/page/" + page + ".html";
		Document document = null;
		try {
			document = Jsoup.connect(url).userAgent("Mozilla").timeout(6000).get();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		assert document != null;
		Elements elements = document.select(".post-container");
		if (elements.size() >1) {
			StringBuilder sb = appendPrefix(page);
			//System.out.print(sb);
			for (Element element : elements) {
				String link = element.select("a").get(0).attr("href");
				String title = element.select(".post-title").text();
				String date = element.select(".m-post-date").text();
				String tag = element.select(".tag-links [rel=tag]").stream().map(Element::text)
						.collect(Collectors.joining(","));
				try {
					jdbcTemplate.update(SqlEntityConstant.MTBlog.MT_BLOG_INSERT,link,title, DateUtils.parseDate(date,"yyyy年MM月dd日"),tag);
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
//				System.out.print(link +"\t");
//				System.out.println(content);
			}
			if (document.select(".pagination .disabled").size() == 2) return;
			this.recursion(++page);
		}
	}

	private StringBuilder appendPrefix(int page) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 30; i++) {
			if (50 / 2 == i) {
				sb.append("第").append(page).append("页");
				sb.append("\n");
				return sb;
			}
			sb.append("  ");
		}
		return sb;
	}
}
