package com.broadlink.iot.device.hub;

import com.broadlink.iot.codec.JsonCodec;
import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.model.S3State;
import com.broadlink.iot.spi.ManualJsonSerializer;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/** S3 - Smart Hub. GEN4. Paginated sub-device listing. */
public class S3HubDevice extends BroadlinkDevice {
    private static final int PAGE = 5;

    public S3HubDevice(DeviceMetadata m) { super(m); this.type = "S3"; }

    public CompletableFuture<List<String>> getSubDevices() {
        return fetchPage(new ArrayList<>(), new int[]{0, PAGE, 8}, 0);
    }

    private CompletableFuture<List<String>> fetchPage(List<String> acc, int[] idxs, int i) {
        if (i >= idxs.length) return CompletableFuture.completedFuture(acc);
        int idx = idxs[i];
        Map<String,Object> req = new HashMap<>();
        req.put("count", PAGE); req.put("index", idx);
        byte[] p = JsonCodec.encodeGen4(14, req);
        return sendPacketAndDecrypt(p).thenCompose(d -> {
            ManualJsonSerializer.S3HubPage pg =
                JsonCodec.decodeGen4(d, ManualJsonSerializer.S3HubPage.class);
            acc.addAll(pg.list);
            if (acc.size() >= pg.total || i == idxs.length - 1)
                return CompletableFuture.completedFuture(acc);
            return fetchPage(acc, idxs, i + 1);
        });
    }

    public CompletableFuture<S3State> getState() { return getState(null); }
    public CompletableFuture<S3State> getState(String did) {
        Map<String,Object> m = new HashMap<>(); if (did != null) m.put("did", did);
        byte[] p = JsonCodec.encodeGen4(1, m);
        return sendPacketAndDecrypt(p).thenApply(d -> JsonCodec.decodeGen4(d, S3State.class));
    }

    public CompletableFuture<S3State> setState(S3State s) {
        byte[] p = JsonCodec.encodeGen4(2, s);
        return sendPacketAndDecrypt(p).thenApply(d -> JsonCodec.decodeGen4(d, S3State.class));
    }
}
