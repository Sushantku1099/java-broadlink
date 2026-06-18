package com.broadlink.iot.device.power;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.CompletableFuture;

/** MP1 - 4-socket power strip. GEN1. */
public class Mp1Device extends BroadlinkDevice {
    public Mp1Device(DeviceMetadata m) { super(m); this.type = "MP1"; }

    public CompletableFuture<Void> setPower(int sid, boolean state) {
        int sm = 0x01 << (sid - 1);
        byte[] p = new byte[16];
        p[0]=0x0D; p[2]=(byte)0xA5; p[3]=(byte)0xA5; p[4]=0x5A; p[5]=0x5A;
        p[6]=(byte)(0xB2 + (state ? sm << 1 : sm)); p[7]=(byte)0xC0; p[8]=0x02; p[0xA]=0x03;
        p[0xD]=(byte)sm; p[0xE]=(byte)(state ? sm : 0);
        return sendPacket(p).thenApply(r -> null);
    }

    public CompletableFuture<Mp1PowerState> checkPower() {
        byte[] p = new byte[16];
        p[0]=0x0A; p[2]=(byte)0xA5; p[3]=(byte)0xA5; p[4]=0x5A; p[5]=0x5A;
        p[6]=(byte)0xAE; p[7]=(byte)0xC0; p[8]=0x01;
        return sendPacket(p).thenApply(r -> {
            int st = codec.decryptResponsePayload(r, identity)[0x0E] & 0xFF;
            return new Mp1PowerState((st & 1) != 0, (st & 2) != 0, (st & 4) != 0, (st & 8) != 0);
        });
    }

    public static class Mp1PowerState { public final boolean s1,s2,s3,s4; public Mp1PowerState(boolean s1,boolean s2,boolean s3,boolean s4){this.s1=s1;this.s2=s2;this.s3=s3;this.s4=s4;} }
}
