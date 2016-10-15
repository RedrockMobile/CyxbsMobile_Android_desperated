package com.mredrock.cyxbs.network.encrypt;

import android.util.Log;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.config.Config;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.SPUtils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class UserInfoEncryption {

    private Encryptor encryptor;
    private boolean isSupportEncrypt = true;

    public UserInfoEncryption() {
        encryptor = new SerialAESEncryptor();
        try {
            encryptor.encrypt("abc".getBytes());
        } catch (Exception e) {
            Log.e("CSET_UIE", "not support", e);
            isSupportEncrypt = false;
        }
        int currentVersion = (int) SPUtils.get(APP.getContext(), Config.SP_KEY_ENCRYPT_VERSION_USER);
        if (currentVersion < Config.USER_INFO_ENCRYPT_VERSION) {
            onUpdate(currentVersion, Config.USER_INFO_ENCRYPT_VERSION);
        }
    }

    public String encrypt(String json) {
        if (json == null) {
            json = "";
        }
        if (!isSupportEncrypt) {
            return json;
        }
        String encrypted = toHex(encryptor.encrypt(json.getBytes()));
        return encrypted;
    }

    public String decrypt(String hexEncrypted) {
        if (hexEncrypted == null) {
            return "";
        }
        if (!isSupportEncrypt) {
            return hexEncrypted;
        }
        try {
            return new String(encryptor.decrypt(toByte(hexEncrypted)));
        } catch (DecryptFailureException e) {
            LogUtils.LOGE("CSET_UIE", "decrypt failure", e);
            return "";
        }
    }

    /**
     * if you update the encrypt method in the future, please update here for compatibility
     *
     * @param i old version
     * @param ii new version
     */
    public void onUpdate(int i, int ii) {
        if (i == 0 && ii == 1) {
            String unEncryptedJson = (String) SPUtils.get(APP.getContext(), Const.SP_KEY_USER, "");
            SPUtils.set(APP.getContext(), Const.SP_KEY_USER, encrypt(unEncryptedJson));
        }

        SPUtils.set(APP.getContext(), Config.SP_KEY_ENCRYPT_VERSION_USER, ii);
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    private static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


}
