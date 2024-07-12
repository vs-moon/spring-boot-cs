package org.xiao.cs.common.box.enumerate;

import org.springframework.http.HttpStatus;
import org.xiao.cs.common.box.service.CommonResponseStatusService;

public enum CommonResponseStatus implements CommonResponseStatusService {

    // TODO 基本 200 or 500
    SUCCESS(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()),
    FAILURE(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),

    // TODO 授权 10001 ~ 10019
    AUTHORIZED_BAN_ISSUE(10000, "禁止颁发"),
    AUTHORIZED_BAN_CROSS(10001, "禁止跨域"),
    AUTHORIZED_INVALID(10002, "认证无效"),


    // TODO 令牌 10020 ~ 10039
    TOKEN_EXPIRED(10011, "令牌过期"),
    TOKEN_INVALID(10012, "令牌无效"),


    // TODO 账户 10040 ~ 10059
    ACCOUNT_ABSENT(10121, "账号不存在"),
    ACCOUNT_BUSY(10122, "账号占线"),
    ACCOUNT_OFFLINE(10123, "账号下线"),
    ACCOUNT_INVALID(10124, "账号无效"),
    ACCOUNT_BAN(10125, "账号封禁"),
    ACCOUNT_FREEZE(10126, "账号冻结"),
    ACCOUNT_LOCK(10127, "账号锁定"),
    ACCOUNT_EXPIRED(10128, "账号过期"),
    ACCOUNT_LOGOFF(10129, "账号注销"),
    ACCOUNT_PASSWORD_ERROR(10130, "密码错误"),
    ACCOUNT_PASSWORD_EXPIRED(10131, "密码过期"),
    ACCOUNT_CREDENTIALS_EXPIRED(10132, "凭证过期"),


    // TODO 分布式 10060 ~ 10079
    DCS_ROLLBACK_ERROR(10201, "分布式事务回滚异常"),

    // TODO 哨兵 10080 ~ 10099
    SENTINEL_FLOW(10211, "Sentinel 流控"),
    SENTINEL_DEGRADE(10212, "Sentinel 降级"),
    SENTINEL_PARAM_FLOW(10213, "Sentinel 参数流控"),
    SENTINEL_AUTHORITY(10214, "Sentinel 权限"),
    SENTINEL_SYSTEM(10215, "Sentinel 系统"),
    SENTINEL_FUSING(10220, "Sentinel 熔断");

    private final Integer code;
    private final String message;

    CommonResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
