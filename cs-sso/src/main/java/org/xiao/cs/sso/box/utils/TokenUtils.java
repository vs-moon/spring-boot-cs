package org.xiao.cs.sso.box.utils;

import io.jsonwebtoken.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.xiao.cs.common.box.enumerate.AuthorizationMode;
import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.exception.CommonException;
import org.xiao.cs.common.box.utils.JsonUtils;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.properties.box.HeadersAttributeProperties;
import org.xiao.cs.redis.box.utils.RedisUtils;
import org.xiao.cs.sso.box.domain.ClaimsOptions;
import org.xiao.cs.sso.box.enumerate.TokenAgeing;
import org.xiao.cs.sso.box.properties.SSOProperties;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Component
public class TokenUtils {

    @Resource
    SSOProperties ssoProperties;
    @Resource
    HeadersAttributeProperties headersAttributeProperties;
    @Resource
    RedisUtils redisUtils;
    @Resource
    HttpServletRequest httpServletRequest;

    private String generateJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    public String issueHibernation(String username) {
        return issueHibernation(username, ssoProperties.getToken().getAgeing().getHibernation().getExpiresResult());
    }

    public String issueHibernation(String username, Long expires) {
        return issue(username, expires, null);
    }

    public String issueHibernation(String username, ClaimsOptions claimsOptions) {
        return issue(username, ssoProperties.getToken().getAgeing().getHibernation().getExpiresResult(), claimsOptions);
    }

    public String issueRoutine(String username) {
        return issueRoutine(username, ssoProperties.getToken().getAgeing().getRoutine().getExpiresResult());
    }

    public String issueRoutine(String username, ClaimsOptions claimsOptions) {
        return issue(username, ssoProperties.getToken().getAgeing().getRoutine().getExpiresResult(), claimsOptions);
    }

    public String issueRoutine(String username, Long expires) {
        return issue(username, expires, null);
    }

    public String issue(String username, Long expires, ClaimsOptions claimsOptions) {
        String audience = SpringUtils.getApplicationName();


        if (ssoProperties.isIssuanceCenter()) {
            JwtBuilder jwtBuilder = builder(
                    ssoProperties.getRsa().getPrivateKey(),
                    audience,
                    audience,
                    username,
                    expires);

            options(jwtBuilder, claimsOptions);

            return jwtBuilder.compact();
        } else {
            throw new CommonException(CommonResponseStatus.AUTHORIZED_BAN_ISSUE);
        }
    }

    public String issueRenewal() {
        String hibernation = httpServletRequest.getHeader(headersAttributeProperties.getAuthorizationHibernation());
        return issueRenewal(hibernation);
    }

    public String issueRenewal(String hibernation) {
        return issueRenewal(hibernation, ssoProperties.getToken().getAgeing().getRoutine().getExpiresResult());
    }

    public String issueRenewal(String hibernation, Long expires) {
        return issueRenewal(hibernation, expires, null);
    }

    public String issueRenewal(String hibernation, ClaimsOptions claimsOptions) {
        return issueRenewal(hibernation, ssoProperties.getToken().getAgeing().getRoutine().getExpiresResult(), claimsOptions);
    }

    public String issueRenewal(String hibernation, Long expires, ClaimsOptions claimsOptions) {
        Claims claims = parser(hibernation).getBody();
        JwtBuilder jwtBuilder = builder(
                ssoProperties.getRsa().getPrivateKey(),
                claims.getIssuer(),
                claims.getAudience(),
                claims.getSubject(),
                expires);

        options(jwtBuilder, claimsOptions);

        return jwtBuilder.compact();
    }

    public String issueProxy(String audience) {
        return issueProxy(audience, read());
    }

    public String issueProxy(String audience, String token) {
        return issueProxy(audience, token, ssoProperties.getToken().getAgeing().getProxy().getExpiresResult());
    }

    public String issueProxy(String audience, Long expires) {
        return issueProxy(audience, read(), expires);
    }

    public String issueProxy(String audience, String token, Long expires) {
        Claims claims = parser(token).getBody();
        ClaimsOptions claimsOptions = toBean(claims);
        JwtBuilder jwtBuilder = builder(
                ssoProperties.getRsa().getPrivateKey(),
                claims.getIssuer(),
                audience,
                claims.getSubject(),
                expires);

        options(jwtBuilder, claimsOptions);

        return jwtBuilder.compact();
    }

    private void options(JwtBuilder jwtBuilder, ClaimsOptions claimsOptions) {
        if (claimsOptions != null) {
            String claimsJsonString = JsonUtils.toString(claimsOptions);
            Map<String, Object> claimsMap = JsonUtils.toMap(claimsJsonString, String.class, Object.class);
            jwtBuilder.addClaims(claimsMap);
        }
    }

