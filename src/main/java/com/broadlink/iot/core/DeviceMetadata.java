package com.broadlink.iot.core;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Immutable metadata for a discovered Broadlink device.
 */
public class DeviceMetadata {

    private final InetSocketAddress host;
    private final byte[] mac;
    private final int deviceType;
    private final String model;
    private final String manufacturer;
    private final String name;
    private final boolean locked;

    public DeviceMetadata(InetSocketAddress host, byte[] mac, int deviceType,
                          String model, String manufacturer, String name, boolean locked) {
        this.host = host;
        this.mac = mac.clone();
        this.deviceType = deviceType;
        this.model = model != null ? model : "";
        this.manufacturer = manufacturer != null ? manufacturer : "";
        this.name = name != null ? name : "";
        this.locked = locked;
    }

    public InetSocketAddress getHost() { return host; }
    public byte[] getMac() { return mac.clone(); }
    public int getDeviceType() { return deviceType; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public String getName() { return name; }
    public boolean isLocked() { return locked; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceMetadata)) return false;
        DeviceMetadata dm = (DeviceMetadata) o;
        return deviceType == dm.deviceType && Arrays.equals(mac, dm.mac);
    }

    @Override
    public int hashCode() {
        return 31 * deviceType + Arrays.hashCode(mac);
    }
}
