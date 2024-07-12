package org.xiao.cs.sso.box.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.xiao.cs.common.box.utils.SpringUtils;

public class EncoderUtils {

    private static final PasswordEncoder passwordEncoder = SpringUtils.getBean(PasswordEncoder.class);
    public static String encode (String password) {
        return passwordEncoder.encode(password);
    }
}
