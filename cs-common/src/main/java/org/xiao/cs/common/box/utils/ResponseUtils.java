package org.xiao.cs.common.box.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseUtils {

    public static void toJson(HttpServletResponse response, Object o) throws IOException {
        send(response, o, MimeTypeUtils.APPLICATION_JSON_VALUE);
    }

    public static void send(HttpServletResponse response, Object o, String contentType) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(contentType);
        PrintWriter writer = response.getWriter();
        writer.println(JsonUtils.toString(o));
        writer.flush();
        writer.close();
    }
}
