package org.xiao.cs.common.box.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

@JacksonStdImpl
public class BigNumberSerializer extends NumberSerializer {

    public static final BigNumberSerializer INSTANCE = new BigNumberSerializer(Number.class);
    private static final long MAX_SAFE_INTEGER = 9007199254740991L;
    private static final long MIN_SAFE_INTEGER = -9007199254740991L;

    public BigNumberSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

    @Override
    public void serialize(Number value, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (value.longValue() > MIN_SAFE_INTEGER && value.longValue() < MAX_SAFE_INTEGER) {
            super.serialize(value, g, provider);
        } else {
            g.writeString(value.toString());
        }
    }
}
