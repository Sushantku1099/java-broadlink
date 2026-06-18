package com.broadlink.iot.device.power;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** SP3 - Smart plug with nightlight. GEN1. bit0=nightlight, bit1=power. */
public class Sp3Device extends BroadlinkDevice {
    public Sp3Device(DeviceMetadata m) { super(m); this.type = "SP3"; }
    public CompletableFuture<Void> setPower(boolean pwr) {
        return checkNightlight().thenCompose(nl -> {
            byte[] p = new byte[16]; p[0] = 2;
            p[4] = (byte)(((nl?1:0) << 1) | (pwr?1:0));
            return sendPacket(p).thenApply(r -> null);
        });
    }
    public CompletableFuture<Void> setNightlight(boolean nl) {
        return checkPower().thenCompose(pwr -> {
            byte[] p = new byte[16]; p[0] = 2;
            p[4] = (byte)(((nl?1:0) << 1) | (pwr?1:0));
            return sendPacket(p).thenApply(r -> null);
        });
    }
    public CompletableFuture<Boolean> checkPower() {
        byte[] p = new byte[16]; p[0] = 1;
        return sendPacket(p).thenApply(r -> (codec.decryptResponsePayload(r, identity)[0x4] & 2) != 0);
    }
    public CompletableFuture<Boolean> checkNightlight() {
        byte[] p = new byte[16]; p[0] = 1;
        return sendPacket(p).thenApply(r -> (codec.decryptResponsePayload(r, identity)[0x4] & 1) != 0);
    }
}
