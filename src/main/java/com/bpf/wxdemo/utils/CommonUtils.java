package com.bpf.wxdemo.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * 常用工具类, UUID MD5等
 * @author baipengfei
 */
public class CommonUtils {

    /**
     * 生成UUID
     * @return 32位随机字符串
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }

    /**
     * 生成MD5
     * @param data
     * @return
     */
    public static String getMD5(String data)  {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;

    }

}
