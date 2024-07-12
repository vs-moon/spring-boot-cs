package org.xiao.cs.sentinel.box.volley;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.utils.ResponseUtils;

@Component
public class BlockExceptionHandlerVolley implements BlockExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(BlockExceptionHandlerVolley.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        ResponseUtils.toJson(httpServletResponse, handleResponse(e));
    }

    public static CommonResponse<Object> handleResponse(Throwable throwable) {
        CommonResponseStatus status = handleException(throwable);
        log.error(throwable.getMessage(), throwable);
        log.error(status.getMessage());
        return CommonResponse.Builder.failure(status);
    }

    public static CommonResponseStatus handleException(Throwable e) {
        if (e instanceof FlowException) {
            return CommonResponseStatus.SENTINEL_FLOW;
        } else if(e instanceof DegradeException) {
            return CommonResponseStatus.SENTINEL_DEGRADE;
        } else if(e instanceof ParamFlowException) {
            return CommonResponseStatus.SENTINEL_PARAM_FLOW;
        } else if(e instanceof AuthorityException) {
            return CommonResponseStatus.SENTINEL_AUTHORITY;
        } else if(e instanceof SystemBlockException) {
            return CommonResponseStatus.SENTINEL_SYSTEM;
        } else {
            return CommonResponseStatus.FAILURE;
        }
    }
}
