package com.broadlink.iot.protocol;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinaryStructTest {

    @Test
    void packUnpack_I() {
        var c = BinaryStruct.compile("<I");
        assertArrayEquals(new byte[]{3,0,0,0}, c.pack(0x03));
        assertEquals(0x03, c.unpack(new byte[]{3,0,0,0})[0]);
    }

    @Test
    void packUnpack_H() {
        var c = BinaryStruct.compile("<H");
        assertArrayEquals(new byte[]{36,0}, c.pack(36));
        assertEquals(36, c.unpack(new byte[]{36,0})[0]);
    }

    @Test
    void packUnpack_HI() {
        var c = BinaryStruct.compile("<HI");
        assertArrayEquals(new byte[]{36,0,2,0,0,0}, c.pack(36, 0x02));
        int[] v = c.unpack(new byte[]{36,0,2,0,0,0});
        assertEquals(36, v[0]);
        assertEquals(2, v[1]);
    }

    @Test
    void packUnpack_bb() {
        var c = BinaryStruct.compile("<bb");
        int[] v = c.unpack(c.pack(23, 5));
        assertEquals(23, v[0]);
        assertEquals(5, v[1]);
    }

    @Test
    void packUnpack_HHHBBI() {
        var c = BinaryStruct.compile("<HHHBBI");
        byte[] p = c.pack(0xA5A5, 0x5A5A, 0, 1, 0x0B, 9);
        assertArrayEquals(new byte[]{
            (byte)0xA5,(byte)0xA5,(byte)0x5A,(byte)0x5A,
            0,0,1,0x0B,9,0,0,0
        }, p);
        assertEquals(12, c.size());
    }

    @Test
    void sizes() {
        assertEquals(4, BinaryStruct.compile("<I").size());
        assertEquals(2, BinaryStruct.compile("<H").size());
        assertEquals(6, BinaryStruct.compile("<HI").size());
        assertEquals(12, BinaryStruct.compile("<HHHBBI").size());
        assertEquals(14, BinaryStruct.compile("<HHHHBBI").size());
    }
}
