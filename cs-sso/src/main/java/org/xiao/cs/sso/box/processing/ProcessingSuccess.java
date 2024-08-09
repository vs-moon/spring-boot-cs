package org.xiao.cs.sso.box.processing;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.xiao.cs.common.box.utils.ResponseUtils;
import org.xiao.cs.common.box.utils.ScopeCacheUtils;
import org.xiao.cs.properties.box.HeadersAttributeProperties;
import org.xiao.cs.sso.box.condition.ProcessingConfigServiceCondition;
import org.xiao.cs.sso.box.constant.ProcessingConstant;
import org.xiao.cs.sso.box.domain.AccountBody;
import org.xiao.cs.sso.box.domain.ClaimsOptions;
import org.xiao.cs.sso.box.domain.TokenResponseBody;
import org.xiao.cs.sso.box.utils.TokenUtils;

import java.io.IOException;

@Service
@Conditional(ProcessingConfigServiceCondition.class)
public class ProcessingSuccess implements AuthenticationSuccessHandler {

    @Resource
    TokenUtils tokenUtils;
    @Resource
    HeadersAttributeProperties headersAttributeProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CommonResponse<Object> success = CommonResponse.Builder.success();
        TokenResponseBody tokenResponseBody = new TokenResponseBody();

        AccountBody accountBody = (AccountBody) ScopeCacheUtils.get(ProcessingConstant.SCOPE_CACHE_CURRENT_ACCOUNT_BODY_KEY);
        ScopeCacheUtils.remove(ProcessingConstant.SCOPE_CACHE_CURRENT_ACCOUNT_BODY_KEY);

        // 载体
        ClaimsOptions claimsOptions = ClaimsOptions.build(accountBody.getBasic().getId(), accountBody.getAuthoritySymbol());
        String routine = tokenUtils.issueRoutine(accountBody.getUsername(), claimsOptions);
        String montageRoutine = tokenUtils.montageToken(routine);
        // 令牌
        response.setHeader(HttpHeaders.AUTHORIZATION, montageRoutine);
        tokenResponseBody.setRoutine(montageRoutine);
        // 允许跨域的服务
        response.setHeader(headersAttributeProperties.getAuthorizationCross(), claimsOptions.getCross());
        // 允许被跨域的站点
        response.setHeader(headersAttributeProperties.getAuthorizationCrossSite(), claimsOptions.getCrossSite());

        boolean isRemember = Boolean.parseBoolean(request.getHeader(headersAttributeProperties.getAuthorizationRemember()));

        if (isRemember) {
            // 持久化令牌
            String hibernation = tokenUtils.issueHibernation(accountBody.getUsername(), ClaimsOptions.build(accountBody.getBasic().getId(), accountBody.getAuthoritySymbol()));
            String montageHibernation = tokenUtils.montageToken(hibernation);
            response.setHeader(headersAttributeProperties.getAuthorizationHibernation(), montageHibernation);
            tokenResponseBody.setHibernation(montageHibernation);
            tokenResponseBody.setRemember(true);
        }

        ResponseUtils.toJson(response, success.setData(tokenResponseBody));
    }
}
