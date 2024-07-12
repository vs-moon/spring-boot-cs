package org.xiao.cs.common.box.enumerate;

public enum AuthorizationMode {

    BEARER("Bearer");

    private final String code;

    AuthorizationMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
