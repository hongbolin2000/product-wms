/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.utils;

import com.hongyou.baron.crypto.AesManager;

/**
 * AES工具类
 *
 * @author Berlin
 */
public class AesUtil {

    /**
     * 生成密匙
     */
    public static void main(String[] args) {
        AesManager.getInstance().generateKeyStore();
    }

    /**
     * 加密
     */
    public static String encrypt(final String data) {
        return AesManager.getInstance().encrypt(data);
    }

    /**
     * 解密（Java端解密）
     */
    public static String decrypt(final String data) {
        return AesManager.getInstance().decrypt(data);
    }

    /**
     * ECB解密（用于前端Crypto库进行加密的数据解密）
     */
    public static String ecbDecrypt(final String key, final String data) {
        return AesManager.getInstance().ecbDecrypt(key, data);
    }
}