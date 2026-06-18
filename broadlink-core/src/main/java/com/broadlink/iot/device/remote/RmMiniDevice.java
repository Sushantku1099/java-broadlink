package com.broadlink.iot.device.remote;

import com.broadlink.iot.core.BroadlinkDevice;
import com.broadlink.iot.core.DeviceMetadata;
import com.broadlink.iot.protocol.BinaryStruct;
import java.util.concurrent.CompletableFuture;

/**
 * RM mini - IR-only remote controller. Protocol: GEN2 (4-byte uint32 LE command).
 */
public class RmMiniDevice extends BroadlinkDevice {

    private static final BinaryStruct.StructCodec CMD = BinaryStruct.compile("<I");
    private static final BinaryStruct.StructCodec SENSOR = BinaryStruct.compile("<bb");

    public RmMiniDevice(DeviceMetadata metadata) {
        super(metadata);
        this.type = "RMMINI";
    }

    protected CompletableFuture<byte[]> send(int command, byte[] data) {
        byte[] header = CMD.pack(command);
        byte[] payload = new byte[header.length + data.length];
        System.arraycopy(header, 0, payload, 0, header.length);
        System.arraycopy(data, 0, payload, header.length, data.length);
        return sendPacketAndDecrypt(payload).thenApply(r ->
            java.util.Arrays.copyOfRange(r, 4, r.length));
    }

    protected CompletableFuture<byte[]> send(int command) { return send(command, new byte[0]); }

    public CompletableFuture<Void> enterLearning() { return send(0x03).thenApply(r -> null); }
    public CompletableFuture<Void> cancelLearning() { return send(0x1E).thenApply(r -> null); }
    public CompletableFuture<byte[]> checkData() { return send(0x04); }
    public CompletableFuture<Void> sendData(byte[] pulses) { return send(0x02, pulses).thenApply(r -> null); }
    public CompletableFuture<Void> sendData(String hex) { return sendData(PulseCodec.hexToPulses(hex)); }

    protected CompletableFuture<double[]> readSensorRaw() {
        return send(0x01).thenApply(r -> {
            int[] v = SENSOR.unpack(r, 0);
            return new double[]{v[0] + v[1] / 10.0};
        });
    }

    public static final class PulseCodec {
        public static byte[] hexToPulses(String hex) {
            if (hex.length() % 2 != 0) throw new IllegalArgumentException("Hex length must be even");
            byte[] p = new byte[hex.length() / 2];
            for (int i = 0; i < hex.length(); i += 2) p[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            return p;
        }
        public static String pulsesToHex(byte[] p) {
            StringBuilder sb = new StringBuilder();
            for (byte b : p) sb.append(String.format("%02x", b & 0xFF));
            return sb.toString();
        }
    }
}
