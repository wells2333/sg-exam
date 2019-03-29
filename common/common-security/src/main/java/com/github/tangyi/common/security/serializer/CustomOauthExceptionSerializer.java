package com.github.tangyi.common.security.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.tangyi.common.security.exceptions.CustomOauthException;

import java.io.IOException;
import java.util.Map;

/**
 * 定制OauthException序列化
 *
 * @author tangyi
 * @date 2019/3/18 22:37
 */
public class CustomOauthExceptionSerializer extends StdSerializer<CustomOauthException> {
    public CustomOauthExceptionSerializer() {
        super(CustomOauthException.class);
    }

    @Override
    public void serialize(CustomOauthException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        // 封装属性，和ResponseBean对应
        gen.writeNumberField("status", value.getCode());
        // 返回给前端的code
        gen.writeNumberField("code", value.getCode());
        // 提示信息
        gen.writeStringField("msg", value.getMessage());
        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                gen.writeStringField(key, add);
            }
        }
        gen.writeEndObject();
    }
}
