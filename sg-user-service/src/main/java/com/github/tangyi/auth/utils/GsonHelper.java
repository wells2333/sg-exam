package com.github.tangyi.auth.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonHelper {

    private static final Gson gson = new Gson();

    private volatile static GsonHelper instance;

    public static GsonHelper getInstance() {
        if (instance == null) {
            synchronized (GsonHelper.class) {
                if (instance == null)
                    instance = new GsonHelper();
            }
        }
        return instance;
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }
}
