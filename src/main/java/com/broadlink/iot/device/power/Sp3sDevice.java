package com.broadlink.iot.device.power;

import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** SP3s - Extends Sp2 (not Sp3). Has energy but no nightlight API. */
public class Sp3sDevice extends Sp2Device {
    public Sp3sDevice(DeviceMetadata m) { super(m); this.type = "SP3S"; }
    public CompletableFuture<Double> getEnergy() {
        byte[] p = {8, 0, (byte)254, 1, 5, 1, 0, 0, 0, 45};
        return sendPacketAndDecrypt(p).thenApply(r -> (r[0x7a] & 0xFF) / 100.0);
    }
}
