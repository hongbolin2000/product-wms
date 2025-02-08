/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import CryptoJS from 'crypto-js'
/********************************************************************************
 * Crypto数据加密帮助类（提供给功能应用调用）
 *
 * @author Berlin
 *******************************************************************************/
export namespace CryptoHelper {

  /**
   * 生成256位密匙
   */
  export function generate256BitKey() {
    const buffer = CryptoJS.lib.WordArray.random(256/16);
    return buffer.toString(CryptoJS.enc.Base64);
  }

  /**
   * AES加密
   */
  export function aesEncrypt(key: string, content: string) {
    const encrypted = CryptoJS.AES.encrypt(content, CryptoJS.enc.Base64.parse(key), {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7,
    });
    return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
  }
}
