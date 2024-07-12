package org.xiao.cs.properties.box;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = SymbolProperties.PROPERTIES_PREFIX)
public class SymbolProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SymbolProperties.class);

    public static final String PROPERTIES_PREFIX =  "org.xiao.cs.properties.symbol";
    public static final String SEPARATE =  ",";
    public static final String UNIVERSAL =  "*";
    public static final String RANK =  ":";
    public static final String TOP_PERMISSIONS =  UNIVERSAL + RANK + UNIVERSAL + RANK + UNIVERSAL;

    private String separate = SEPARATE;
    private String universal = UNIVERSAL;
    private String rank = RANK;
    private String topPermissions;

    public String getSeparate() {
        return separate;
    }

    public void setSeparate(String separate) {
        this.separate = separate;
    }

    public String getUniversal() {
        return universal;
    }

    public void setUniversal(String universal) {
        this.universal = universal;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTopPermissions() {
        return topPermissions;
    }

    public void setTopPermissions(String topPermissions) {
        this.topPermissions = topPermissions;
    }

    @Override
    public void afterPropertiesSet() {

        if (StringUtils.isBlank(topPermissions)) {
            if (StringUtils.isNoneBlank(universal, rank)) {
                topPermissions = universal + rank + universal + rank + universal;
            } else {
                topPermissions = TOP_PERMISSIONS;
            }
        }

        log.info("InitializingBean: SymbolProperties -> " + PROPERTIES_PREFIX);
    }
}
