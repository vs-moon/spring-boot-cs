package org.xiao.cs.sso.box.domain;

public class AccountRegister {
    private String username;
    private String password;
    private String method;

    public AccountRegister() {}

    public AccountRegister(String username, String password, String method) {
        this.username = username;
        this.password = password;
        this.method = method;
    }

    public String getUsername() {
        return username;
    }

    public AccountRegister setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountRegister setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public AccountRegister setMethod(String method) {
        this.method = method;
        return this;
    }
}
