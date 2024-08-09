package org.xiao.cs.common.box.exception;

import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.service.CommonResponseStatusService;

public class BusinessException extends RuntimeException {

    private CommonResponseStatusService commonResponseStatusService;

    public BusinessException() {
        this(CommonResponseStatus.FAILURE);
    }

    public BusinessException(CommonResponseStatusService commonResponseStatusService) {
        super(commonResponseStatusService.getMessage());
        this.commonResponseStatusService = commonResponseStatusService;
    }

    public BusinessException(String message, CommonResponseStatusService commonResponseStatusService) {
        super(message);
        this.commonResponseStatusService = commonResponseStatusService;
    }

    public BusinessException(String message, Throwable cause, CommonResponseStatusService commonResponseStatusService) {
        super(message, cause);
        this.commonResponseStatusService = commonResponseStatusService;
    }

    public BusinessException(Throwable cause, CommonResponseStatusService commonResponseStatusService) {
        super(cause);
        this.commonResponseStatusService = commonResponseStatusService;
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, CommonResponseStatusService commonResponseStatusService) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.commonResponseStatusService = commonResponseStatusService;
    }

    public CommonResponseStatusService getStatus() {
        return commonResponseStatusService;
    }

    public void setStatus(CommonResponseStatusService commonResponseStatusService) {
        this.commonResponseStatusService = commonResponseStatusService;
    }
}
