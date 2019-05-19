package com.github.tangyi.gateway.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.tangyi.gateway.model.AccessToken;

import java.io.IOException;

/**
 * @author tangyi
 * @date 2019/5/19 15:47
 */
public class AccessTokenJacksonSerializer extends StdSerializer<AccessToken> {

    public AccessTokenJacksonSerializer() {
        super(AccessToken.class);
    }

    @Override
    public void serialize(AccessToken accessToken, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("access_token", accessToken.getJti());
        gen.writeNumberField("expires_in", accessToken.getExpiresIn());
        gen.writeStringField("refresh_token", accessToken.getJti());
        gen.writeStringField("scope", accessToken.getScope());
        gen.writeStringField("token_type", accessToken.getTokenType());
        gen.writeEndObject();
    }
}
