package com.gto.bang.util;

import com.google.gson.JsonParseException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;

/**
 * Created by shenjialong on 16/7/24 10:10.
 */
public class JsonUtil {
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static final <T> T str2Obj(String s, Class<T> valueType) throws JsonParseException,
            IOException {
        return mapper.readValue(s, valueType);
    }

    public static final <T> T str2Obj(String s, TypeReference<T> typeReference)
            throws JsonParseException, IOException {
        return (T) mapper.readValue(s, typeReference);
    }

    public static final String obj2Str(Object obj) throws
            IOException {
        return mapper.writeValueAsString(obj);
    }
}
