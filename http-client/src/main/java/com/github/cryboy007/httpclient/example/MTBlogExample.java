package com.github.cryboy007.httpclient.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.github.cryboy007.httpclient.constant.SqlEntityConstant;
import com.overzealous.remark.Remark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import static com.github.cryboy007.httpclient.constant.URLConstant.MEITUAN_BLOG;

/**
 * @ClassName MTBlogExample
 * @Author tao.he
 * @Since 2022/5/13 18:02
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MTBlogExample {
	private final RestTemplate restTemplate;
	private final JdbcTemplate jdbcTemplate;
	private final Remark remark;
	private final ClassPathXmlApplicationContext classPathXmlApplicationContext;

	public void getBlog() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("爬取美团文章");
		recursion(1);
		stopWatch.stop();
		log.info("执行完毕{}",stopWatch.prettyPrint());

	}

	public void recursion(int pageCount) {
		String url = MEITUAN_BLOG;
		if (pageCount > 1) url = MEITUAN_BLOG + "/page/" + pageCount + ".html";
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
			StringBuilder sb = appendPrefix(pageCount);
			//System.out.print(sb);
			for (Element element : elements) {
				String link = element.select("a").get(0).attr("href");
				String title = element.select(".post-title").text();
				String date = element.select(".m-post-date").text();
				String tag = element.select(".tag-links [rel=tag]").stream().map(Element::text)
						.collect(Collectors.joining(","));
				if (StringUtils.isEmpty(title)) {
					continue;
				}
				try {
					try {
						String oldTitle = jdbcTemplate.queryForObject(SqlEntityConstant.MTBlog.GET_UP_TO_DATE_LINK, String.class);
						if (Objects.requireNonNull(oldTitle).equals(title)) {
							log.info("同步完毕---》");
							return;
						}
					}catch (EmptyResultDataAccessException ignored) {
						log.info("第一次备份+++");
					}
					jdbcTemplate.update(SqlEntityConstant.MTBlog.MT_BLOG_INSERT,link,title, DateUtils.parseDate(date,"yyyy年MM月dd日"),tag);
					Document page = Jsoup.connect(link).userAgent("Mozilla").timeout(6000).get();
					String html = page.select(".post-title,.meta-box,.post-content,[class='meta-box post-bottom-meta-box hidden-print']")
							.outerHtml();
					String mdString = remark.convert(html);
					//上传至远端 todo
					String path = "C:\\gitHub_Project\\example\\http-client\\src\\main\\resources\\markdown";
					OutputStream outputStream = new FileOutputStream(new File(path, removeInvalidSymbol(title) + ".md"));
					IOUtils.write(mdString,outputStream, StandardCharsets.UTF_8);
				}
				catch (ParseException e) {
					log.info("标题:{},Date:{}",title,date,e);
				}
				catch (IOException e) {
					log.info("IOException",e);
				}
//				System.out.print(link +"\t");
//				System.out.println(content);
			}
			log.info("当前页码:{}",pageCount);
			if (document.select(".pagination .disabled").size() == 2) return;
			this.recursion(++pageCount);
		}
	}

	private String removeInvalidSymbol(String title) {
		Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
		Matcher matcher = pattern.matcher(title);
		return matcher.replaceAll("");
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
