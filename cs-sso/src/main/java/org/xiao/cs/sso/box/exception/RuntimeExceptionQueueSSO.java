package org.xiao.cs.sso.box.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.xiao.cs.common.box.exception.CommonHttpException;
import org.xiao.cs.volley.box.service.RuntimeExceptionQueue;

@Service("exceptionCaptureSSO")
// 用于 @PreAuthorize 注解异常捕获
public class RuntimeExceptionQueueSSO implements RuntimeExceptionQueue {

    @Override
    public void capture(RuntimeException runtimeException) throws RuntimeException {
        if (runtimeException instanceof AccessDeniedException) {
            throw new CommonHttpException(HttpStatus.FORBIDDEN);
        }
    }
}
