package com.broadlink.iot.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthPayloadTest {
    @Test
    void payloadIs80Bytes() {
        assertEquals(0x50, BroadlinkDevice.buildAuthPayload().length);
    }

    @Test
    void matchesNodeBroadlinkTestVector() {
        byte[] expected = {
    0,0,0,0,49,49,49,49,49,49,49,49,49,49,49,49,
    49,49,49,49,0,0,0,0,0,0,0,0,0,0,1,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,
    84,101,115,116,32,49,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
};
        assertArrayEquals(expected, BroadlinkDevice.buildAuthPayload());
    }

    @Test
    void offset0x04to0x13is0x31() {
        byte[] p = BroadlinkDevice.buildAuthPayload();
        for (int i = 0x04; i < 0x14; i++)
            assertEquals(0x31, p[i] & 0xFF, "at offset 0x" + Integer.toHexString(i));
    }

    @Test
    void flagsAt0x1Eand0x2D() {
        byte[] p = BroadlinkDevice.buildAuthPayload();
        assertEquals(0x01, p[0x1E] & 0xFF);
        assertEquals(0x01, p[0x2D] & 0xFF);
    }

    @Test
    void test1At0x30() {
        byte[] p = BroadlinkDevice.buildAuthPayload();
        assertEquals('T', p[0x30]); assertEquals('e', p[0x31]);
        assertEquals('s', p[0x32]); assertEquals('t', p[0x33]);
        assertEquals(' ', p[0x34]); assertEquals('1', p[0x35]);
    }
}
