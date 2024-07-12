package org.xiao.cs.sso.box.processing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.xiao.cs.common.box.utils.ResponseUtils;

import java.io.IOException;

@Component
public class ProcessingPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseUtils.toJson(response, CommonResponse.BuilderStatus.failure(HttpStatus.UNAUTHORIZED));
    }
}
