package com.broadlink.iot.device.power;

import com.broadlink.iot.codec.JsonCodec;
import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.model.Bg1State;
import java.util.concurrent.CompletableFuture;

/** BG1 - BG Electrical gang switch. GEN4B. */
public class Bg1Device extends BroadlinkDevice {
    public Bg1Device(DeviceMetadata m) { super(m); this.type = "BG1"; }
    public CompletableFuture<Bg1State> getState() { byte[] p = JsonCodec.encodeGen4B(1, new Bg1State()); return sendPacket(p).thenApply(r -> JsonCodec.decodeGen4B(codec.decryptResponsePayload(r, identity), Bg1State.class)); }
    public CompletableFuture<Bg1State> setState(Bg1State s) { byte[] p = JsonCodec.encodeGen4B(2, s); return sendPacket(p).thenApply(r -> JsonCodec.decodeGen4B(codec.decryptResponsePayload(r, identity), Bg1State.class)); }
}
