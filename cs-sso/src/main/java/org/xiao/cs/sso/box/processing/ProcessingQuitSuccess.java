package org.xiao.cs.sso.box.processing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.xiao.cs.common.box.utils.ResponseUtils;
import org.xiao.cs.sso.box.condition.ProcessingConfigServiceCondition;

import java.io.IOException;

@Service
@Conditional(ProcessingConfigServiceCondition.class)
public class ProcessingQuitSuccess implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ResponseUtils.toJson(response, CommonResponse.Builder.success());
    }
}
