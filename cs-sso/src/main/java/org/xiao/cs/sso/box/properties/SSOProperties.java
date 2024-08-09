package org.xiao.cs.sso.box.properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.xiao.cs.common.box.constant.AgeingConstant;
import org.xiao.cs.common.box.enumerate.CalendarMapping;
import org.xiao.cs.sso.box.enumerate.VisitorMode;
import org.xiao.cs.sso.box.utils.RsaUtils;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@ConfigurationProperties(prefix = SSOProperties.PROPERTIES_PREFIX)
public class SSOProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SSOProperties.class);

    public static final String PROPERTIES_PREFIX =  "org.xiao.cs.sso";

    private boolean enabled = true;
    private boolean issuanceCenter = false;
    private String issuer;
    private boolean commonResource;
    private ConfineProperties confine;
    private Map<String, List<VisitCompetenceProperties>> visitCompetence = new HashMap<>();
    private MatchersProperties matchers;
    private RsaProperties rsa;
    private TokenProperties token;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIssuanceCenter() {
        return issuanceCenter;
    }

    public void setIssuanceCenter(boolean issuanceCenter) {
        this.issuanceCenter = issuanceCenter;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public boolean isCommonResource() {
        return commonResource;
    }

    public void setCommonResource(boolean commonResource) {
        this.commonResource = commonResource;
    }

    public ConfineProperties getConfine() {
        return confine;
    }

    public void setConfine(ConfineProperties confine) {
        this.confine = confine;
    }

    public Map<String, List<VisitCompetenceProperties>> getVisitCompetence() {
        return visitCompetence;
    }

    public void setVisitCompetence(Map<String, List<VisitCompetenceProperties>> visitCompetence) {
        this.visitCompetence = visitCompetence;
    }

    public MatchersProperties getMatchers() {
        return matchers;
    }

    public void setMatchers(MatchersProperties matchers) {
        this.matchers = matchers;
    }

    public RsaProperties getRsa() {
        return rsa;
    }

    public void setRsa(RsaProperties rsa) {
        this.rsa = rsa;
    }

    public TokenProperties getToken() {
        return token;
    }

    public void setToken(TokenProperties token) {
        this.token = token;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("InitializingBean: SSOProperties -> " + PROPERTIES_PREFIX);

        if (enabled) {
            if (issuanceCenter) {
                if (StringUtils.isNotBlank(issuer)) {
                    throw new Exception("properties: " + PROPERTIES_PREFIX + ".issuer ban setup");
                }
            } else {
                if (!commonResource) {
                    if (StringUtils.isBlank(issuer)) {
                        throw new Exception("properties: " + PROPERTIES_PREFIX + ".issuer not found");
                    }
                }
            }

            rsa.after();
            token.after();
        }
    }

    public static class ConfineProperties {
        private String signerEntrance = "/login";
        private String cancelEntrance = "/logout";

        public String getSignerEntrance() {
            return signerEntrance;
        }

        public void setSignerEntrance(String signerEntrance) {
            this.signerEntrance = signerEntrance;
        }

        public String getCancelEntrance() {
            return cancelEntrance;
        }

        public void setCancelEntrance(String cancelEntrance) {
            this.cancelEntrance = cancelEntrance;
        }
    }

    public static class VisitCompetenceProperties {
        private String visitor;
        private VisitorMode mode = VisitorMode.R;

        public String getVisitor() {
            return visitor;
        }

        public void setVisitor(String visitor) {
            this.visitor = visitor;
        }

        public VisitorMode getMode() {
            return mode;
        }

        public void setMode(VisitorMode mode) {
            this.mode = mode;
        }
    }

    public static class MatchersProperties {
        private List<HttpMethod> anyMethods = new ArrayList<>();
        private List<MatchersProperties.Permit> any = new ArrayList<>();
        private List<MatchersProperties.Permit> deny = new ArrayList<>();
        private List<MatchersProperties.Permit> anonymous = new ArrayList<>();
        private List<String> anyRaw = new ArrayList<>();
        private List<String> denyRaw = new ArrayList<>();
        private List<String> anonymousRaw = new ArrayList<>();

        public List<HttpMethod> getAnyMethods() {
            return anyMethods;
        }

        public void setAnyMethods(List<HttpMethod> anyMethods) {
            this.anyMethods = anyMethods;
        }

        public List<Permit> getAny() {
            return any;
        }

        public void setAny(List<Permit> any) {
            this.any = any;
        }

        public List<Permit> getDeny() {
            return deny;
        }

        public void setDeny(List<Permit> deny) {
            this.deny = deny;
        }

        public List<Permit> getAnonymous() {
            return anonymous;
        }

        public void setAnonymous(List<Permit> anonymous) {
            this.anonymous = anonymous;
        }

        public List<String> getAnyRaw() {
            return anyRaw;
        }

        public void setAnyRaw(List<String> anyRaw) {
            this.anyRaw = anyRaw;
        }

        public List<String> getDenyRaw() {
            return denyRaw;
        }

        public void setDenyRaw(List<String> denyRaw) {
            this.denyRaw = denyRaw;
        }

        public List<String> getAnonymousRaw() {
            return anonymousRaw;
        }

        public void setAnonymousRaw(List<String> anonymousRaw) {
            this.anonymousRaw = anonymousRaw;
        }

        public static class Permit {
            private HttpMethod method;
            private List<String> patterns;

            public HttpMethod getMethod() {
                return method;
            }

            public void setMethod(HttpMethod method) {
                this.method = method;
            }

            public List<String> getPatterns() {
                return patterns;
            }

            public void setPatterns(List<String> patterns) {
                this.patterns = patterns;
            }
        }
    }

    public static class RsaProperties {

        private PublicKey publicKey;
        private PrivateKey privateKey;

        private Boolean keyPathFirst = false;

        private String publicKeyPath;
        private String privateKeyPath;

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) throws Exception {
            if (StringUtils.isNoneBlank(publicKey)) {
                this.publicKey = RsaUtils.getPublicKey(publicKey.getBytes(StandardCharsets.UTF_8));
            }
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) throws Exception {
            if (StringUtils.isNoneBlank(privateKey)) {
                this.privateKey = RsaUtils.getPrivateKey(privateKey.getBytes(StandardCharsets.UTF_8));
            }
        }

        public String getPublicKeyPath() {
            return publicKeyPath;
        }

        public void setPublicKeyPath(String publicKeyPath) {
            this.publicKeyPath = publicKeyPath;
        }

        public String getPrivateKeyPath() {
            return privateKeyPath;
        }

        public void setPrivateKeyPath(String privateKeyPath) {
            this.privateKeyPath = privateKeyPath;
        }

        public Boolean getKeyPathFirst() {
            return keyPathFirst;
        }

        public void setKeyPathFirst(Boolean keyPathFirst) {
            this.keyPathFirst = keyPathFirst;
        }

        public void after() throws Exception {
            if (keyPathFirst) {
                if (StringUtils.isNoneBlank(publicKeyPath)) {
                    publicKey = RsaUtils.getPublicKey(publicKeyPath);
                }

                if (StringUtils.isNoneBlank(privateKeyPath)) {
                    privateKey = RsaUtils.getPrivateKey(privateKeyPath);
                }
            }
        }
    }

    public static class TokenProperties {
        private Map<String, String[]> cross = new HashMap<>();
        private Map<String, String[]> crossSite = new HashMap<>();
        private AgeingProperties ageing;

        public Map<String, String[]> getCross() {
            return cross;
        }

        public void setCross(Map<String, String[]> cross) {
            this.cross = cross;
        }

        public Map<String, String[]> getCrossSite() {
            return crossSite;
        }

        public void setCrossSite(Map<String, String[]> crossSite) {
            this.crossSite = crossSite;
        }

        public AgeingProperties getAgeing() {
            return ageing;
        }
        public void setAgeing(AgeingProperties ageing) {
            this.ageing = ageing;
        }
        public void after() {
            ageing.after();
        }

        public static class AgeingProperties {
            private ExpiresProperties hibernation;
            private ExpiresProperties routine;
            private ExpiresProperties proxy;

            public ExpiresProperties getHibernation() {
                return hibernation;
            }

            public void setHibernation(ExpiresProperties hibernation) {
                this.hibernation = hibernation;
            }

            public ExpiresProperties getRoutine() {
                return routine;
            }

            public void setRoutine(ExpiresProperties routine) {
                this.routine = routine;
            }

            public ExpiresProperties getProxy() {
                return proxy;
            }

            public void setProxy(ExpiresProperties proxy) {
                this.proxy = proxy;
            }

            public void after() {
                hibernation.after(1, CalendarMapping.YEAR);
                routine.after(7, CalendarMapping.DAY);
                proxy.after(30, CalendarMapping.SECOND);
            }

            public static class ExpiresProperties {
                private Integer expires;
                private CalendarMapping expiresUnit;
                private Long expiresResult;

                public Integer getExpires() {
                    return expires;
                }

                public void setExpires(Integer expires) {
                    this.expires = expires;
                }

                public CalendarMapping getExpiresUnit() {
                    return expiresUnit;
                }

                public void setExpiresUnit(CalendarMapping expiresUnit) {
                    this.expiresUnit = expiresUnit;
                }

                public Long getExpiresResult() {
                    return expiresResult;
                }

                public long convert () {
                    return switch (expiresUnit) {
                        case YEAR -> DateUtils.addYears(new Date(), expires).getTime();
                        case MONTH -> DateUtils.addMonths(new Date(), expires).getTime() - new Date().getTime();
                        case WEEK -> (long) expires * AgeingConstant.UNIT_WEEK;
                        case DAY -> (long) expires * AgeingConstant.UNIT_DAY;
                        case HOUR -> (long) expires * AgeingConstant.UNIT_HOUR;
                        case MIN -> (long) expires * AgeingConstant.UNIT_MIN;
                        case SECOND -> (long) expires * AgeingConstant.UNIT_BASE;
                    };
                }

                public void after(int defaultExpires, CalendarMapping defaultExpiresUnit) {
                    if (expires == null || expiresUnit == null) {
                        if (expires == null && expiresUnit == null) {
                            expires = defaultExpires;
                            expiresUnit = CalendarMapping.DAY;
                        } else {
                            if (expires == null) {
                                expires = defaultExpires;
                            }

                            if (expiresUnit == null) {
                                expiresUnit = defaultExpiresUnit;
                            }
                        }

                    }

                    expiresResult = convert();
                }
            }
        }
    }
}
