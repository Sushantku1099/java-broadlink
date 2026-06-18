package com.broadlink.iot.device.light;

import com.broadlink.iot.codec.JsonCodec;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.model.LightBulbState;
import java.util.concurrent.CompletableFuture;

/** Lb1 - Smart bulb. GEN4B. */
public class Lb1Device extends AbstractLightDevice<LightBulbState> {
    public Lb1Device(DeviceMetadata m) { super(m); this.type = "SmartBulb"; }
    public CompletableFuture<LightBulbState> getState() { byte[] p = JsonCodec.encodeGen4B(1, new LightBulbState()); return sendPacketAndDecrypt(p).thenApply(d -> JsonCodec.decodeGen4B(d, LightBulbState.class)); }
    public CompletableFuture<LightBulbState> setState(LightBulbState s) { byte[] p = JsonCodec.encodeGen4B(2, s); return sendPacket(p).thenApply(r -> JsonCodec.decodeGen4B(codec.decryptResponsePayload(r, identity), LightBulbState.class)); }
}
