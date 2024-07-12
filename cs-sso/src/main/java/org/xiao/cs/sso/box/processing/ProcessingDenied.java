package org.xiao.cs.sso.box.processing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.xiao.cs.common.box.utils.ResponseUtils;

import java.io.IOException;

@Component
public class ProcessingDenied implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ResponseUtils.toJson(response, CommonResponse.BuilderStatus.failure(HttpStatus.FORBIDDEN));
    }
}
