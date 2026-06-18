package com.broadlink.iot.device.light;

import com.broadlink.iot.codec.JsonCodec;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.model.LightBulbState;
import java.util.concurrent.CompletableFuture;

/** Lb2 - Smart bulb Rev 2. GEN4. */
public class Lb2Device extends AbstractLightDevice<LightBulbState> {
    public Lb2Device(DeviceMetadata m) { super(m); this.type = "LB2"; }
    public CompletableFuture<LightBulbState> getState() { byte[] p = JsonCodec.encodeGen4(1, new LightBulbState()); return sendPacketAndDecrypt(p).thenApply(d -> JsonCodec.decodeGen4(d, LightBulbState.class)); }
    public CompletableFuture<LightBulbState> setState(LightBulbState s) { byte[] p = JsonCodec.encodeGen4(2, s); return sendPacket(p).thenApply(r -> JsonCodec.decodeGen4(codec.decryptResponsePayload(r, identity), LightBulbState.class)); }
}
