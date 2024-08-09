package org.xiao.cs.common.box.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = SpringUtils.getBean(ObjectMapper.class);

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj.getClass() == String.class) {
            return (String) obj;
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Json 序列化出错: {}", obj, e);
            return null;
        }
    }

    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error("Json 序列化出错: {}", json, e);
            return null;
        }
    }

    public static <T> T toBean(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error("Json 序列化出错: {}", json, e);
            return null;
        }
    }

    public static <T> T toBean(InputStream inputStream, Class<T> tClass) {
        try {
            return mapper.readValue(inputStream, tClass);
        } catch (IOException e) {
            log.error("Json 序列化出错: {}", inputStream, e);
            return null;
        }
    }

    public static <T> T toBean(InputStream inputStream, TypeReference<T> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            log.error("Json 序列化出错: {}", inputStream, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("Json 序列化出错: {}", json, e);
            return null;
        }
    }

    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            log.error("Json 序列化出错: {}", json, e);
            return null;
        }
    }
}
