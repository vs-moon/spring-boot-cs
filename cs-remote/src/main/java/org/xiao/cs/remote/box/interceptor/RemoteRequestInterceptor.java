package org.xiao.cs.remote.box.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.xiao.cs.properties.box.HeadersAttributeProperties;
import org.xiao.cs.sso.box.properties.SSOProperties;
import org.xiao.cs.sso.box.utils.TokenUtils;

@Configuration
public class RemoteRequestInterceptor implements RequestInterceptor {

    @Resource
    TokenUtils tokenUtils;
    @Resource
    SSOProperties ssoProperties;
    @Resource
    HeadersAttributeProperties headersAttributeProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        setToken(requestTemplate);
    }

    private void setToken(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String audience = requestTemplate.feignTarget().name();

        if (authentication.isAuthenticated()) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, tokenUtils.montageToken(tokenUtils.read()));

            String montageTokenProxy = tokenUtils.montageToken(tokenUtils.issueProxy(audience));
            requestTemplate.header(headersAttributeProperties.getAuthorizationProxy(), montageTokenProxy);
        } else {
            long expires = ssoProperties.getToken().getAgeing().getProxy().getExpiresResult();

            String token = tokenUtils.issueRoutine(authentication.getName(), expires);
            String montageToken = tokenUtils.montageToken(token);
            requestTemplate.header(HttpHeaders.AUTHORIZATION, montageToken);

            String montageTokenProxy = tokenUtils.montageToken(tokenUtils.issueProxy(audience, token));
            requestTemplate.header(headersAttributeProperties.getAuthorizationProxy(), montageTokenProxy);
        }
    }
}
