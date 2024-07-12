package org.xiao.cs.sso.box.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.xiao.cs.common.box.domain.CommonRequest;
import org.xiao.cs.common.box.exception.CommonHttpException;
import org.xiao.cs.common.box.utils.JsonUtils;
import org.xiao.cs.sso.box.domain.AccountSign;

import java.io.IOException;

public class ProcessingParamFilter extends UsernamePasswordAuthenticationFilter {

    public ProcessingParamFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        CommonRequest<AccountSign> commonRequest;
        UsernamePasswordAuthenticationToken authentication = null;

        if (request.getMethod().equals(HttpMethod.POST.toString())) {
            if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                try {
                    commonRequest = JsonUtils.toBean(request.getInputStream(), new TypeReference<>() {});
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (commonRequest != null && commonRequest.getArgs() != null) {
                    authentication = UsernamePasswordAuthenticationToken.unauthenticated(commonRequest.getArgs().getUsername(), commonRequest.getArgs().getPassword());
                }
            } else {
                String username = obtainUsername(request);
                String password = obtainPassword(request);
                authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            }
        } else {
            throw new CommonHttpException(HttpStatus.METHOD_NOT_ALLOWED);
        }

        if (authentication != null) {
            this.setDetails(request, authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return this.getAuthenticationManager().authenticate(authentication);
    }
}
