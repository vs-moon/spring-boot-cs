package org.xiao.cs.sso.box.processing;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.xiao.cs.sso.box.filter.ProcessingParamFilter;
import org.xiao.cs.sso.box.condition.ProcessingConfigServiceCondition;

@Component
@Conditional(ProcessingConfigServiceCondition.class)
public class ProcessingConfigWith extends AbstractHttpConfigurer<ProcessingConfigWith, HttpSecurity> {

    @Resource
    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Resource
    AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        builder.addFilterAfter(this.authenticationProcessingParamFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        super.configure(builder);
    }

    private ProcessingParamFilter authenticationProcessingParamFilter(AuthenticationManager authenticationManager) {
        ProcessingParamFilter processingParamFilter =
                new ProcessingParamFilter(authenticationManager);

        processingParamFilter.setAuthenticationSuccessHandler(this.authenticationSuccessHandler);
        processingParamFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler);
        return processingParamFilter;
    }
}
