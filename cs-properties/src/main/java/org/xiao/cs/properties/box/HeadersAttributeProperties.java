package org.xiao.cs.properties.box;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.xiao.cs.common.box.enumerate.AuthorizationMode;

@Configuration
@ConfigurationProperties(prefix = HeadersAttributeProperties.PROPERTIES_PREFIX)
public class HeadersAttributeProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(HeadersAttributeProperties.class);

    public static final String PROPERTIES_PREFIX =  "org.xiao.cs.properties.headers-attribute";

    private AuthorizationMode authorizationMode = AuthorizationMode.BEARER;
    private String authorizationProxy;
    private String authorizationRemember;
    private String authorizationHibernation;
    private String authorizationRenewal;

    public String getAuthorizationProxy() {
        return authorizationProxy;
    }

    public void setAuthorizationProxy(String authorizationProxy) {
        this.authorizationProxy = authorizationProxy;
    }

    public AuthorizationMode getAuthorizationMode() {
        return authorizationMode;
    }

    public void setAuthorizationMode(AuthorizationMode authorizationMode) {
        this.authorizationMode = authorizationMode;
    }

    public String getAuthorizationRemember() {
        return authorizationRemember;
    }

    public void setAuthorizationRemember(String authorizationRemember) {
        this.authorizationRemember = authorizationRemember;
    }

    public String getAuthorizationHibernation() {
        return authorizationHibernation;
    }

    public void setAuthorizationHibernation(String authorizationHibernation) {
        this.authorizationHibernation = authorizationHibernation;
    }

    public String getAuthorizationRenewal() {
        return authorizationRenewal;
    }

    public void setAuthorizationRenewal(String authorizationRenewal) {
        this.authorizationRenewal = authorizationRenewal;
    }

    @Override
    public void afterPropertiesSet() {
        log.info("InitializingBean: HeadersAttributeProperties -> " + PROPERTIES_PREFIX);
    }
}
