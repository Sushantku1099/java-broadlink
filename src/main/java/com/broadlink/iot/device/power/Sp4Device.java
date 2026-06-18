package com.broadlink.iot.device.power;

import com.broadlink.iot.codec.JsonCodec;
import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.model.Sp4State;
import java.util.concurrent.CompletableFuture;

/** SP4 - JSON smart plug. GEN4 (12-byte header). */
public class Sp4Device extends BroadlinkDevice {
    public Sp4Device(DeviceMetadata m) { super(m); this.type = "SP4"; }
    public CompletableFuture<Void> setPower(boolean p) { Sp4State s = new Sp4State(); s.setPwr(p); return setState(s).thenApply(r -> null); }
    public CompletableFuture<Void> setNightlight(boolean n) { Sp4State s = new Sp4State(); s.setNtlight(n); return setState(s).thenApply(r -> null); }
    public CompletableFuture<Sp4State> setState(Sp4State s) { byte[] p = JsonCodec.encodeGen4(2, s); return sendPacket(p).thenApply(r -> JsonCodec.decodeGen4(codec.decryptResponsePayload(r, identity), Sp4State.class)); }
    public CompletableFuture<Sp4State> getState() { byte[] p = JsonCodec.encodeGen4(1, new Sp4State()); return sendPacketAndDecrypt(p).thenApply(r -> JsonCodec.decodeGen4(r, Sp4State.class)); }
    public CompletableFuture<Boolean> checkPower() { return getState().thenApply(Sp4State::isPwr); }
    public CompletableFuture<Boolean> checkNightlight() { return getState().thenApply(Sp4State::isNtlight); }
}
