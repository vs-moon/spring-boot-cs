package org.xiao.cs.volley.box.interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.xiao.cs.common.box.exception.BusinessException;
import org.xiao.cs.common.box.exception.CommonException;
import org.xiao.cs.common.box.exception.CommonHttpException;
import org.xiao.cs.volley.box.service.RuntimeExceptionQueue;

import java.util.List;

@RestControllerAdvice
public class ExceptionVolley {

    private static final Logger log = LoggerFactory.getLogger(ExceptionVolley.class);
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    List<RuntimeExceptionQueue> runtimeExceptionQueue;

    @ExceptionHandler({ Throwable.class })
    public CommonResponse<Object> capture (Throwable throwable) {
        return process(request, response, throwable, true);
    }

    private CommonResponse<Object> process(HttpServletRequest request,
                                           HttpServletResponse response,
                                           Throwable throwable,
                                           Boolean isFirst) {

        // TODO 无论任何异常都要提前写入 500 异常状态码 (除非是业务异常 BusinessException), 调用方需要直观的得知是否异常, 不采取无感知方式
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        Throwable cause = throwable.getCause();

        // TODO 嵌套异常
        if (cause != null) {
            log.error(cause.getMessage(), cause);
        }

        // TODO 自定义公共异常
        if (throwable instanceof CommonException commonException) {
            log.error(commonException.getMessage(), commonException);
            return CommonResponse.Builder.failure(commonException.getStatus());
        }

        // TODO 自定义业务异常 (状态码设置为 200, 以免调用方无法根据业务状态码判断业务响应, 需要在业务代码中抛出)
        else if (throwable instanceof BusinessException businessException) {
            response.setStatus(HttpStatus.OK.value());
            log.error(businessException.getMessage(), businessException);
            return CommonResponse.Builder.failure(businessException.getStatus());
        }

        // TODO 自定义公共异常 (Http) (用于其他模块抛出标准语义性的异常, 比如鉴权模块)
        else if (throwable instanceof CommonHttpException commonHttpException) {
            HttpStatus httpStatus = commonHttpException.getStatus();
            response.setStatus(httpStatus.value());
            log.error(httpStatus.getReasonPhrase(), commonHttpException);
            return CommonResponse.BuilderStatus.failure(httpStatus);
        }

        // TODO 运行时异常 (优先遍历实现了 RuntimeExceptionQueue 的实现类, 由实现类内部捕获后做出相应的异常逻辑处理, 比如捕获了第三方异常, 抛出其他异常)
        else if (throwable instanceof RuntimeException runtimeException) {
            if (isFirst) {
                for (RuntimeExceptionQueue queueItem : runtimeExceptionQueue) {
                    try {
                        queueItem.capture(runtimeException);
                    } catch (RuntimeException runtimeExceptionCapture) {
                        return process(request, response, runtimeExceptionCapture, false);
                    }
                }
            }

            log.error(runtimeException.getMessage(), runtimeException);
            return CommonResponse.Builder.failure(runtimeException.getMessage());
        }

        // TODO 服务器异常
        else if (throwable instanceof ServletException servletException) {
            if (servletException instanceof ErrorResponse errorResponse) {
                int status = errorResponse.getStatusCode().value();
                // (如实的写入异常响应状态码)
                response.setStatus(status);
                log.error(errorResponse.getBody().getDetail(), errorResponse);
                return CommonResponse.BuilderStatus.failure(status);
            } else {
                log.error(servletException.getMessage(), servletException);
                return CommonResponse.BuilderStatus.failure(servletException.getMessage());
            }
        }

        // TODO 其余异常
        else {
            log.error(throwable.getMessage(), throwable);
            return CommonResponse.Builder.failure(throwable.getMessage());
        }
    }
}
