package org.xiao.cs.sso.box.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
// 用于捕获 Filter 层异常, 抛出到 Controller 层
public class ProcessingExceptionFilter extends OncePerRequestFilter {
    @Resource
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            if (e instanceof RuntimeException runtimeException) {
                handlerExceptionResolver.resolveException(request, response, null, runtimeException);
            } else {
                handlerExceptionResolver.resolveException(request, response, null, new RuntimeException(e.getMessage(), e));
            }
        }
    }
}
