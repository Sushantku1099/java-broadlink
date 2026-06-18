package com.broadlink.iot.core;

import com.broadlink.iot.security.BroadlinkCrypto;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Cryptographic identity for a Broadlink device session.
 */
public class DeviceIdentity {

    private byte[] id;
    private byte[] key;
    private final byte[] iv;
    private int count;

    public DeviceIdentity() {
        this.id = new byte[]{0, 0, 0, 0};
        this.key = BroadlinkCrypto.DEFAULT_KEY.clone();
        this.iv = BroadlinkCrypto.STATIC_IV.clone();
        this.count = ThreadLocalRandom.current().nextInt(0x10000);
    }

    public synchronized int nextCount() {
        count = (count + 1) & 0xFFFF;
        return count;
    }

    public synchronized void updateFromAuth(byte[] newId, byte[] newKey) {
        if (newKey.length != 16) {
            throw new IllegalArgumentException("Session key must be 16 bytes, got: " + newKey.length);
        }
        this.id = newId.clone();
        this.key = newKey.clone();
    }

    public byte[] getId() { return id.clone(); }
    public byte[] getKey() { return key.clone(); }
    public byte[] getIv() { return iv.clone(); }
    public int getCount() { return count; }
}
