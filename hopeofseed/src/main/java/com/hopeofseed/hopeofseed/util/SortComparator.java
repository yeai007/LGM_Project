package com.hopeofseed.hopeofseed.util;

import android.util.Log;
import com.hopeofseed.hopeofseed.JNXData.ConvAndGroupData;
import com.lgm.utils.DateTools;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Comparator;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/21 7:57
 * 修改人：whisper
 * 修改时间：2016/12/21 7:57
 * 修改备注：
 */
public class SortComparator implements Comparator {
    private static final String TAG = "SortComparator";

    @Override
    public int compare(Object o1, Object o2) {
        Long time1 = new Long(0l);
        Long time2 = new Long(0l);
        String t1 = "";
        String t2 = "";
        Conversation itemData1 = ((ConvAndGroupData) o1).getConversation();
        if (itemData1 != null) {
            Message message1 = itemData1.getLatestMessage();
            if (message1 != null) {
                time1 = message1.getCreateTime();
                t1 = DateTools.getLongToString(time1);
            } else {
                try {
                    t1 = DateTools.getNowTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                t1 = DateTools.getNowTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Conversation itemData2 = ((ConvAndGroupData) o2).getConversation();
        if (itemData2 != null) {
            Message message2 = itemData2.getLatestMessage();
            if (message2 != null) {
                time2 = message2.getCreateTime();
                t2 = DateTools.getLongToString(time2);
            } else {
                try {
                    t2 = DateTools.getNowTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                t2 = DateTools.getNowTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Long[] longDiff = null;
        try {
            longDiff = DateTools.getDiffTime(t2, t1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";
        String hour1 = String.valueOf(2);
        if (longDiff[0] < 0) {
            result = "-" + Math.abs(Integer.parseInt(String.valueOf(longDiff[1]))) + zhuijia(Math.abs(Integer.parseInt(String.valueOf(longDiff[2])))) + zhuijia(Math.abs(Integer.parseInt(String.valueOf(longDiff[3]))));

        } else {
            result = String.valueOf(longDiff[1]) + zhuijia(Integer.parseInt(String.valueOf(longDiff[2]))) + zhuijia(Integer.parseInt(String.valueOf(longDiff[3])));
        }
        Log.e(TAG, "compare: " + result);
        int iResult = Integer.parseInt(result);
        if (iResult == 0) {
            iResult = 10000000;
        }
        return iResult;
    }

    private String zhuijia(int str) {
        DecimalFormat df = new DecimalFormat("00");
        String str2 = df.format(str);
        return str2;
    }

}
