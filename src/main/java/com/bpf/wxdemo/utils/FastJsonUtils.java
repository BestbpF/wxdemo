package com.bpf.wxdemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * fastjson工具类
 * @author baipengfei
 */
public class FastJsonUtils {

    /**
     * object转化为json字符串
     * @param object Object
     * @return String
     */
    public static String convertObjectToJSON(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     *  json字符串转换为Object数组
     * @param s Json字符串
     * @return Object[]
     */
    public static Object[] toArray(String s) {
        return toArray(s, null);
    }

    /**
     * json字符串转换为制定类型数组数组
     * @param s Json字符串
     * @param clazz 返回类型
     * @return 返回数组
     */
    public static <T> Object[] toArray(String s, Class<T> clazz) {
        return JSON.parseArray(s, clazz).toArray();
    }

    /**
     * json字符串转换为指定类型List
     * @param s json字符串
     * @param clazz 制定类型
     * @return 返回List
     */
    public static <T> List<T> toList(String s, Class<T> clazz) {
        return JSON.parseArray(s, clazz);
    }


    /**
     * json字符串转化为map
     * @param s json字符串
     * @return Map
     */
    public static <K, V> Map<K, V> toMap(String s) {
        return JSONObject.parseObject(s, Map.class);
    }

    /**
     * json字符串转化为指定类型对象
     * @param jsonData
     * @param clazz
     * @return
     */
    public static <T> T convertJsonToObject(String jsonData, Class<T> clazz) {
        return JSONObject.parseObject(jsonData, clazz);
    }

}