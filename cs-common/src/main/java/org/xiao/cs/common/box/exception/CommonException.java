package org.xiao.cs.common.box.exception;

import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.service.CommonResponseStatusService;

public class CommonException extends RuntimeException {

    private CommonResponseStatusService commonResponseStatusService;

    public CommonException() {
        this(CommonResponseStatus.FAILURE);
    }

    public CommonException(CommonResponseStatusService commonResponseStatusService) {
        super(commonResponseStatusService.getMessage());
        this.commonResponseStatusService = commonResponseStatusService;
    }

    public CommonException(String message, CommonResponseStatusService commonResponseStatusService) {
        super(message);
        this.commonResponseStatusService = commonResponseStatusService;
    }

    public CommonException(String message, Throwable cause, CommonResponseStatusService commonResponseStatusService) {
        super(message, cause);
        this.commonResponseStatusService = commonResponseStatusService;
    }

    public CommonException(Throwable cause, CommonResponseStatusService commonResponseStatusService) {
        super(cause);
        this.commonResponseStatusService = commonResponseStatusService;
    }

    protected CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, CommonResponseStatusService commonResponseStatusService) {
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
