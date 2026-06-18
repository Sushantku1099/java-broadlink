package com.broadlink.iot.device.remote;

import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.protocol.BinaryStruct;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/** RM mini B-variant. GEN3 (6-byte header: uint16 LE length + uint32 LE command). */
public class RmMiniBDevice extends RmMiniDevice {
    private static final BinaryStruct.StructCodec HDR = BinaryStruct.compile("<HI");
    private static final BinaryStruct.StructCodec LEN = BinaryStruct.compile("<H");

    public RmMiniBDevice(DeviceMetadata m) { super(m); this.type = "RMMINIB"; }

    protected CompletableFuture<byte[]> send(int command, byte[] data) {
        int len = data.length + 4;
        byte[] h = HDR.pack(len, command);
        byte[] p = new byte[h.length + data.length];
        System.arraycopy(h, 0, p, 0, h.length);
        System.arraycopy(data, 0, p, h.length, data.length);
        return sendPacketAndDecrypt(p).thenApply(r -> {
            int rl = LEN.unpack(r, 0)[0];
            return Arrays.copyOfRange(r, 6, rl + 2);
        });
    }
}
