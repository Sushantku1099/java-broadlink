package com.broadlink.iot.protocol;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Crc16ModbusTest {
    @Test
    void hysenGetTempPayload() {
        byte[] d = {0x01, 0x03, 0, 0, 0, 0x08};
        int crc = Crc16Modbus.compute(d);
        byte[] b = Crc16Modbus.toBytesLE(crc);
        assertEquals(0x44, b[0] & 0xFF);
        assertEquals(0x0C, b[1] & 0xFF);
    }

    @Test
    void hysenGetFullStatusPayload() {
        byte[] d = {0x01, 0x03, 0, 0, 0, 0x16};
        int crc = Crc16Modbus.compute(d);
        assertEquals(0x04C4, crc); // verify CRC
    }
}
