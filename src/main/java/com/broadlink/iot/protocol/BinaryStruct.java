package com.broadlink.iot.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Python struct-style binary packer/unpacker using ByteBuffer.
 * Format: < for little-endian. I=uint32, H=uint16, B=uint8, b=int8.
 */
public final class BinaryStruct {

    private BinaryStruct() {}

    public static StructCodec compile(String format) {
        boolean le = true;
        int pos = 0;
        if (pos < format.length() && format.charAt(pos) == '<') { le = true; pos++; }
        else if (pos < format.length() && format.charAt(pos) == '>') { le = false; pos++; }
        final boolean littleEndian = le;
        final String fields = format.substring(pos);

        return new StructCodec() {
            public int size() { int s = 0; for (int i = 0; i < fields.length(); i++) s += fieldSize(fields.charAt(i)); return s; }
            public int[] unpack(byte[] data, int offset) {
                ByteBuffer buf = ByteBuffer.wrap(data, offset, data.length - offset);
                if (littleEndian) buf.order(ByteOrder.LITTLE_ENDIAN);
                int[] result = new int[fields.length()];
                for (int i = 0; i < fields.length(); i++) result[i] = readField(buf, fields.charAt(i));
                return result;
            }
            public int[] unpack(byte[] data) { return unpack(data, 0); }
            public byte[] pack(int... values) {
                ByteBuffer buf = ByteBuffer.allocate(size());
                if (littleEndian) buf.order(ByteOrder.LITTLE_ENDIAN);
                for (int i = 0; i < values.length; i++) writeField(buf, fields.charAt(i), values[i]);
                return buf.array();
            }
            public void packInto(byte[] dest, int offset, int... values) {
                ByteBuffer buf = ByteBuffer.wrap(dest, offset, size());
                if (littleEndian) buf.order(ByteOrder.LITTLE_ENDIAN);
                for (int i = 0; i < values.length; i++) writeField(buf, fields.charAt(i), values[i]);
            }
        };
    }

    private static int fieldSize(char c) {
        if (c == 'I') return 4; if (c == 'H') return 2;
        if (c == 'B' || c == 'b') return 1;
        throw new IllegalArgumentException("Unknown struct field: " + c);
    }

    private static int readField(ByteBuffer buf, char c) {
        if (c == 'I') return buf.getInt();
        if (c == 'H') return buf.getShort() & 0xFFFF;
        if (c == 'B') return buf.get() & 0xFF;
        if (c == 'b') return buf.get();
        throw new IllegalArgumentException("Unknown struct field: " + c);
    }

    private static void writeField(ByteBuffer buf, char c, int value) {
        if (c == 'I') { buf.putInt(value); return; }
        if (c == 'H') { buf.putShort((short) value); return; }
        if (c == 'B' || c == 'b') { buf.put((byte) value); return; }
        throw new IllegalArgumentException("Unknown struct field: " + c);
    }

    public interface StructCodec {
        int size();
        int[] unpack(byte[] data, int offset);
        int[] unpack(byte[] data);
        byte[] pack(int... values);
        void packInto(byte[] dest, int offset, int... values);
    }
}
