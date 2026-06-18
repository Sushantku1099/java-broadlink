package com.broadlink.iot.device.remote;

import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** RM pro/pro+ - IR+RF remote controller with temperature sensor. GEN2. */
public class RmProDevice extends RmMiniDevice {
    public RmProDevice(DeviceMetadata m) { super(m); this.type = "RMPRO"; }
    public CompletableFuture<Void> sweepFrequency() { return send(0x19).thenApply(r -> null); }
    public CompletableFuture<Void> cancelSweepFrequency() { return send(0x1E).thenApply(r -> null); }
    public CompletableFuture<Boolean> checkFrequency() { return send(0x1A).thenApply(r -> r[0] == 1); }
    public CompletableFuture<Void> findRfPacket() { return send(0x1B).thenApply(r -> null); }
    public CompletableFuture<Double> checkTemperature() { return readSensorRaw().thenApply(v -> v[0]); }
}
