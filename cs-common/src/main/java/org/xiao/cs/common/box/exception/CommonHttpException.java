package org.xiao.cs.common.box.exception;

import org.springframework.http.HttpStatus;

public class CommonHttpException extends RuntimeException {

    private HttpStatus httpStatus;

    public CommonHttpException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public CommonHttpException(HttpStatus httpStatus) {
        this(httpStatus.getReasonPhrase(), httpStatus);
    }

    public CommonHttpException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CommonHttpException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public CommonHttpException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    protected CommonHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public CommonHttpException setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }
}
