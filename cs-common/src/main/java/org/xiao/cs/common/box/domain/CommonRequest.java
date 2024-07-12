package org.xiao.cs.common.box.domain;

import org.xiao.cs.common.box.utils.SpringUtils;

public class CommonRequest<T> {
    private String sign;
    private T args;

    public CommonRequest() {}
    public CommonRequest(String sign, T args) {
        this.sign = sign;
        this.args = args;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getArgs() {
        return args;
    }

    public void setArgs(T args) {
        this.args = args;
    }

    public static <T> CommonRequest<T> builder(T args) {
        return new CommonRequest<>(SpringUtils.getProperty("spring.application.name"), args);
    }
}
