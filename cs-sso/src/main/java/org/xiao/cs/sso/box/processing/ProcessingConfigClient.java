package org.xiao.cs.sso.box.processing;

import org.springframework.context.annotation.Conditional;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;
import org.xiao.cs.sso.box.service.ProcessingConfigInvoker;
import org.xiao.cs.sso.box.condition.ProcessingConfigClientCondition;

@Component
@Conditional(ProcessingConfigClientCondition.class)
public class ProcessingConfigClient implements ProcessingConfigInvoker {
    public void doFilter(HttpSecurity http) throws Exception {
        // 登出配置
        http.logout(AbstractHttpConfigurer::disable);
    }
}
