package com.github.cryboy007.httpclient.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName XXLJobService
 * @Author tao.he
 * @Since 2022/7/8 15:18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class XXLJobService {

    private final RestTemplate restTemplate;

    public void killXxlJob (String str) {
        final String[] ids = str.split(",");

        HttpHeaders headers = new HttpHeaders();//header参数
        List<String> cookies = new ArrayList<>();
        cookies.add("XXL_JOB_LOGIN_IDENTITY" +
                "=7b226964223a312c22757365726e616d65223a2261646d696e222c2270617373776f7264223a226531306164633339343962613539616262653536653035376632306638383365222c22726f6c65223a312c227065726d697373696f6e223a6e756c6c7d");

// header设置
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
// cookie设置
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //post表单 ，如果是个json则设置为MediaType.APPLICATION_JSON


//        HttpEntity<String> httpEntity = new HttpEntity(headers);

        for (String id : ids) {
            MultiValueMap<String,String> param = new LinkedMultiValueMap<String, String>();//参数放入一个map中，restTemplate不能用hashMap
            HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<MultiValueMap<String,String>>(param,headers);
            param.add("id",id);

            String url = "https://e3mp.goldlion.com.cn:8443/xxl-job-admin/joblog/logKill";

            ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
            log.info("body:{}",responseEntity.getBody());
        }

    }
}
