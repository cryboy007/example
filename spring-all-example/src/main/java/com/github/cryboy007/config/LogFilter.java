package com.github.cryboy007.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName LogFilter
 * @Author tao.he
 * @Since 2022/3/30 14:15
 */
@Component
@WebFilter(filterName = "MscObjectFilter",urlPatterns = "/*")
@Slf4j
public class LogFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            implantationMDC(servletRequest);
            filterChain.doFilter(servletRequest,servletResponse);
            removerMDC();
        } catch (Exception e) {
            log.error("处理请求有误");
        }
    }

    /*private String user_id;
    private String operation;
    private String threadId;
    private String ip;
    private String url;
    private String params;*/

    public void implantationMDC(ServletRequest request){
        try {
            //MDC.put("user_id", String.valueOf();
            MDC.put("operation", String.valueOf(((HttpServletRequest) request).getRequestURL()));
            MDC.put("threadId", String.valueOf(UUID.randomUUID()));
           // MDC.put("ip", WXPayUtil.getIp((HttpServletRequest)request));
            MDC.put("url",String.valueOf(((HttpServletRequest) request).getRequestURL()));
            MDC.put("params", String.valueOf(request.getParameterMap()));
        } catch (Exception e) {
            //logger.error("添加MDC信息异常",e);
        }
    }

    public void removerMDC(){
        try {
            MDC.remove("user_id");
            MDC.remove("operation");
            MDC.remove("threadId");
            MDC.remove("ip");
            MDC.remove("url");
            MDC.remove("params");
        } catch (Exception e) {
            log.error("删除MDC信息异常",e);
        }
    }
}
