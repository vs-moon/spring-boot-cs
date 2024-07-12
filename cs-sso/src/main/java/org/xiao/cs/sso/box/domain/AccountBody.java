package org.xiao.cs.sso.box.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.xiao.cs.common.box.constant.CommonConstant;
import org.xiao.cs.sso.box.enumerate.AuthoritySymbolStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class AccountBody implements UserDetails {

    private Basic basic;
    private AuthoritySymbol<String> authoritySymbol;
    private AuthoritySymbolStrategy authorityStrategy;

    public AccountBody() {
        this.authorityStrategy = AuthoritySymbolStrategy.PERMISSION;
    }

    /*********************************************************************************************************/

    public Basic getBasic() {
        return basic;
    }

    public AccountBody setBasic(Basic basic) {
        this.basic = basic;
        return this;
    }

    public AuthoritySymbol<String> getAuthoritySymbol() {
        return authoritySymbol;
    }

    public AccountBody setAuthoritySymbol(AuthoritySymbol<String> authoritySymbol) {
        this.authoritySymbol = authoritySymbol;
        return this;
    }

    public AuthoritySymbolStrategy getAuthorityStrategy() {
        return authorityStrategy;
    }

    public AccountBody setAuthorityStrategy(AuthoritySymbolStrategy authorityStrategy) {
        this.authorityStrategy = authorityStrategy;
        return this;
    }

    /*********************************************************************************************************/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();

        switch (authorityStrategy) {

            case ORG -> grantedAuthority.addAll(authoritySymbol.orgAuthority());

            case ROLE -> grantedAuthority.addAll(authoritySymbol.roleAuthority());

            case PERMISSION -> grantedAuthority.addAll(authoritySymbol.permissionAuthority());

            case ORG_ROLE -> {
                grantedAuthority.addAll(authoritySymbol.orgAuthority());
                grantedAuthority.addAll(authoritySymbol.roleAuthority());
            }

            case ORG_PERMISSION -> {
                grantedAuthority.addAll(authoritySymbol.orgAuthority());
                grantedAuthority.addAll(authoritySymbol.permissionAuthority());
            }

            case ROLE_PERMISSION -> {
                grantedAuthority.addAll(authoritySymbol.roleAuthority());
                grantedAuthority.addAll(authoritySymbol.permissionAuthority());
            }

            default -> {
                grantedAuthority.addAll(authoritySymbol.orgAuthority());
                grantedAuthority.addAll(authoritySymbol.roleAuthority());
                grantedAuthority.addAll(authoritySymbol.permissionAuthority());
            }
        }

        return grantedAuthority;
    }

    @Override
    public String getPassword() {
        return basic.getPassword();
    }

    @Override
    public String getUsername() {
        return basic.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return basic.getExpireTime() == null || basic.getExpireTime().after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return basic.getLockTime() == null || basic.getLockTime().before(new Date());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return CommonConstant.VALID_T.equals(basic.getValid());
    }

    public static class Basic implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Long id;

        private String number;

        private String username;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        private String nationality;

        private String identity;

        private String phone;

        private LocalDateTime birthday;

        private String sex;

        private String describe;

        private Integer tryCount;

        private Date lockTime;

        private Date expireTime;

        private String valid;

        public Basic() {}

        public Basic(Long id, String number, String username, String password, String nationality, String identity, String phone, LocalDateTime birthday, String sex, String describe, Integer tryCount, Date lockTime, Date expireTime, String valid) {
            this.id = id;
            this.number = number;
            this.username = username;
            this.password = password;
            this.nationality = nationality;
            this.identity = identity;
            this.phone = phone;
            this.birthday = birthday;
            this.sex = sex;
            this.describe = describe;
            this.tryCount = tryCount;
            this.lockTime = lockTime;
            this.expireTime = expireTime;
            this.valid = valid;
        }

        public Long getId() {
            return id;
        }

        public Basic setId(Long id) {
            this.id = id;
            return this;
        }

        public String getNumber() {
            return number;
        }

        public Basic setNumber(String number) {
            this.number = number;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public Basic setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getNationality() {
            return nationality;
        }

        public String getPassword() {
            return password;
        }

        public Basic setPassword(String password) {
            this.password = password;
            return this;
        }

        public Basic setNationality(String nationality) {
            this.nationality = nationality;
            return this;
        }

        public String getIdentity() {
            return identity;
        }

        public Basic setIdentity(String identity) {
            this.identity = identity;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public Basic setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public LocalDateTime getBirthday() {
            return birthday;
        }

        public Basic setBirthday(LocalDateTime birthday) {
            this.birthday = birthday;
            return this;
        }

        public String getSex() {
            return sex;
        }

        public Basic setSex(String sex) {
            this.sex = sex;
            return this;
        }

        public String getDescribe() {
            return describe;
        }

        public Basic setDescribe(String describe) {
            this.describe = describe;
            return this;
        }

        public Integer getTryCount() {
            return tryCount;
        }

        public Basic setTryCount(Integer tryCount) {
            this.tryCount = tryCount;
            return this;
        }

        public Date getLockTime() {
            return lockTime;
        }

        public Basic setLockTime(Date lockTime) {
            this.lockTime = lockTime;
            return this;
        }

        public Date getExpireTime() {
            return expireTime;
        }

        public Basic setExpireTime(Date expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        public String getValid() {
            return valid;
        }

        public Basic setValid(String valid) {
            this.valid = valid;
            return this;
        }
    }

    public static class Expand implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String id;

        private String nationality;

        private String identity;

        private String phone;

        private Date birthday;

        private String sex;

        private String describe;

        public Expand() {}

        public Expand(String id, String nationality, String identity, String phone, Date birthday, String sex, String describe) {
            this.id = id;
            this.nationality = nationality;
            this.identity = identity;
            this.phone = phone;
            this.birthday = birthday;
            this.sex = sex;
            this.describe = describe;
        }

        public String getId() {
            return id;
        }

        public Expand setId(String id) {
            this.id = id;
            return this;
        }

        public String getNationality() {
            return nationality;
        }

        public Expand setNationality(String nationality) {
            this.nationality = nationality;
            return this;
        }

        public String getIdentity() {
            return identity;
        }

        public Expand setIdentity(String identity) {
            this.identity = identity;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public Expand setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Date getBirthday() {
            return birthday;
        }

        public Expand setBirthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public String getSex() {
            return sex;
        }

        public Expand setSex(String sex) {
            this.sex = sex;
            return this;
        }

        public String getDescribe() {
            return describe;
        }

        public Expand setDescribe(String describe) {
            this.describe = describe;
            return this;
        }
    }
}
