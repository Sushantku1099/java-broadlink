package com.broadlink.iot.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

/**
 * Root base class for ALL Broadlink devices.
 * Provides authentication, UDP transport, AES encryption, and packet framing.
 */
public class BroadlinkDevice implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(BroadlinkDevice.class);

    protected static final int CMD_DATA = 0x6A;
    protected static final int CMD_AUTH = 0x65;

    protected final DeviceMetadata metadata;
    protected final DeviceIdentity identity;
    protected final BroadlinkTransport transport;
    protected final BroadlinkPacketCodec codec;

    protected String type = "Unknown";
    private boolean authenticated = false;

    public BroadlinkDevice(DeviceMetadata metadata) {
        this.metadata = metadata;
        this.identity = new DeviceIdentity();
        this.transport = new BroadlinkTransport(metadata.getHost());
        this.codec = new BroadlinkPacketCodec(new BroadlinkCipher());
    }

    BroadlinkDevice(DeviceMetadata metadata, DeviceIdentity identity,
                    BroadlinkTransport transport, BroadlinkPacketCodec codec) {
        this.metadata = metadata;
        this.identity = identity;
        this.transport = transport;
        this.codec = codec;
    }

    public CompletableFuture<BroadlinkDevice> authenticate() {
        byte[] authPayload = buildAuthPayload();
        log.debug("{} authenticating...", metadata.getName());
        return sendPacket(authPayload, CMD_AUTH).thenApply(response -> {
            byte[] decrypted = codec.decryptResponsePayload(response, identity);
            byte[] deviceId = new byte[4];
            System.arraycopy(decrypted, 0, deviceId, 0, 4);
            byte[] sessionKey = new byte[16];
            System.arraycopy(decrypted, 4, sessionKey, 0, 16);
            identity.updateFromAuth(deviceId, sessionKey);
            authenticated = true;
            log.debug("{} authenticated", metadata.getName());
            return this;
        });
    }

    protected CompletableFuture<byte[]> sendPacket(byte[] payload, int command) {
        byte[] packet = codec.buildPacket(payload, command, identity, metadata);
        return transport.sendReceive(packet).thenApply(response -> {
            int error = codec.getErrorCode(response);
            if (error != 0) throw new DeviceErrorException("Device error: " + error, error);
            return response;
        });
    }

    protected CompletableFuture<byte[]> sendPacket(byte[] payload) {
        return sendPacket(payload, CMD_DATA);
    }

    protected CompletableFuture<byte[]> sendPacketAndDecrypt(byte[] payload) {
        return sendPacket(payload).thenApply(response ->
            codec.decryptResponsePayload(response, identity));
    }

    static byte[] buildAuthPayload() {
        byte[] payload = new byte[0x50];
        for (int i = 0x04; i < 0x14; i++) payload[i] = 0x31;
        payload[0x1E] = 0x01;
        payload[0x2D] = 0x01;
        byte[] test1 = {'T', 'e', 's', 't', ' ', '1'};
        System.arraycopy(test1, 0, payload, 0x30, 6);
        return payload;
    }

    public boolean isAuthenticated() { return authenticated; }
    public String getType() { return type; }
    public DeviceMetadata getMetadata() { return metadata; }

    @Override
    public void close() { transport.close(); }

    public static class DeviceErrorException extends RuntimeException {
        private final int errorCode;
        public DeviceErrorException(String msg, int errorCode) { super(msg); this.errorCode = errorCode; }
        public int getErrorCode() { return errorCode; }
    }

    @Override
    public String toString() { return type + "[" + metadata.getName() + "]"; }
}
