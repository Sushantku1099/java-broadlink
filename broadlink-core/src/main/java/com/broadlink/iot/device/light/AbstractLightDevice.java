package com.broadlink.iot.device.light;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** Abstract base for Lb1/Lb2 smart bulbs. */
public abstract class AbstractLightDevice<T> extends BroadlinkDevice {
    public AbstractLightDevice(DeviceMetadata m) { super(m); }
    public abstract CompletableFuture<T> getState();
    public abstract CompletableFuture<T> setState(T state);
}
