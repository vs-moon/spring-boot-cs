package org.xiao.cs.sso.box.processing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.utils.ResponseUtils;
import org.xiao.cs.sso.box.condition.ProcessingConfigServiceCondition;

import java.io.IOException;

@Service
@Conditional(ProcessingConfigServiceCondition.class)
public class ProcessingFailure implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        CommonResponseStatus commonCommonResponseStatusCode;
        if (exception instanceof UsernameNotFoundException) {
            commonCommonResponseStatusCode = CommonResponseStatus.ACCOUNT_ABSENT;
        } else if (exception instanceof BadCredentialsException) {
            commonCommonResponseStatusCode = CommonResponseStatus.ACCOUNT_PASSWORD_ERROR;
        } else if (exception instanceof LockedException) {
            commonCommonResponseStatusCode = CommonResponseStatus.ACCOUNT_LOCK;
        } else if (exception instanceof DisabledException) {
            commonCommonResponseStatusCode = CommonResponseStatus.ACCOUNT_INVALID;
        } else if (exception instanceof AccountExpiredException) {
            commonCommonResponseStatusCode = CommonResponseStatus.ACCOUNT_EXPIRED;
        } else if (exception instanceof CredentialsExpiredException) {
            commonCommonResponseStatusCode = CommonResponseStatus.ACCOUNT_CREDENTIALS_EXPIRED;
        } else {
            commonCommonResponseStatusCode = CommonResponseStatus.FAILURE;
        }

        ResponseUtils.toJson(response, CommonResponse.Builder.failure(commonCommonResponseStatusCode));
    }
}
