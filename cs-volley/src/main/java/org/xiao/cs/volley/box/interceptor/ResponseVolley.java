package org.xiao.cs.volley.box.interceptor;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.xiao.cs.common.box.domain.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.xiao.cs.common.box.utils.JsonUtils;

@RestControllerAdvice
public class ResponseVolley implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@Nullable MethodParameter returnType,
                            @Nullable Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  @Nullable MethodParameter returnType,
                                  @Nullable MediaType selectedContentType,
                                  @Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nullable ServerHttpRequest request,
                                  @Nullable ServerHttpResponse response) {

        if (body instanceof Page<?> page) {
            PageInfo<?> pageInfo = new PageInfo<>(page, page.getPageSize());
            return CommonResponse.Builder.success(pageInfo);
        } else if (body instanceof CommonResponse<?>) {
            return body;
        } else {
            return body instanceof String ? JsonUtils.toString(CommonResponse.Builder.success(body)) : CommonResponse.Builder.success(body);
        }
    }
}
