package com.agoda.interceptor;

import com.agoda.RateLimitManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Created by Andrey Kapitonov on 25.11.2015.
 */
@Component
public class RateLimitInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LogManager.getLogger(RateLimitInterceptor.class);

    @Autowired
    private RateLimitManager rateLimitManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = false;

        log.debug("hello from interceptor");

        return true;
    }
}
