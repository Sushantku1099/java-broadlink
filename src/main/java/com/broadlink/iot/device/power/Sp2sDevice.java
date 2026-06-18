package com.broadlink.iot.device.power;

import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** SP2s - Smart plug with energy monitoring. */
public class Sp2sDevice extends Sp2Device {
    public Sp2sDevice(DeviceMetadata m) { super(m); this.type = "SP2S"; }
    public CompletableFuture<Double> getEnergy() {
        byte[] p = new byte[16]; p[0] = 4;
        return sendPacketAndDecrypt(p).thenApply(r -> r[0x4] / 1000.0);
    }
}
