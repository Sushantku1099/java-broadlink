package com.broadlink.iot.device.climate;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.protocol.Crc16Modbus;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/** Hysen HY02/HY03 - Thermostat. Modbus-like + CRC-16. */
public class HysenDevice extends BroadlinkDevice {
    public HysenDevice(DeviceMetadata m) { super(m); this.type = "Hysen"; }

    private CompletableFuture<byte[]> sendRequest(byte[] mp) {
        int len = mp.length + 2;
        int crc = Crc16Modbus.compute(mp);
        byte[] cr = Crc16Modbus.toBytesLE(crc);
        byte[] p = new byte[2 + mp.length + 2];
        p[0]=(byte)(len&0xFF); p[1]=(byte)((len>>8)&0xFF);
        System.arraycopy(mp,0,p,2,mp.length);
        System.arraycopy(cr,0,p,2+mp.length,2);
        return sendPacket(p).thenApply(r -> {
            byte[] d = codec.decryptResponsePayload(r, identity);
            int rl = d[0] & 0xFF;
            if (rl + 2 > d.length) throw new RuntimeException("Hysen: bad length");
            return Arrays.copyOfRange(d, 2, rl);
        });
    }

    public CompletableFuture<Double> getTemp() { return sendRequest(new byte[]{0x01,0x03,0,0,0,0x08}).thenApply(p -> (p[5]&0xFF)/2.0); }
    public CompletableFuture<Double> getExternalTemp() { return sendRequest(new byte[]{0x01,0x03,0,0,0,0x08}).thenApply(p -> (p[18]&0xFF)/2.0); }
    public CompletableFuture<Void> setMode(int am, int lm, int sn) { return sendRequest(new byte[]{0x01,0x06,0,0x02,(byte)(((lm+1)<<4)+am),(byte)sn}).thenApply(r -> null); }
    public CompletableFuture<Void> setTemp(double t) { int v = (int)(t*2); return sendRequest(new byte[]{0x01,0x06,0,0x01,0,(byte)v}).thenApply(r -> null); }
    public CompletableFuture<Void> setPower(int pw, int rl) { return sendRequest(new byte[]{0x01,0x06,0,0,(byte)rl,(byte)pw}).thenApply(r -> null); }
    public CompletableFuture<Void> setTime(int h,int m,int s,int d) { return sendRequest(new byte[]{0x01,0x10,0,0x08,0,0x02,0x04,(byte)h,(byte)m,(byte)s,(byte)d}).thenApply(r -> null); }
}
