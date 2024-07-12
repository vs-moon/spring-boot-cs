package org.xiao.cs.sso.box.domain;

import org.xiao.cs.sso.box.enumerate.AccountSignMethod;

public class AccountSign {
    private String username;
    private String password;
    private AccountSignMethod method;

    public AccountSign() {}

    public AccountSign(String username, String password, AccountSignMethod method) {
        this.username = username;
        this.password = password;
        this.method = method;
    }

    public String getUsername() {
        return username;
    }

    public AccountSign setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountSign setPassword(String password) {
        this.password = password;
        return this;
    }

    public AccountSignMethod getMethod() {
        return method;
    }

    public AccountSign setMethod(AccountSignMethod method) {
        this.method = method;
        return this;
    }
}
