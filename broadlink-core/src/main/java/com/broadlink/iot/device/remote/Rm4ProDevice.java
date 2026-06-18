package com.broadlink.iot.device.remote;

import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** RM4 pro - IR+RF + temp + humidity. GEN3. */
public class Rm4ProDevice extends Rm4MiniDevice {
    public Rm4ProDevice(DeviceMetadata m) { super(m); this.type = "RM4PRO"; }
    public CompletableFuture<Void> sweepFrequency() { return send(0x19).thenApply(r -> null); }
    public CompletableFuture<Void> cancelSweepFrequency() { return send(0x1E).thenApply(r -> null); }
    public CompletableFuture<Boolean> checkFrequency() { return send(0x1A).thenApply(r -> r[0] == 1); }
    public CompletableFuture<Void> findRfPacket() { return send(0x1B).thenApply(r -> null); }
    public CompletableFuture<Double> checkTemperature() { return checkSensors().thenApply(v -> v[0]); }
}
