package com.github.cryboy007.httpclient.controller;

import com.github.cryboy007.httpclient.example.MTBlogExample;
import com.github.cryboy007.httpclient.example.XXLJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *@ClassName JsoupController
 *@Author tao.he
 *@Since 2022/5/14 0:17
 */
@RestController
@RequiredArgsConstructor
public class JsoupController {
	private final MTBlogExample mtBlogExample;
	private final XXLJobService xxlJobService;

	@GetMapping("getBlog")
	public void getMTBlog() {
		mtBlogExample.getBlog();
	}

	@PostMapping("killJob")
	public void killXxlJob(@RequestParam("str") String str) {
		xxlJobService.killXxlJob(str);
	}
}
