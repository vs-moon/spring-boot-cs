package org.xiao.cs.sso.box.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.exception.CommonException;
import org.xiao.cs.common.box.exception.CommonHttpException;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.properties.box.HeadersAttributeProperties;
import org.xiao.cs.redis.box.utils.RedisUtils;
import org.xiao.cs.sso.box.domain.ClaimsOptions;
import org.xiao.cs.sso.box.enumerate.TokenAgeing;
import org.xiao.cs.sso.box.properties.SSOProperties;
import org.xiao.cs.sso.box.utils.TokenUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Component
public class ProcessingTokenFilter extends OncePerRequestFilter {

    @Resource
    RedisUtils redisUtils;
    @Resource
    TokenUtils tokenUtils;
    @Resource
    SSOProperties ssoProperties;
    @Resource
    HeadersAttributeProperties headersAttributeProperties;
    @Resource
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = tokenUtils.read();
            try {
                Jws<Claims> jws = tokenUtils.parser(token);
                String namespace = tokenUtils.getNamespace(TokenAgeing.ROUTINE, jws.getSignature());

                if (blacklistCheck(request, response, namespace)) {
                    authenticated(request, response, filterChain, jws);
                }
            } catch (CommonException e) {
                capture(request, response, filterChain, e);
            }
        } catch (CommonException e) {
            filterChain.doFilter(request, response);
        }
    }

    // 黑名单校验
    private boolean blacklistCheck(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull String namespace) {

        if (redisUtils.hasKey(namespace)) {
            handlerExceptionResolver.resolveException(request, response, null, new CommonException(CommonResponseStatus.ACCOUNT_LOGOFF));
            return false;
        }

        return true;
    }

    // 捕获
    private void capture(@NonNull HttpServletRequest request,
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain,
                                   @NonNull CommonException e) throws ServletException, IOException {

        // Token 过期时只放行颁发中心服务的登录接口
        if (e.getStatus().equals(CommonResponseStatus.TOKEN_EXPIRED)) {
            if (ssoProperties.isIssuanceCenter()) {
                if (request.getServletPath().equals(ssoProperties.getConfine().getSignerEntrance())) {
                    filterChain.doFilter(request, response);
                } else {
                    hibernationCheck(request, response, filterChain, e);
                }
            } else {
                hibernationCheck(request, response, filterChain, e);
            }
        } else if (e.getStatus().equals(CommonResponseStatus.TOKEN_INVALID)) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    // 持久校验
    private void hibernationCheck(@NonNull HttpServletRequest request,
                               @NonNull HttpServletResponse response,
                               @NonNull FilterChain filterChain,
                               @NonNull CommonException e) throws ServletException, IOException {

        boolean isRemember = Boolean.parseBoolean(request.getHeader(headersAttributeProperties.getAuthorizationRemember()));

        if (isRemember) {
            String hibernation = tokenUtils.read(TokenAgeing.HIBERNATION);
            Jws<Claims> jws = tokenUtils.parser(hibernation);
            ClaimsOptions claimsOptions = tokenUtils.toBean(jws.getBody());
            String issueRenewal = tokenUtils.issueRenewal(hibernation, claimsOptions);
            response.setHeader(headersAttributeProperties.getAuthorizationRenewal(), tokenUtils.montageToken(issueRenewal));
            authenticated(request, response, filterChain, jws);
        } else {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    // 认证
    private void authenticated(@NonNull HttpServletRequest request,
                               @NonNull HttpServletResponse response,
                               @NonNull FilterChain filterChain,
                               Jws<Claims> jws) throws ServletException, IOException {

        Claims claims = jws.getBody();
        ClaimsOptions claimsOptions = tokenUtils.toBean(claims);
        String username = claims.getSubject();
        String applicationName = SpringUtils.getProperty("spring.application.name");

        final AtomicReference<UsernamePasswordAuthenticationToken> atomicReference =
                new AtomicReference<>(new UsernamePasswordAuthenticationToken(null, null));

        // TODO 1 [目标服务为公共资源] (公共资源可以被任意内部生态平台令牌持有者访问)
        if (ssoProperties.isCommonResource()) {
            atomicReference.set(UsernamePasswordAuthenticationToken.authenticated(username, null, Collections.emptyList()));
        // TODO 2 [目标服务为颁发中心] (不同颁发中心颁发的令牌不允许直接跨平台访问, 除非持有跨域标识或代理令牌 -> 2.3, 2.4)
        } else if (ssoProperties.isIssuanceCenter()) {
            // TODO 2.1 [校验令牌匿名性] (携带令牌者不能二次访问或者需要将当前令牌失效)
            if (request.getServletPath().equals(ssoProperties.getConfine().getSignerEntrance())) {
                throw new CommonHttpException(HttpStatus.FORBIDDEN);
            }

            // TODO 2.2 [校验令牌颁发者] (颁发中心服务的应用名称与令牌的颁发者相同)
            if (applicationName.equals(claims.getIssuer())) {
                atomicReference.set(UsernamePasswordAuthenticationToken.authenticated(username, null, Collections.emptyList()));
            // TODO 2.3 [校验跨域标识] (跨平台直连访问)
            } else if (claimsOptions.getCross().contains(applicationName)) {
                atomicReference.set(UsernamePasswordAuthenticationToken.authenticated(username, null, Collections.emptyList()));
            // TODO 2.4 [校验代理令牌] (跨平台代理访问)
            } else {
                proxyAuthorization(request, response, applicationName, proxyClaims ->
                        atomicReference.set(UsernamePasswordAuthenticationToken.authenticated(proxyClaims.getSubject(), null, Collections.emptyList())));
            }
        // TODO 3 [目标服务的颁发者与令牌的颁发者相同] (不同颁发中心颁发的令牌只能访问其被附属的服务, 除非持有跨域标识或代理令牌 -> 4, 5)
        } else if (ssoProperties.getIssuer().equals(claims.getIssuer())) {
            atomicReference.set(UsernamePasswordAuthenticationToken.authenticated(username, null, Collections.emptyList()));
        // TODO 4 [校验跨域标识] (跨平台直连访问)
        } else if (claimsOptions.getCross().contains(applicationName)) {
            atomicReference.set(UsernamePasswordAuthenticationToken.authenticated(username, null, Collections.emptyList()));
        // TODO 5 [校验代理令牌] (跨平台代理访问)
        } else {
            proxyAuthorization(request, response, applicationName, proxyClaims ->
                    atomicReference.set(UsernamePasswordAuthenticationToken.authenticated(proxyClaims.getSubject(), null, Collections.emptyList())));
        }

        atomicReference.get().setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(atomicReference.get());

        filterChain.doFilter(request, response);
    }

    // 代理认证
    private void proxyAuthorization(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String applicationName,
                                     Consumer<Claims> consumer) {

        String proxy = tokenUtils.read(TokenAgeing.PROXY);

        if (StringUtils.isNotBlank(proxy)) {
            Claims proxyClaims = tokenUtils.parser(proxy).getBody();
            if (proxyClaims.getAudience().equals(applicationName)) {
                consumer.accept(proxyClaims);
            } else {
                handlerExceptionResolver.resolveException(request, response, null, new CommonException(CommonResponseStatus.AUTHORIZED_BAN_CROSS));
            }
        } else {
            handlerExceptionResolver.resolveException(request, response, null, new CommonException(CommonResponseStatus.TOKEN_INVALID));
        }
    }
}
