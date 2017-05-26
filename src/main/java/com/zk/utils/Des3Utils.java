package com.zk.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/**
 * Created by werewolf on 2017/2/19.
 */
public class Des3Utils {

    private final static String encoding = "utf-8";

    /**
     * @param _key  密钥
     * @param _data 明文
     * @return Base64编码的密文
     */
    public static byte[] encode(String _key, String _data) {
        try {
            byte[] key = _key.getBytes(encoding);
            byte[] data = _data.getBytes(encoding);
            Key desKey;
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            return cipher.doFinal(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * @param _key 密钥
     * @param data Base64编码的密文
     * @return 明文
     */
    public static byte[] decode(String _key, byte[] data) {
        try {
            byte[] key = _key.getBytes(encoding);
            Key desKey;
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            return cipher.doFinal(data);
        } catch (Exception ex) {
            return null;
        }
    }


    public static void main(String[] args) throws UnsupportedEncodingException {





        String key = DigestUtils.MD5("123456");
        String str = "{111,222}";
        System.out.println(DateUtils.currentTimeSecs());
        System.out.println("-----加密------"+key);
        String str1 = DigestUtils.encodeBase64(Des3Utils.encode(key, str));
        System.out.println("加密参数:" + str1);
        System.out.println("-----加密--end----");
        System.out.println(DateUtils.currentTimeSecs());

        String out = new String(Des3Utils.decode(key, DigestUtils.decodeBase64(str1.getBytes("utf-8"))), "utf-8");
        System.out.println("解密参数:" + out);
        System.out.println(DateUtils.currentTimeSecs());

    }
}
