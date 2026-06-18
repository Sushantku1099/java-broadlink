package com.broadlink.iot.device.sensor;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.protocol.BinaryStruct;
import java.util.concurrent.CompletableFuture;

/** A1 - Environmental sensor. */
public class A1Device extends BroadlinkDevice {
    private static final BinaryStruct.StructCodec TMP = BinaryStruct.compile("<bb");
    private static final String[] L = {"dark","dim","normal","bright"};
    private static final String[] A = {"excellent","good","normal","bad"};
    private static final String[] N = {"quiet","normal","noisy"};

    public A1Device(DeviceMetadata m) { super(m); this.type = "A1"; }

    public CompletableFuture<A1SensorData> checkSensors() {
        byte[] p = {0x01};
        return sendPacket(p).thenApply(r -> {
            byte[] d = codec.decryptResponsePayload(r, identity);
            int[] t = TMP.unpack(d, 4);
            return new A1SensorData(t[0]+t[1]/10.0, (d[6]&0xFF)+(d[7]&0xFF)/10.0,
                label(L, d[8]), label(A, d[10]), label(N, d[12]));
        });
    }

    private static String label(String[] a, int i) { return i>=0&&i<a.length?a[i]:"unknown"; }

    public static class A1SensorData { public final double t,h; public final String l,a,n; public A1SensorData(double t,double h,String l,String a,String n){this.t=t;this.h=h;this.l=l;this.a=a;this.n=n;} }
}
