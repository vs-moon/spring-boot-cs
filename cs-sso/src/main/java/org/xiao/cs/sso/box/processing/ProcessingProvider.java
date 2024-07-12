package org.xiao.cs.sso.box.processing;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.xiao.cs.common.box.enumerate.CommonResponseStatus;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.common.box.utils.ScopeCacheUtils;
import org.xiao.cs.sso.box.domain.AccountBody;
import org.xiao.cs.sso.box.condition.ProcessingConfigServiceCondition;

import java.util.Collections;

import static org.xiao.cs.sso.box.constant.ProcessingConstant.SCOPE_CACHE_CURRENT_ACCOUNT_BODY_KEY;

@Component
@Conditional(ProcessingConfigServiceCondition.class)
public class ProcessingProvider implements AuthenticationProvider {

    @Resource
    PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService = SpringUtils.getBean(UserDetailsService.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        AccountBody accountBody = (AccountBody) userDetailsService.loadUserByUsername(username);

        if (accountBody == null || accountBody.getBasic() == null) {
            throw new UsernameNotFoundException(CommonResponseStatus.ACCOUNT_ABSENT.getMessage());
        } else if (!passwordEncoder.matches(password, accountBody.getPassword())) {
            throw new BadCredentialsException(CommonResponseStatus.ACCOUNT_PASSWORD_ERROR.getMessage());
        } else if (!accountBody.isAccountNonLocked()) {
            throw new LockedException(CommonResponseStatus.ACCOUNT_LOCK.getMessage());
        } else if (!accountBody.isEnabled()) {
            throw new DisabledException(CommonResponseStatus.ACCOUNT_INVALID.getMessage());
        } else if (!accountBody.isAccountNonExpired()) {
            throw new AccountExpiredException(CommonResponseStatus.ACCOUNT_EXPIRED.getMessage());
        } else if (!accountBody.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(CommonResponseStatus.ACCOUNT_CREDENTIALS_EXPIRED.getMessage());
        } else {
            ScopeCacheUtils.put(SCOPE_CACHE_CURRENT_ACCOUNT_BODY_KEY, accountBody);
            return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
