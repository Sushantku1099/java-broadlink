package com.broadlink.iot.security;

/**
 * Cryptographic constants for the Broadlink protocol.
 * The default key is the well-known universal key used for the authentication
 * handshake. It is replaced by a device-specific session key after auth().
 * The IV is static and never rotates - this is a known protocol limitation
 * preserved for firmware compatibility.
 */
public final class BroadlinkCrypto {

    private BroadlinkCrypto() {}

    /** Universal default AES-128 key (16 bytes). Used only for auth(). */
    public static final byte[] DEFAULT_KEY = {
        (byte) 0x09, (byte) 0x76, (byte) 0x28, (byte) 0x34,
        (byte) 0x3F, (byte) 0xE9, (byte) 0x9E, (byte) 0x23,
        (byte) 0x76, (byte) 0x5C, (byte) 0x15, (byte) 0x13,
        (byte) 0xAC, (byte) 0xCF, (byte) 0x8B, (byte) 0x02
    };

    /** Static AES-128 initialization vector (16 bytes). Never rotates. */
    public static final byte[] STATIC_IV = {
        (byte) 0x56, (byte) 0x2E, (byte) 0x17, (byte) 0x99,
        (byte) 0x6D, (byte) 0x09, (byte) 0x3D, (byte) 0x28,
        (byte) 0xDD, (byte) 0xB3, (byte) 0xBA, (byte) 0x69,
        (byte) 0x5A, (byte) 0x2E, (byte) 0x6F, (byte) 0x58
    };

    /** Security modes for Wi-Fi provisioning. */
    public enum SecurityMode {
        NONE(0), WEP(1), WPA1(2), WPA2(3), WPA1_2(4);

        private final int code;
        SecurityMode(int code) { this.code = code; }
        public int getCode() { return code; }
        public static SecurityMode fromCode(int code) {
            return values()[code];
        }
    }
}
