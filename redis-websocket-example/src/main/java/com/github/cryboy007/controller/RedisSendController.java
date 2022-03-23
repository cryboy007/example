package com.github.cryboy007.controller;

import com.github.cryboy007.constant.RedisConstant;
import com.github.cryboy007.to.MessageTo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName RedisSendController
 * @Author tao.he
 * @Since 2022/1/20 11:38
 */
@RestController
@RequestMapping("/redis")
public class RedisSendController {
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("send")
    public String sendRedis() {
        MessageTo messageTo = new MessageTo();
        messageTo.setUserId(1L);
        messageTo.setMessage("有新库存入库了----》》》");
        redisTemplate.convertAndSend(RedisConstant.MESSAGE_PROGRESS_CHANNEL,messageTo);
        return "success";
    }
}
