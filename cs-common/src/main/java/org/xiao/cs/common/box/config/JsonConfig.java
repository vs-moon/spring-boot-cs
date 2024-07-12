package org.xiao.cs.common.box.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.xiao.cs.common.box.serializer.BigNumberSerializer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class JsonConfig {

    public static final String FORMATTER_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATTER_DATE = "yyyy-MM-dd";
    public static final String FORMATTER_TIME = "HH:mm:ss";

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            setJavaTimeModule(builder);
            DateFormat dateFormat = new SimpleDateFormat(FORMATTER_DATE_TIME);
            dateFormat.setTimeZone(TIME_ZONE);
            builder.dateFormat(dateFormat);
            builder.timeZone(TIME_ZONE);
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        };
    }

    public void setJavaTimeModule(Jackson2ObjectMapperBuilder builder) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Long.class, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(Long.TYPE, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(FORMATTER_DATE_TIME)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(FORMATTER_DATE)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(FORMATTER_TIME)));
        javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(FORMATTER_DATE_TIME)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(FORMATTER_DATE)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(FORMATTER_TIME)));
        javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);

        builder.modules(javaTimeModule);
    }
}
