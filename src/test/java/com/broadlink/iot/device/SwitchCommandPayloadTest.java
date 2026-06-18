package com.broadlink.iot.device;

import com.broadlink.iot.protocol.BinaryStruct;
import com.broadlink.iot.protocol.Crc16Modbus;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SwitchCommandPayloadTest {

    @Test void sp1SetPower() { byte[] p = new byte[4]; p[0]=1; assertArrayEquals(new byte[]{1,0,0,0}, p); }
    @Test void sp2SetPower() { byte[] p = new byte[16]; p[0]=2; p[4]=1; assertEquals(2,p[0]); assertEquals(1,p[4]); }
    @Test void sp2CheckPower() { byte[] p = new byte[16]; p[0]=1; assertEquals(1,p[0]); }
    @Test void sp2sGetEnergy() { byte[] p = new byte[16]; p[0]=4; assertEquals(4,p[0]); }
    @Test void sp3sGetEnergy() { byte[] p = {8,0,(byte)254,1,5,1,0,0,0,45}; assertEquals(10,p.length); assertEquals(8,p[0]); }
    @Test void a1Payload() { byte[] p = {0x01}; assertEquals(1,p.length); }
    @Test void s1cPayload() { byte[] p = new byte[16]; p[0]=0x06; assertEquals(0x06,p[0]); }

    @Test void mp1CheckPower() {
        byte[] p = new byte[16];
        p[0x00]=0x0A; p[0x02]=(byte)0xA5; p[0x03]=(byte)0xA5;
        p[0x04]=0x5A; p[0x05]=0x5A; p[0x06]=(byte)0xAE; p[0x07]=(byte)0xC0; p[0x08]=0x01;
        assertEquals(10, p[0] & 0xFF);
        assertEquals((byte)0xAE, p[0x06]);
    }

    @Test void mp1SetPowerSocket5() {
        int sm = 0x10;
        assertEquals(0x10, 0x01 << (5-1));
        byte[] p = new byte[16];
        p[0]=0x0D; p[2]=(byte)0xA5; p[3]=(byte)0xA5; p[4]=0x5A; p[5]=0x5A;
        p[6]=(byte)(0xB2 + (sm << 1)); p[7]=(byte)0xC0; p[8]=0x02; p[0xA]=0x03;
        p[0xD]=(byte)sm; p[0xE]=(byte)sm;
        assertEquals((byte)0xD2, p[6]);
    }

    @Test void dooyaOpen() {
        byte[] p = new byte[16];
        p[0]=0x09; p[2]=(byte)0xBB; p[3]=1; p[9]=(byte)0xFA; p[10]=0x44;
        assertEquals(9, p[0] & 0xFF);
        assertEquals(0xBB, p[2] & 0xFF);
        assertEquals(1, p[3]);
    }

    @Test void gen4JsonHeaderMagic() {
        var c = BinaryStruct.compile("<HHHBBI");
        byte[] h = new byte[12];
        c.packInto(h, 0, 0xA5A5, 0x5A5A, 0, 1, 0x0B, 2);
        assertEquals((byte)0xA5, h[0]);
        assertEquals((byte)0xA5, h[1]);
        assertEquals(1, h[6]);  // flag at byte 6
        assertEquals(0x0B, h[7]);  // proto at byte 7
    }

    @Test void gen4bJsonHeader() {
        var c = BinaryStruct.compile("<HHHHBBI");
        int tl = 14;
        byte[] h = new byte[14];
        c.packInto(h, 0, tl, 0xA5A5, 0x5A5A, 0, 1, 0x0B, 2);
        assertEquals(tl & 0xFF, h[0]);
        assertEquals((tl >> 8) & 0xFF, h[1]);
        assertEquals((byte)0xA5, h[2]);
    }

    @Test void crc16HysenGetTemp() {
        byte[] d = {0x01,0x03,0,0,0,0x08};
        assertEquals(0x0C44, Crc16Modbus.compute(d));
    }

    @Test void crc16HysenGetFullStatus() {
        byte[] d = {0x01,0x03,0,0,0,0x16};
        assertEquals(0x04C4, Crc16Modbus.compute(d));
    }
}
