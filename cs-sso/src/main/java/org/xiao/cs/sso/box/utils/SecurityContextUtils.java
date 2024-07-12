package org.xiao.cs.sso.box.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {
    public static boolean isNotAnonymous() {
        return !isAnonymous();
    }
    public static boolean isAnonymous() {
        return getAuthentication() instanceof AnonymousAuthenticationToken;
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
