package org.xiao.cs.remote.box.interceptor;

import feign.InvocationContext;
import feign.ResponseInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteResponseInterceptor implements ResponseInterceptor {

    @Override
    public Object intercept(InvocationContext invocationContext, Chain chain) throws Exception {
        return chain.next(invocationContext);
    }

    @Override
    public ResponseInterceptor andThen(ResponseInterceptor nextInterceptor) {
        return ResponseInterceptor.super.andThen(nextInterceptor);
    }

    @Override
    public Chain apply(Chain chain) {
        return ResponseInterceptor.super.apply(chain);
    }
}
