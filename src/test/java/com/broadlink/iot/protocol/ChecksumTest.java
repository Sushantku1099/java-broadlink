package com.broadlink.iot.protocol;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChecksumTest {
    @Test
    void seedIsBEAF() { assertEquals(0xBEAF, Checksum.SEED); }

    @Test
    void emptyReturnsSeed() { assertEquals(0xBEAF, Checksum.compute(new byte[0])); }

    @Test
    void singleByte() { assertEquals(0xBEB0, Checksum.compute(new byte[]{0x01})); }

    @Test
    void wrapsAt16Bits() { assertEquals(0xBFAE, Checksum.compute(new byte[]{(byte)0xFF})); }

    @Test
    void rangeMatchesFull() {
        byte[] d = {1,2,3,4,5};
        assertEquals(Checksum.compute(d), Checksum.compute(d, 0, d.length));
    }
}
