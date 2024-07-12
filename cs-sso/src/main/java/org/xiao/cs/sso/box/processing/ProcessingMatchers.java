package org.xiao.cs.sso.box.processing;

import jakarta.annotation.Resource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;
import org.xiao.cs.sso.box.properties.SSOProperties;

@Component
public class ProcessingMatchers implements Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {

    @Resource
    SSOProperties ssoProperties;

    @Override
    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry matcherRegistry) {

        SSOProperties.MatchersProperties matchersProperties = ssoProperties.getMatchers();

        matchersProperties.getAnyMethods().forEach(anyMethod -> matcherRegistry.requestMatchers(anyMethod).permitAll());

        // permitAll
        int anyRawSize = matchersProperties.getAnyRaw().size();
        matcherRegistry.requestMatchers(matchersProperties.getAnyRaw().toArray(new String [anyRawSize])).permitAll();
        matchersProperties.getAny().forEach(any -> {
            int anySize = any.getPatterns().size();
            matcherRegistry
                    .requestMatchers(any.getMethod(), any.getPatterns().toArray(new String[anySize])).permitAll();
        });

        // anonymous
        int anonymousRawRawSize = matchersProperties.getAnonymousRaw().size();
        matcherRegistry.requestMatchers(matchersProperties.getAnonymousRaw().toArray(new String[anonymousRawRawSize])).anonymous();
        matchersProperties.getAnonymous().forEach(anonymous -> {
            int anonymousSize = anonymous.getPatterns().size();
            matcherRegistry
                    .requestMatchers(anonymous.getMethod(), anonymous.getPatterns().toArray(new String[anonymousSize])).anonymous();
        });

        // denyAll
        int denyRawRawSize = matchersProperties.getDenyRaw().size();
        matcherRegistry.requestMatchers(matchersProperties.getDenyRaw().toArray(new String [denyRawRawSize])).denyAll();
        matchersProperties.getDeny().forEach(deny -> {
            int denySize = deny.getPatterns().size();
            matcherRegistry
                    .requestMatchers(deny.getMethod(), deny.getPatterns().toArray(new String [denySize])).denyAll();
        });

        matcherRegistry.anyRequest().authenticated();
    }
}
