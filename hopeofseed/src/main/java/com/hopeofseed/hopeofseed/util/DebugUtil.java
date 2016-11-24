package com.hopeofseed.hopeofseed.util;

import android.util.Log;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/6 11:04
 * 修改人：whisper
 * 修改时间：2016/10/6 11:04
 * 修改备注：
 */
public class DebugUtil {
    // 使用Log来显示调试信息,因为log在实现上每个message有4k字符长度限制
    // 所以这里使用自己分节的方式来输出足够长度的message
    public static void loglong(String log_tag, String str) {
        str = str.trim();
        int index = 0;
        int maxLength = 4000;
        String sub;
        while (index < str.length()) {
            // java的字符不允许指定超过总的长度end
            if (str.length() <= index + maxLength) {
                sub = str.substring(index);
            } else {
                sub = str.substring(index, maxLength);
            }

            index += maxLength;
            Log.i(log_tag, sub.trim());
        }
    }
}
