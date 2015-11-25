package com.agoda.interceptor;

import com.agoda.RateLimitManager;
import com.agoda.exception.InvalidApiKeyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private static final int ERROR_CODE_CALL_LIMIT_EXCEEDED = 429;
    private static final int ERROR_CODE_UNKNOWN_ERROR = 400;
    private static final int ERROR_CODE_INVALID_API_KEY = 403;

    @Value("${api.key.header.name}")
    private String apiKeyHeaderName;

    @Autowired
    private RateLimitManager rateLimitManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = false;

        String sessionId = request.getSession().getId();
        String apiKey = request.getHeader(apiKeyHeaderName);

        try {
            RateLimitManager.ClientState clientState = rateLimitManager.updateClientState(sessionId, apiKey);
            if (clientState != null) {
                if (clientState.isPostponed()) {
                    String message = "api calls limit exceeded, you will be postponed till " + clientState.getEndOfPostponePeriod();
                    log.debug(message);
                    response.sendError(ERROR_CODE_CALL_LIMIT_EXCEEDED, message);
                } else {
                    log.debug("client state is ok, sending response");
                    result = true;
                }
            } else {
                log.error("unknown error, client state is null");
                response.sendError(ERROR_CODE_UNKNOWN_ERROR, "unknown error");
            }
        } catch (InvalidApiKeyException e) {
            log.warn("you provided invalid api key: " + apiKey);
            response.sendError(ERROR_CODE_INVALID_API_KEY, "you provided invalid api key");
        }

        return result;
    }
}
