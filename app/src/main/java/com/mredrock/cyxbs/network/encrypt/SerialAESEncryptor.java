package com.mredrock.cyxbs.network.encrypt;

import android.os.Build;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class SerialAESEncryptor implements Encryptor {

    @Override
    public byte[] encrypt(byte[] orig) {
        try {
            byte[] rawKey = generateRawKey();
            SecretKeySpec sKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            return cipher.doFinal(orig);
        } catch (Exception e) {
            throw new RuntimeException("encrypt failure", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] encrypted) throws DecryptFailureException {
        try {
            byte[] rawKey = generateRawKey();
            SecretKeySpec sKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new DecryptFailureException(e);
        }
    }

    private static byte[] generateRawKey() throws Exception {
        String key = Build.SERIAL;
//        String key = null;
        if (key == null) {
            key = "huQVa6y^Rd0Z^e#K";
        }
        return getRawKey(key.getBytes());
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

}
