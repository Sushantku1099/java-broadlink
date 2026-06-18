package com.broadlink.iot.device;

import com.broadlink.iot.device.remote.RmMiniDevice;
import com.broadlink.iot.protocol.BinaryStruct;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RmCommandPayloadTest {

    private static final BinaryStruct.StructCodec I  = BinaryStruct.compile("<I");
    private static final BinaryStruct.StructCodec HI = BinaryStruct.compile("<HI");

    // === GEN2 Tests ===
    @Test void enterLearningGen2()  { assertArrayEquals(new byte[]{3,0,0,0},  I.pack(0x03)); }
    @Test void cancelLearningGen2() { assertArrayEquals(new byte[]{30,0,0,0}, I.pack(0x1E)); }
    @Test void checkDataGen2()     { assertArrayEquals(new byte[]{4,0,0,0},  I.pack(0x04)); }
    @Test void sweepFreqGen2()     { assertArrayEquals(new byte[]{25,0,0,0}, I.pack(0x19)); }
    @Test void checkFreqGen2()     { assertArrayEquals(new byte[]{26,0,0,0}, I.pack(0x1A)); }
    @Test void findRfPacketGen2()  { assertArrayEquals(new byte[]{27,0,0,0}, I.pack(0x1B)); }
    @Test void readSensorGen2()    { assertArrayEquals(new byte[]{1,0,0,0},  I.pack(0x01)); }

    @Test void sendDataGen2() {
        byte[] pulses = {74,7,(byte)212,7,17,(byte)161,(byte)184,70};
        byte[] hdr = I.pack(0x02);
        byte[] full = new byte[hdr.length + pulses.length];
        System.arraycopy(hdr, 0, full, 0, hdr.length);
        System.arraycopy(pulses, 0, full, hdr.length, pulses.length);
        assertEquals(12, full.length);
        assertEquals(2, full[0]);
    }

    // === GEN3 Tests ===
    @Test void enterLearningGen3()  { assertArrayEquals(new byte[]{4,0,3,0,0,0},  HI.pack(4,0x03)); }
    @Test void cancelLearningGen3() { assertArrayEquals(new byte[]{4,0,30,0,0,0}, HI.pack(4,0x1E)); }
    @Test void checkDataGen3()     { assertArrayEquals(new byte[]{4,0,4,0,0,0},  HI.pack(4,0x04)); }
    @Test void sweepFreqGen3()     { assertArrayEquals(new byte[]{4,0,25,0,0,0}, HI.pack(4,0x19)); }
    @Test void checkFreqGen3()     { assertArrayEquals(new byte[]{4,0,26,0,0,0}, HI.pack(4,0x1A)); }
    @Test void findRfGen3()        { assertArrayEquals(new byte[]{4,0,27,0,0,0}, HI.pack(4,0x1B)); }
    @Test void checkSensorsGen3()  { assertArrayEquals(new byte[]{4,0,36,0,0,0}, HI.pack(4,0x24)); }

    @Test void sendDataGen3() {
        byte[] h = HI.pack(36, 0x02);
        assertEquals(6, h.length);
        assertEquals(36, h[0] & 0xFF);
        assertEquals(2, h[2]);
    }

    @Test void hexToPulsesAndBack() {
        String hex = "4a07d40711a1b84676cddae3cc3dee5c1506062e630bcccccb2d65254a46b34c";
        byte[] pulses = RmMiniDevice.PulseCodec.hexToPulses(hex);
        assertEquals(32, pulses.length);
        assertEquals(0x4A, pulses[0] & 0xFF);
        assertEquals(hex, RmMiniDevice.PulseCodec.pulsesToHex(pulses));
    }
}
