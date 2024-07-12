package org.xiao.cs.sita.box.service;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xiao.cs.common.box.exception.CommonException;
import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.volley.box.service.RuntimeExceptionQueue;

@Service("csSitaExceptionCapture")
public class RuntimeExceptionQueueSita implements RuntimeExceptionQueue {

    private static final Logger log = LoggerFactory.getLogger(RuntimeExceptionQueueSita.class);

    @Override
    public void capture(RuntimeException runtimeException) throws RuntimeException {
        try {
            if (RootContext.inGlobalTransaction()) {
                log.error("======= 分布式服务执行异常: 进行全局事务回滚");
                log.error("======= XID: {} | 模式: {} | 服务名称: {} | 端口号: {}", RootContext.getXID(), RootContext.getBranchType(), SpringUtils.getProperty("spring.application.name"), SpringUtils.getProperty("server.port"));
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            }
        } catch (TransactionException transactionException) {
            throw new CommonException(CommonResponseStatus.DCS_ROLLBACK_ERROR);
        }
    }
}
