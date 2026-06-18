package com.broadlink.iot.protocol;

/**
 * 16-bit additive checksum with seed 0xBEAF.
 * Used for command packet payload checksums, packet checksums,
 * discovery request checksums, and JSON device header checksums.
 */
public final class Checksum {

    /** The universal seed for all Broadlink additive checksums. */
    public static final int SEED = 0xBEAF;

    private Checksum() {}

    public static int compute(byte[] data) {
        int sum = SEED;
        for (byte b : data) {
            sum = (sum + (b & 0xFF)) & 0xFFFF;
        }
        return sum;
    }

    public static int compute(byte[] data, int offset, int length) {
        int sum = SEED;
        for (int i = 0; i < length; i++) {
            sum = (sum + (data[offset + i] & 0xFF)) & 0xFFFF;
        }
        return sum;
    }

    public static void writeChecksumLE(byte[] dest, int offset, int checksum) {
        dest[offset] = (byte) (checksum & 0xFF);
        dest[offset + 1] = (byte) ((checksum >> 8) & 0xFF);
    }
}
