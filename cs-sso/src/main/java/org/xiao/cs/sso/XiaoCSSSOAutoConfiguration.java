package org.xiao.cs.sso;

import jakarta.annotation.Resource;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.xiao.cs.common.box.basic.AutoRegistrarBasic;
import org.xiao.cs.redis.XiaoCSRedisAutoConfiguration;
import org.xiao.cs.sso.box.filter.ProcessingExceptionFilter;
import org.xiao.cs.sso.box.filter.ProcessingPermissionFilter;
import org.xiao.cs.sso.box.filter.ProcessingTokenFilter;
import org.xiao.cs.sso.box.processing.ProcessingDenied;
import org.xiao.cs.sso.box.processing.ProcessingMatchers;
import org.xiao.cs.sso.box.processing.ProcessingPoint;
import org.xiao.cs.sso.box.properties.SSOProperties;
import org.xiao.cs.sso.box.service.ProcessingConfigInvoker;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties({ SSOProperties.class })
@Import({ XiaoCSSSOAutoConfiguration.Registrar.class })
@AutoConfigureBefore({ ManagementWebSecurityAutoConfiguration.class })
@AutoConfigureAfter({ XiaoCSRedisAutoConfiguration.class })
public class XiaoCSSSOAutoConfiguration {

    @Resource
    ProcessingExceptionFilter processingExceptionFilter;
    @Resource
    ProcessingPermissionFilter processingPermissionFilter;
    @Resource
    ProcessingConfigInvoker processingConfigInvoker;
    @Resource
    ProcessingTokenFilter processingTokenFilter;
    @Resource
    ProcessingMatchers processingMatchers;
    @Resource
    ProcessingDenied processingDenied;
    @Resource
    ProcessingPoint processingPoint;

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 防火墙: 禁用
        http.csrf(AbstractHttpConfigurer::disable);
        // 表单登录: 禁用
        http.formLogin(AbstractHttpConfigurer::disable);
        // 请求缓存: 禁用
        http.requestCache(AbstractHttpConfigurer::disable);
        // 会话策略: 禁用
        http.sessionManagement(sessionManagement ->
                sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 路径规则
        http.authorizeHttpRequests(processingMatchers);
        // 过滤器: Exception
        http.addFilterBefore(processingExceptionFilter, CsrfFilter.class);
        // 过滤器: Token
        http.addFilterBefore(processingTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 过滤器: AuthorizePermission
        http.addFilterBefore(processingPermissionFilter, UsernamePasswordAuthenticationFilter.class);
        // Security 异常处理: 会覆盖重定向逻辑
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        // 未登录 | 未认证
                        .authenticationEntryPoint(processingPoint)
                        // 未授权
                        .accessDeniedHandler(processingDenied));

        processingConfigInvoker.doFilter(http);

        return http.build();
    }

    public static class Registrar extends AutoRegistrarBasic {
        Registrar() {
            super(Registrar.class);
        }
    }
}
