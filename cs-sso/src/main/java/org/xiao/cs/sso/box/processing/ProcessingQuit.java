package org.xiao.cs.sso.box.processing;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.xiao.cs.sso.box.condition.ProcessingConfigServiceCondition;
import org.xiao.cs.sso.box.enumerate.TokenAgeing;
import org.xiao.cs.sso.box.utils.TokenUtils;

@Service
@Conditional(ProcessingConfigServiceCondition.class)
public class ProcessingQuit implements LogoutHandler {

    @Resource
    TokenUtils tokenUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        tokenUtils.disable(TokenAgeing.HIBERNATION);
        tokenUtils.disable(TokenAgeing.ROUTINE);
    }
}
