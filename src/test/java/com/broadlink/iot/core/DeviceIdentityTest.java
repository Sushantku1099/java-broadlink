package com.broadlink.iot.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DeviceIdentityTest {
    @Test
    void defaultKeyIsBroadlinkUniversal() {
        DeviceIdentity id = new DeviceIdentity();
        byte[] key = id.getKey();
        assertEquals(0x09, key[0] & 0xFF);
        assertEquals(0x02, key[15] & 0xFF);
        assertEquals(16, key.length);
    }

    @Test
    void defaultIdIsZero() {
        byte[] id = new DeviceIdentity().getId();
        for (byte b : id) assertEquals(0, b);
        assertEquals(4, id.length);
    }

    @Test
    void countIncrements() {
        DeviceIdentity id = new DeviceIdentity();
        int a = id.nextCount();
        int b = id.nextCount();
        assertEquals(((a + 1) & 0xFFFF), b);
    }

    @Test
    void updateFromAuthReplacesKey() {
        DeviceIdentity id = new DeviceIdentity();
        byte[] newId = {0x01, 0x02, 0x03, 0x04};
        byte[] newKey = {0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x10,0x11,
                         0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19};
        id.updateFromAuth(newId, newKey);
        assertArrayEquals(newId, id.getId());
        assertArrayEquals(newKey, id.getKey());
    }
}
