package com.broadlink.iot.json;

import com.broadlink.iot.spi.JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;

/**
 * Gson-based JSON serializer for Android.
 * Gson is already in most Android projects — no extra dependency.
 */
public class GsonSerializer implements JsonSerializer {

    private static final Gson GSON = new GsonBuilder()
        .serializeNulls()
        .create();

    @Override
    public <T> T fromJson(byte[] json, Class<T> type) {
        return GSON.fromJson(new String(json, StandardCharsets.UTF_8), type);
    }

    @Override
    public byte[] toJson(Object object) {
        return GSON.toJson(object).getBytes(StandardCharsets.UTF_8);
    }
}
