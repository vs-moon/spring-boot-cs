package org.xiao.cs.sso.box.domain;

import org.apache.commons.lang3.StringUtils;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.properties.box.SymbolProperties;
import org.xiao.cs.sso.box.properties.SSOProperties;

import java.util.Map;

public class ClaimsOptions {
    private static final SymbolProperties symbolProperties = SpringUtils.getBean(SymbolProperties.class);
    private static final SSOProperties ssoProperties = SpringUtils.getBean(SSOProperties.class);
    private Long id;
    private String org;
    private String role;
    private String permission;
    private String cross;
    private String crossSite;

    public ClaimsOptions() {}

    public ClaimsOptions(Long id, String org, String role, String permission, String cross, String crossSite) {
        this.id = id;
        this.org = org;
        this.role = role;
        this.permission = permission;
        this.cross = cross;
        this.crossSite = crossSite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String[] toOrgArray() {
        return org.split(symbolProperties.getSeparate());
    }

    public String[] toRoleArray() {
        return role.split(symbolProperties.getSeparate());
    }

    public String[] toPermissionArray() {
        return permission.split(symbolProperties.getSeparate());
    }

    public String getCross() {
        return cross;
    }

    public void setCross(String cross) {
        this.cross = cross;
    }

    public String getCrossSite() {
        return crossSite;
    }

    public void setCrossSite(String crossSite) {
        this.crossSite = crossSite;
    }

    public static ClaimsOptions build(Long id, AuthoritySymbol<String> authoritySymbol) {
        Map<String, String[]> crossMap = ssoProperties.getToken().getCross();
        Map<String, String[]> crossSiteMap = ssoProperties.getToken().getCrossSite();

        String[] crossItemArray = crossMap.get(SpringUtils.getApplicationName());
        String[] crossSiteItemArray = crossSiteMap.get(SpringUtils.getApplicationName());

        String crossItemString = StringUtils.isNoneBlank(crossItemArray) ?
                StringUtils.join(crossItemArray, SymbolProperties.SEPARATE) :
                StringUtils.EMPTY;
        String crossSiteItemString = StringUtils.isNoneBlank(crossSiteItemArray) ?
                StringUtils.join(crossSiteItemArray, SymbolProperties.SEPARATE) :
                StringUtils.EMPTY;

        return new ClaimsOptions(id,
                authoritySymbol.orgJoin(),
                authoritySymbol.roleJoin(),
                authoritySymbol.permissionJoin(),
                crossItemString,
                crossSiteItemString);
    }
}
