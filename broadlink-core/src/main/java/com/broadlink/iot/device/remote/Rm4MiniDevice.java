package com.broadlink.iot.device.remote;

import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.protocol.BinaryStruct;
import java.util.concurrent.CompletableFuture;

/** RM4 mini - IR + temperature + humidity. GEN3. */
public class Rm4MiniDevice extends RmMiniBDevice {
    private static final BinaryStruct.StructCodec TMP = BinaryStruct.compile("<bb");

    public Rm4MiniDevice(DeviceMetadata m) { super(m); this.type = "RM4MINI"; }

    public CompletableFuture<double[]> checkSensors() {
        return send(0x24).thenApply(p -> {
            int[] t = TMP.unpack(p, 0);
            return new double[]{t[0] + t[1] / 100.0, (p[2] & 0xFF) + (p[3] & 0xFF) / 100.0};
        });
    }
    public CompletableFuture<Double> checkTemperature() { return checkSensors().thenApply(v -> v[0]); }
    public CompletableFuture<Double> checkHumidity() { return checkSensors().thenApply(v -> v[1]); }
}
