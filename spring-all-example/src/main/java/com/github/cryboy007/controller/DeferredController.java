package com.github.cryboy007.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName DeferredController
 * @Author tao.he
 * @Since 2022/3/30 11:03
 */
@RestController
@Slf4j
public class DeferredController {

    private static volatile ConcurrentHashMap<String, DeferredResult<String>> DEFERRED_RESULT = new ConcurrentHashMap<>(20000);
    private static volatile AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @GetMapping("queryOrder")
    public DeferredResult<String> queryOrder(@RequestParam("orderId") String orderId,@RequestParam("delay") Long delayTime) {
        log.info("订单orderId:[{}]发起了支付", orderId);
        DeferredResult<String> deferredResult = new DeferredResult<>(3000L);
        //超时
        deferredResult.onTimeout(() -> {
            log.info("订单超时orderId:[{}]",orderId);
            DEFERRED_RESULT.get(orderId).setResult("超时了");
        });

        // 完成操作
        deferredResult.onCompletion(() -> {
            log.info("订单orderId:[{}]完成.", orderId);
            DEFERRED_RESULT.remove(orderId);
        }); // 保存此 DeferredResult 的结果
        DEFERRED_RESULT.put(orderId, deferredResult);
        return deferredResult;
    }

    /**
     * 支付回调
     *
     * @param orderId 订单id
     * @return 支付回调结果
     */
    @GetMapping("payNotify")
    public String payNotify(@RequestParam("orderId") String orderId) {
        log.info("订单orderId:[{}]支付完成回调", orderId);

        // 默认结果发生了异常
        if ("123".equals(orderId)) {
            DEFERRED_RESULT.get(orderId).setErrorResult(new RuntimeException("订单发生了异常"));
            return "回调处理失败";
        }

        if (DEFERRED_RESULT.containsKey(orderId)) {
            Optional.ofNullable(DEFERRED_RESULT.get(orderId)).ifPresent(result -> result.setResult("完成支付"));
            // 设置之前orderId toPay请求的结果
            return "回调处理成功";
        }
        return "回调处理失败";
    }
}