    public JwtBuilder builder(PrivateKey privateKey, String issuer, String audience, String username, Long expires) {
        return Jwts.builder()
                .setId(generateJTI())
                .setIssuer(issuer)
                .setSubject(username)
                .setAudience(audience)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expires))
                .signWith(SignatureAlgorithm.RS256, privateKey);
    }

    public boolean disable() throws CommonException {
        return disable(TokenAgeing.ROUTINE);
    }

    public boolean disable(TokenAgeing tokenAgeing) throws CommonException {
        if (exist(tokenAgeing)) {
            Jws<Claims> claimsJws = parser(tokenAgeing);
            Claims body = claimsJws.getBody();
            String signature = claimsJws.getSignature();
            String namespace = getNamespace(tokenAgeing, signature);
            return redisUtils.set(namespace, true, body.getExpiration().getTime() - new Date().getTime());
        } else {
            return false;
        }
    }

    public boolean exist(TokenAgeing tokenAgeing) {
        try {
            read(tokenAgeing);
            return true;
        } catch (CommonException e) {
            return false;
        }
    }

    public boolean check(TokenAgeing tokenAgeing) {
        try {
            parser(tokenAgeing);
            return true;
        } catch (CommonException e) {
            return false;
        }
    }

    public String read() throws CommonException {
        return read(TokenAgeing.ROUTINE);
    }

    public String read(TokenAgeing tokenAgeing) throws CommonException {
        return read(tokenAgeing, headersAttributeProperties.getAuthorizationMode());
    }

    public String read(AuthorizationMode authorizationMode) throws CommonException {
        return read(TokenAgeing.ROUTINE, authorizationMode);
    }

    public String read(TokenAgeing tokenAgeing, AuthorizationMode authorizationMode) throws CommonException {
        String authorizationHeader;
        String token;

        if (TokenAgeing.ROUTINE.equals(tokenAgeing)) {
            authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        } else if (TokenAgeing.PROXY.equals(tokenAgeing)) {
            authorizationHeader = httpServletRequest.getHeader(headersAttributeProperties.getAuthorizationProxy());
        } else {
            authorizationHeader = httpServletRequest.getHeader(headersAttributeProperties.getAuthorizationHibernation());
        }

        if (StringUtils.isBlank(authorizationHeader)) {
            throw new CommonException(CommonResponseStatus.AUTHORIZED_INVALID);
        }

        token = authorizationHeader.replace(authorizationMode.getCode(), "");

        if (StringUtils.isBlank(token)) {
            throw new CommonException(CommonResponseStatus.TOKEN_INVALID);
        }

        return token;
    }

    public String montageToken(String token) {
        return montageToken(token, headersAttributeProperties.getAuthorizationMode());
    }

    public String montageToken(String token, AuthorizationMode authorizationMode) {
        return authorizationMode.getCode() + StringUtils.SPACE + token;
    }

    public ClaimsOptions toBean() throws CommonException {
        return toBean(ClaimsOptions.class);
    }
    public ClaimsOptions toBean(Claims claims) throws CommonException {
        return toBean(claims, ClaimsOptions.class);
    }
    public ClaimsOptions toBean(String token) throws CommonException {
        return toBean(token, ClaimsOptions.class);
    }

    public ClaimsOptions toBean(TokenAgeing tokenAgeing) throws CommonException {
        return toBean(tokenAgeing, ClaimsOptions.class);
    }

    public <T> T toBean(Class<T> claimsOptionsType) throws CommonException {
        return toBean(TokenAgeing.ROUTINE, claimsOptionsType);
    }

    public <T> T toBean(TokenAgeing tokenAgeing, Class<T> claimsOptionsType) throws CommonException {
        return toBean(parser(tokenAgeing).getBody(), claimsOptionsType);
    }
    public <T> T toBean(String token, Class<T> claimsOptionsType) throws CommonException {
        return toBean(parser(token).getBody(), claimsOptionsType);
    }

    public <T> T toBean(Claims claims, Class<T> claimsOptionsType) throws CommonException {
        return JsonUtils.toBean(JsonUtils.toString(claims), claimsOptionsType);
    }

    public Jws<Claims> parser() throws CommonException {
        return parser(TokenAgeing.ROUTINE);
    }

    public Jws<Claims> parser(TokenAgeing tokenAgeing) throws CommonException {
        return parser(read(tokenAgeing));
    }

    public Jws<Claims> parser(String token) throws CommonException {
        return parser(token, ssoProperties.getRsa().getPublicKey());
    }

    public Jws<Claims> parser(String token, PublicKey publicKey) throws CommonException {
        try {
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CommonException(CommonResponseStatus.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new CommonException(CommonResponseStatus.TOKEN_INVALID);
        }
    }

    public String getNamespace(TokenAgeing tokenAgeing, String signature) {
        return "TOKEN:" + tokenAgeing + ":" + SpringUtils.getApplicationName() + ":" + signature;
    }
}
