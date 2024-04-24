/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serial;

@Slf4j
public class JsonMapper extends ObjectMapper {

    @Serial
	private static final long serialVersionUID = 1L;

    private static JsonMapper mapper;

    public static JsonMapper getInstance() {
        if (mapper == null) {
            mapper = new JsonMapper().enableSimple();
        }
        return mapper;
    }

    public String toJson(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            log.warn("Object to json failed: " + object, e);
            return null;
        }
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.warn("Json to object failed: " + jsonString, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.readValue(jsonString, javaType);
        } catch (IOException e) {
            log.warn("Json to object failed: " + jsonString, e);
            return null;
        }
    }

    public static Object fromJsonObject(String jsonString, JavaType javaType) {
        return JsonMapper.getInstance().fromJson(jsonString, javaType);
    }

    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public JsonMapper enableSimple() {
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return this;
    }

    public ObjectMapper getMapper() {
        return this;
    }

    public static String toJsonString(Object object) {
        return JsonMapper.getInstance().toJson(object);
    }

    public static Object fromJsonString(String jsonString, Class<?> clazz) {
        return JsonMapper.getInstance().fromJson(jsonString, clazz);
    }
}
