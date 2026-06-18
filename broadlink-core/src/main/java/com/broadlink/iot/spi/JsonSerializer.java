package com.broadlink.iot.spi;

public interface JsonSerializer {
    <T> T fromJson(byte[] data, Class<T> type);
    byte[] toJson(Object obj);

    static JsonSerializer getInstance() {
        try {
            for (JsonSerializer s : java.util.ServiceLoader.load(JsonSerializer.class)) return s;
        } catch (Exception ignored) {}
        return new ManualJsonSerializer();
    }
}
