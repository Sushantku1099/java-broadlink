package com.broadlink.iot.device.power;

import com.broadlink.iot.codec.JsonCodec;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.model.Sp4State;
import java.util.concurrent.CompletableFuture;

/** SP4B - JSON Rev B. GEN4B (14-byte header). /1000 scaling. */
public class Sp4bDevice extends Sp4Device {
    public Sp4bDevice(DeviceMetadata m) { super(m); this.type = "SP4B"; }
    public CompletableFuture<Sp4State> setState(Sp4State s) { byte[] p = JsonCodec.encodeGen4B(2, s); return sendPacket(p).thenApply(r -> decodeG4B(r)); }
    public CompletableFuture<Sp4State> getState() { byte[] p = JsonCodec.encodeGen4B(1, new Sp4State()); return sendPacketAndDecrypt(p).thenApply(this::decodeG4B); }
    private Sp4State decodeG4B(byte[] r) { Sp4State s = JsonCodec.decodeGen4B(r, Sp4State.class); Integer nb = s.getNtlbrightness(); if (nb != null) s.setNtlbrightness(nb / 1000); Integer mw = s.getMaxworktime(); if (mw != null) s.setMaxworktime(mw / 1000); return s; }
}
