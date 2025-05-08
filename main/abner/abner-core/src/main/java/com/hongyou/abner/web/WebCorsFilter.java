///*
// * Copyright 2024, Hongyou Software Development Studio.
// */
//package com.hongyou.baron.web.filters;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * CORS跨域访问过滤器 @todo 先取消跨域配置，暂时不删除代码，等以后项目中用到后再删除
// *
// * @author Berlin
// */
//@Component
//public class WebCorsFilter implements Filter {
//
//    /**
//     * 允许CORS跨域访问
//     *
//     * @param request HTTP请求
//     * @param response HTTP想要
//     * @param chain 过滤器链
//     */
//    @Override
//    public void doFilter(
//            final ServletRequest request, final ServletResponse response, final FilterChain chain
//    ) throws IOException, ServletException {
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
//        httpResponse.setHeader("Access-Control-Max-Age", "3600");
//
//        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
//            httpResponse.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//}
