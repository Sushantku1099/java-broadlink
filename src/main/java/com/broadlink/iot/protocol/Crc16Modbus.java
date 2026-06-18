package com.broadlink.iot.protocol;

/**
 * CRC-16/MODBUS implementation for Hysen thermostat protocol.
 * Polynomial: 0xA001, Initial value: 0xFFFF, No XOR-out.
 */
public final class Crc16Modbus {

    private Crc16Modbus() {}

    private static final int[] TABLE = new int[256];

    static {
        for (int i = 0; i < 256; i++) {
            int crc = i;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc = crc >> 1;
                }
            }
            TABLE[i] = crc;
        }
    }

    public static int compute(byte[] data) {
        return compute(data, 0, data.length);
    }

    public static int compute(byte[] data, int offset, int length) {
        int crc = 0xFFFF;
        for (int i = 0; i < length; i++) {
            int b = data[offset + i] & 0xFF;
            crc = (crc >> 8) ^ TABLE[(crc ^ b) & 0xFF];
        }
        return crc;
    }

    public static byte[] toBytesLE(int crc) {
        return new byte[] {
            (byte) (crc & 0xFF),
            (byte) ((crc >> 8) & 0xFF)
        };
    }
}
