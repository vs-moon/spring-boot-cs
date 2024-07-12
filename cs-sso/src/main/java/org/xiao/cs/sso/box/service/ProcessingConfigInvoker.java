package org.xiao.cs.sso.box.service;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface ProcessingConfigInvoker {
    void doFilter(HttpSecurity http) throws Exception;
}
