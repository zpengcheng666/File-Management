package com.aspi.docmanage.util;

import java.util.List;

/**
 * @Author：zpc
 * @Date：2025/1/15 上午10:13
 * @Description： List<Long> 转换为 Long[]
 */
public class ToPrimitiveLongArray {
    public static Long[] toPrimitiveLongArray(List<Long> longObjectList) {
        if (longObjectList == null || longObjectList.isEmpty()) {
            return new Long[0];
        }
        Long[] result = new Long[longObjectList.size()];
        for (int i = 0; i < longObjectList.size(); i++) {
            result[i] = longObjectList.get(i).longValue();
        }

        return result;
    }
}
