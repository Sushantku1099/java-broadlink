package com.broadlink.iot.core;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

/**
 * AES-128-CBC cipher for Broadlink protocol encryption/decryption.
 * Encryption uses PKCS5Padding. Decryption uses NoPadding because
 * Broadlink firmware may produce non-standard trailing bytes.
 */
public class BroadlinkCipher {

    public byte[] encrypt(byte[] plaintext, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            return cipher.doFinal(plaintext);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("AES encryption failed", e);
        }
    }

    public byte[] decrypt(byte[] ciphertext, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            return cipher.doFinal(ciphertext);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("AES decryption failed", e);
        }
    }
}
