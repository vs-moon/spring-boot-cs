package org.xiao.cs.sso.box.processing;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.xiao.cs.sso.box.condition.ProcessingConfigServiceCondition;
import org.xiao.cs.sso.box.properties.SSOProperties;
import org.xiao.cs.sso.box.service.ProcessingConfigInvoker;

@Component
@Conditional(ProcessingConfigServiceCondition.class)
public class ProcessingConfigService implements ProcessingConfigInvoker {

    @Resource
    SSOProperties ssoProperties;
    @Resource
    ProcessingConfigWith processingConfigWith;
    @Resource
    ProcessingProvider processingProvider;
    @Resource
    ProcessingQuit processingQuit;
    @Resource
    ProcessingQuitSuccess processingQuitSuccess;

    public void doFilter(HttpSecurity http) throws Exception {
        // 登出处理
        http.logout(logoutConfig ->
                logoutConfig
                        .logoutUrl(ssoProperties.getConfine().getCancelEntrance())
                        .addLogoutHandler(processingQuit)
                        .logoutSuccessHandler(processingQuitSuccess));
        // 认证处理
        http.authenticationProvider(processingProvider);
        // 追加配置
        http.with(processingConfigWith, configWith -> {});
    }
}
