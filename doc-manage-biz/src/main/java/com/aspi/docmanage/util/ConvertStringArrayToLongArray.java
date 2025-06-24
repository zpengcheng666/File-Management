package com.aspi.docmanage.util;

/**
 * @Author：zpc
 * @Date：2025/1/15 上午10:15
 * 将字符串数组转换为 Long 数组
 */
public class ConvertStringArrayToLongArray {
    public static Long[] convertStringArrayToLongArray(String[] stringArray) {
        // 创建 Long 数组
        Long[] longArray = new Long[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            // 将每个 String 转换为 Long
            longArray[i] = Long.parseLong(stringArray[i]);
        }

        return longArray;
    }
}
