package com.broadlink.iot.device.alarm;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/** S1C / S2KIT - Alarm system. GEN1. */
public class S1CDevice extends BroadlinkDevice {
    private static final Map<Integer,String> TYPES = new HashMap<>();
    static { TYPES.put(0x31,"Door Sensor"); TYPES.put(0x91,"Key Fob"); TYPES.put(0x21,"Motion Sensor"); }

    public S1CDevice(DeviceMetadata m) { super(m); this.type = "S1C"; }

    public CompletableFuture<S1CStatus> getSensorsStatus() {
        byte[] p = new byte[16]; p[0] = 0x06;
        return sendPacketAndDecrypt(p).thenApply(pl -> {
            int cnt = pl[4] & 0xFF;
            byte[] recs = Arrays.copyOfRange(pl, 6, pl.length);
            int rn = recs.length / 83;
            List<S1CSensor> sens = new ArrayList<>();
            for (int i = 0; i < rn; i++) {
                int off = i * 83;
                String ser = hex(recs, off+26, 4);
                if (!"00000000".equals(ser))
                    sens.add(new S1CSensor(recs[off]&0xFF, TYPES.getOrDefault(recs[off+3]&0xFF,"Unknown"), recs[off+1]&0xFF, ser));
            }
            return new S1CStatus(cnt, sens);
        });
    }

    private static String hex(byte[] d, int o, int l) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < l; i++) sb.append(String.format("%02x", d[o+i]&0xFF)); return sb.toString(); }

    public static class S1CStatus { public final int count; public final List<S1CSensor> sensors; public S1CStatus(int c, List<S1CSensor> s) { this.count=c; this.sensors=s; } }
    public static class S1CSensor { public final int status,order; public final String type,serial; public S1CSensor(int st,String ty,int or,String se){this.status=st;this.type=ty;this.order=or;this.serial=se;} }
}
