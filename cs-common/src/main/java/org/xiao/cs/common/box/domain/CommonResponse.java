package org.xiao.cs.common.box.domain;

import org.springframework.http.HttpStatus;
import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.service.CommonResponseStatusService;
import org.xiao.cs.common.box.utils.JsonUtils;

public class CommonResponse<T> {

    private Integer code;
    private String message;
    private Boolean success;
    private T data;

    public CommonResponse() {}

    public CommonResponse(Integer code, String message, Boolean success, T data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public CommonResponse<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public CommonResponse<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public CommonResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static class Builder {
        public static CommonResponse<Object> success() {
            return success(CommonResponseStatus.SUCCESS);
        }

        public static CommonResponse<Object> success(CommonResponseStatusService commonResponseStatusService) {
            return success(commonResponseStatusService, null);
        }

        public static CommonResponse<Object> success(Object data) {
            return success(CommonResponseStatus.SUCCESS, data);
        }

        public static CommonResponse<Object> success(CommonResponseStatusService commonResponseStatusService, Object data) {
            return builder(commonResponseStatusService, data,true);
        }

        public static CommonResponse<Object> failure(String message) {
            return failure(CommonResponseStatus.FAILURE).setMessage(message);
        }

        public static CommonResponse<Object> failure() {
            return failure(CommonResponseStatus.FAILURE);
        }

        public static CommonResponse<Object> failure(CommonResponseStatusService commonResponseStatusService) {
            return failure(commonResponseStatusService, null);
        }

        public static CommonResponse<Object> failure(Object data) {
            return failure(CommonResponseStatus.FAILURE, data);
        }

        public static CommonResponse<Object> failure(CommonResponseStatusService commonResponseStatusService, Object data) {
            return builder(commonResponseStatusService, data,false);
        }

        public static CommonResponse<Object> builder(CommonResponseStatusService commonResponseStatusService, Object data, Boolean success) {
            return new CommonResponse<>(commonResponseStatusService.getCode(),
                    commonResponseStatusService.getMessage(),
                    success,
                    data);
        }
    }

    public static class BuilderStatus {
        public static CommonResponse<Object> success() {
            return success(HttpStatus.OK);
        }

        public static CommonResponse<Object> success(int httpStatus) {
            return success(HttpStatus.resolve(httpStatus));
        }

        public static CommonResponse<Object> success(HttpStatus httpStatus) {
            return success(httpStatus, null);
        }

        public static CommonResponse<Object> success(Object data) {
            return success(HttpStatus.OK, data);
        }

        public static CommonResponse<Object> success(HttpStatus httpStatus, Object data) {
            return builder(httpStatus, true, data);
        }

        public static CommonResponse<Object> failure(String message) {
            return failure(HttpStatus.INTERNAL_SERVER_ERROR).setMessage(message);
        }

        public static CommonResponse<Object> failure() {
            return failure(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        public static CommonResponse<Object> failure(int status) {
            return failure(HttpStatus.resolve(status));
        }

        public static CommonResponse<Object> failure(HttpStatus httpStatus) {
            return failure(httpStatus, null);
        }

        public static CommonResponse<Object> failure(Object data) {
            return failure(HttpStatus.INTERNAL_SERVER_ERROR, data);
        }

        public static CommonResponse<Object> failure(HttpStatus httpStatus, Object data) {
            return builder(httpStatus, false, data);
        }

        public static CommonResponse<Object> builder(HttpStatus httpStatus, Boolean success, Object data) {
            return new CommonResponse<>(httpStatus.value(),
                    httpStatus.getReasonPhrase(),
                    success,
                    data);
        }
    }

    @Override
    public String toString() {
        return JsonUtils.toString(this);
    }
}
