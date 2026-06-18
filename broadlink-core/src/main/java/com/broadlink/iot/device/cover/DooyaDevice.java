package com.broadlink.iot.device.cover;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import java.util.concurrent.*;

/** Dooya DT360E - Curtain motor. GEN1. */
public class DooyaDevice extends BroadlinkDevice {
    private static final ScheduledExecutorService SCH = Executors.newScheduledThreadPool(1);

    public DooyaDevice(DeviceMetadata m) { super(m); this.type = "Dooya"; }

    private CompletableFuture<Integer> send(int m1, int m2) {
        byte[] p = new byte[16]; p[0]=0x09; p[2]=(byte)0xBB; p[3]=(byte)m1; p[4]=(byte)m2; p[9]=(byte)0xFA; p[10]=0x44;
        return sendPacket(p).thenApply(r -> codec.decryptResponsePayload(r, identity)[4] & 0xFF);
    }

    public CompletableFuture<Integer> open() { return send(0x01,0x00); }
    public CompletableFuture<Integer> closeCurtain() { return send(0x02,0x00); }
    public CompletableFuture<Integer> stop() { return send(0x03,0x00); }
    public CompletableFuture<Integer> getPercentage() { return send(0x06,0x5D); }

    public CompletableFuture<Integer> setPercentageAndWait(int target) {
        CompletableFuture<Integer> result = new CompletableFuture<>();
        getPercentage().thenAccept(cur -> {
            if (cur > target) closeCurtain().thenAccept(r -> pollTo(target, true, result));
            else if (cur < target) open().thenAccept(r -> pollTo(target, false, result));
            else result.complete(cur);
        }).exceptionally(ex -> { result.completeExceptionally(ex); return null; });
        return result;
    }

    private void pollTo(int target, boolean closing, CompletableFuture<Integer> result) {
        SCH.schedule(() -> getPercentage().thenAccept(cur -> {
            boolean reached = closing ? cur <= target : cur >= target;
            if (reached) stop().thenAccept(r -> result.complete(cur));
            else pollTo(target, closing, result);        }).exceptionally(ex -> { result.completeExceptionally(ex); return null; }), 200, TimeUnit.MILLISECONDS);
    }
}
