package org.xiao.cs.sso.box.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xiao.cs.sso.box.permission.AuthorizePermission;

import java.io.IOException;

@Component
public class ProcessingPermissionFilter extends OncePerRequestFilter {

    @Resource
    AuthorizePermission authorizePermission;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 先获取token
        // 从token中获取roles
        // 获取与角色匹配的菜单url
        // 用菜单url集合与token中的roles进行比较

        // MENU:ns-sso:/menu
        filterChain.doFilter(request, response);
    }
}
