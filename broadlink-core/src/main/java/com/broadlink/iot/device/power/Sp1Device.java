package com.broadlink.iot.device.power;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

public class Sp1Device extends BroadlinkDevice {
    public Sp1Device(DeviceMetadata m) { super(m); this.type = "SP1"; }
    public CompletableFuture<Void> setPower(boolean pwr) {
        byte[] p = new byte[4]; p[0] = (byte)(pwr ? 1 : 0);
        return sendPacket(p).thenApply(r -> null);
    }
}
