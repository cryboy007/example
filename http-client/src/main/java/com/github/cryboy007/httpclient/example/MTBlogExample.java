package com.github.cryboy007.httpclient.example;

import lombok.RequiredArgsConstructor;
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

    public void getBlog() {
        final String page = restTemplate.getForObject(MEITUAN_BLOG, String.class);
        System.out.println(page);
    }
}
