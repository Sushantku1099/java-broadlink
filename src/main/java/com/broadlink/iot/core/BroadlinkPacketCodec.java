package com.broadlink.iot.core;

import com.broadlink.iot.protocol.Checksum;
import java.util.Arrays;

/**
 * Builds and parses the universal 56-byte Broadlink command packet header.
 * HEADER (56 bytes = 0x38):
 *   0x00-0x07   Preamble 5A A5 AA 55 5A A5 AA 55
 *   0x20-0x21   Packet checksum (uint16 LE) - computed LAST
 *   0x22-0x23   Error code (uint16 LE) - zero in request
 *   0x24-0x25   Device type (uint16 LE)
 *   0x26        Command code
 *   0x28-0x29   Sequence counter (uint16 LE)
 *   0x2A-0x2F   MAC address REVERSED
 *   0x30-0x33   Device ID
 *   0x34-0x35   Payload checksum (uint16 LE) - BEFORE encryption
 */
public class BroadlinkPacketCodec {

    private static final int HEADER_SIZE = 0x38;
    private static final byte[] PREAMBLE = {
        (byte) 0x5A, (byte) 0xA5, (byte) 0xAA, (byte) 0x55,
        (byte) 0x5A, (byte) 0xA5, (byte) 0xAA, (byte) 0x55
    };

    private final BroadlinkCipher cipher;

    public BroadlinkPacketCodec(BroadlinkCipher cipher) {
        this.cipher = cipher;
    }

    public byte[] buildPacket(byte[] plaintextPayload, int command,
                               DeviceIdentity identity, DeviceMetadata metadata) {
        int count = identity.nextCount();
        byte[] paddedPayload = padPkcs7(plaintextPayload);
        int payloadChecksum = Checksum.compute(paddedPayload);

        byte[] header = new byte[HEADER_SIZE];
        System.arraycopy(PREAMBLE, 0, header, 0, 8);

        int dt = metadata.getDeviceType();
        header[0x24] = (byte) (dt & 0xFF);
        header[0x25] = (byte) ((dt >> 8) & 0xFF);
        header[0x26] = (byte) command;
        header[0x28] = (byte) (count & 0xFF);
        header[0x29] = (byte) ((count >> 8) & 0xFF);

        byte[] mac = metadata.getMac();
        for (int i = 0; i < 6; i++) header[0x2A + i] = mac[5 - i];

        System.arraycopy(identity.getId(), 0, header, 0x30, 4);
        header[0x34] = (byte) (payloadChecksum & 0xFF);
        header[0x35] = (byte) ((payloadChecksum >> 8) & 0xFF);

        byte[] encryptedPayload = cipher.encrypt(paddedPayload, identity.getKey(), identity.getIv());

        byte[] fullPacket = new byte[HEADER_SIZE + encryptedPayload.length];
        System.arraycopy(header, 0, fullPacket, 0, HEADER_SIZE);
        System.arraycopy(encryptedPayload, 0, fullPacket, HEADER_SIZE, encryptedPayload.length);

        int packetChecksum = Checksum.compute(fullPacket);
        fullPacket[0x20] = (byte) (packetChecksum & 0xFF);
        fullPacket[0x21] = (byte) ((packetChecksum >> 8) & 0xFF);

        return fullPacket;
    }

    public int getErrorCode(byte[] response) {
        if (response.length < 0x24) return -1;
        return (response[0x22] & 0xFF) | ((response[0x23] & 0xFF) << 8);
    }

    public byte[] decryptResponsePayload(byte[] response, DeviceIdentity identity) {
        if (response.length <= HEADER_SIZE)
            throw new IllegalArgumentException("Response too short: " + response.length);
        byte[] ciphertext = Arrays.copyOfRange(response, HEADER_SIZE, response.length);
        return cipher.decrypt(ciphertext, identity.getKey(), identity.getIv());
    }

    static byte[] padPkcs7(byte[] data) {
        int blockSize = 16;
        int padLen = blockSize - (data.length % blockSize);
        if (padLen == 0) padLen = blockSize;
        byte[] padded = new byte[data.length + padLen];
        System.arraycopy(data, 0, padded, 0, data.length);
        Arrays.fill(padded, data.length, padded.length, (byte) padLen);
        return padded;
    }
}
