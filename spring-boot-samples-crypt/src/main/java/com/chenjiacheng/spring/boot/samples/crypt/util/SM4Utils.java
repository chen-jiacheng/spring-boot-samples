package com.chenjiacheng.spring.boot.samples.crypt.util;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;

public class SM4Utils {

    // SM4密钥，长度必须为16字节
    private static final byte[] keys = new byte[]{64, 74, 104, 120, 50, 48, 50, 52, 35, 36, 37, 94, 38, 42, 33, 43};
    private static final SM4 sm4 = SmUtil.sm4(keys);
    public static final String SM4_PREFIX = "@SM4@-";

    /**
     * SM4加密
     *
     * @param data 明文
     * @return 密文
     */
    public static String encrypt(String data) {
        return SM4_PREFIX + sm4.encryptHex(data);
    }

    /**
     * SM4解密
     *
     * @param data 密文
     * @return 明文
     */
    public static String decrypt(String data) {
        return sm4.decryptStr(data.replace(SM4_PREFIX, ""));
    }

    /**
     * SM4加密
     *
     * @param data 明文
     * @return 密文
     */
    public static String encrypt(String key, String data) {
        if (key.length() != 16) {
            key = SM4Utils.padding(key);
        }
        return SmUtil.sm4(key.getBytes(StandardCharsets.UTF_8)).encryptHex(data);
    }

    /**
     * SM4解密
     *
     * @param data 密文
     * @return 明文
     */
    public static String decrypt(String key, String data) {
        if (key.length() != 16) {
            key = SM4Utils.padding(key);
        }
        return SmUtil.sm4(key.getBytes(StandardCharsets.UTF_8)).decryptStr(data);
    }

    private static String padding(String key) {
        if (key.length() < 16) {
            int diff = 16 - key.length();
            StringBuilder keyBuilder = new StringBuilder(key);
            for (int i = 0; i < diff; i++) {
                keyBuilder.append("\0");
            }
            key = keyBuilder.toString();
        } else {
            key = key.substring(0, 16);
        }
        return key;
    }

    public static void main(String[] args) {
        String cipher = SM4Utils.encrypt("mkt_ledger_key", "ledger@2024#PRO");
        System.out.println("cipher = " + cipher);

        String decrypt = SM4Utils.decrypt("mkt_ledger_key", cipher);
        System.out.println("decrypt = " + decrypt);


    }

}