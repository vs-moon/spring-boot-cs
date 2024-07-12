package org.xiao.cs.sso.box.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.xiao.cs.properties.box.SymbolProperties;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class AuthoritySymbol<T> {
    private T [] org;
    private T [] role;
    private T [] permission;

    public AuthoritySymbol() {}

    public AuthoritySymbol(T[] org, T[] role, T[] permission) {
        this.org = org;
        this.role = role;
        this.permission = permission;
    }

    public T[] getOrg() {
        return org;
    }

    public AuthoritySymbol<T> setOrg(T[] org) {
        this.org = org;
        return this;
    }

    public T[] getRole() {
        return role;
    }

    public AuthoritySymbol<T> setRole(T[] role) {
        this.role = role;
        return this;
    }

    public T[] getPermission() {
        return permission;
    }

    public AuthoritySymbol<T> setPermission(T[] permission) {
        this.permission = permission;
        return this;
    }

    public String orgJoin () {
        return StringUtils.join(org, SymbolProperties.SEPARATE);
    }

    public Collection<GrantedAuthority> orgAuthority () {
        return Arrays.stream((String[]) org).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String roleJoin () {
        return StringUtils.join(role, SymbolProperties.SEPARATE);
    }

    public Collection<GrantedAuthority> roleAuthority () {
        return Arrays.stream((String[]) role).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String permissionJoin () {
        return StringUtils.join(permission, SymbolProperties.SEPARATE);
    }

    public Collection<GrantedAuthority> permissionAuthority () {
        return Arrays.stream((String[]) permission).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
