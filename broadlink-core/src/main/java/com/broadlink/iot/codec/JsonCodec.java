package com.broadlink.iot.codec;

import com.broadlink.iot.protocol.BinaryStruct;
import com.broadlink.iot.protocol.Checksum;
import com.broadlink.iot.spi.JsonSerializer;
import java.nio.charset.StandardCharsets;

/**
 * JSON-over-binary codec for GEN4 (12-byte) and GEN4B (14-byte) headers.
 * Uses pluggable JsonSerializer — no hard dependency on Jackson or Gson.
 */
public class JsonCodec {

    private static final JsonSerializer JSON = JsonSerializer.getInstance();
    private static final BinaryStruct.StructCodec G4_HDR  = BinaryStruct.compile("<HHHBBI");
    private static final BinaryStruct.StructCodec G4B_HDR = BinaryStruct.compile("<HHHHBBI");
    private static final BinaryStruct.StructCodec LEN_CODEC = BinaryStruct.compile("<I");

    public static byte[] encodeGen4(int flag, Object state) {
        byte[] jb = JSON.toJson(state);
        byte[] h = new byte[12];
        G4_HDR.packInto(h, 0, 0xA5A5, 0x5A5A, 0, flag, 0x0B, jb.length);
        byte[] p = new byte[12 + jb.length];
        System.arraycopy(h, 0, p, 0, 12);
        System.arraycopy(jb, 0, p, 12, jb.length);
        writeCS(p, 4, Checksum.compute(p));
        return p;
    }

    public static <T> T decodeGen4(byte[] data, Class<T> type) {
        int jl = LEN_CODEC.unpack(data, 8)[0];
        byte[] jb = new byte[jl];
        System.arraycopy(data, 12, jb, 0, jl);
        return JSON.fromJson(jb, type);
    }

    public static byte[] encodeGen4B(int flag, Object state) {
        byte[] jb = JSON.toJson(state);
        int tl = 12 + jb.length;
        byte[] h = new byte[14];
        G4B_HDR.packInto(h, 0, tl, 0xA5A5, 0x5A5A, 0, flag, 0x0B, jb.length);
        byte[] p = new byte[14 + jb.length];
        System.arraycopy(h, 0, p, 0, 14);
        System.arraycopy(jb, 0, p, 14, jb.length);
        writeCS(p, 6, Checksum.compute(p, 2, p.length - 2));
        return p;
    }

    public static <T> T decodeGen4B(byte[] data, Class<T> type) {
        int jl = LEN_CODEC.unpack(data, 10)[0];
        byte[] jb = new byte[jl];
        System.arraycopy(data, 14, jb, 0, jl);
        return JSON.fromJson(jb, type);
    }

    private static void writeCS(byte[] d, int o, int cs) {
        d[o] = (byte)(cs & 0xFF);
        d[o+1] = (byte)((cs >> 8) & 0xFF);
    }
}
