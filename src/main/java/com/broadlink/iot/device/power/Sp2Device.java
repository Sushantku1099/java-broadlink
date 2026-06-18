package com.broadlink.iot.device.power;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** SP2 - Smart plug. GEN1. */
public class Sp2Device extends BroadlinkDevice {
    public Sp2Device(DeviceMetadata m) { super(m); this.type = "SP2"; }
    public CompletableFuture<Void> setPower(boolean pwr) {
        byte[] p = new byte[16]; p[0] = 2; p[4] = (byte)(pwr ? 1 : 0);
        return sendPacket(p).thenApply(r -> null);
    }
    public CompletableFuture<Boolean> checkPower() {
        byte[] p = new byte[16]; p[0] = 1;
        return sendPacket(p).thenApply(r -> codec.decryptResponsePayload(r, identity)[0x4] != 0);
    }
}
