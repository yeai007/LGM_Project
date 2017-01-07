package com.lgm.utils;

import java.text.DecimalFormat;

import static java.lang.Double.parseDouble;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/3 15:16
 * 修改人：whisper
 * 修改时间：2017/1/3 15:16
 * 修改备注：
 */
public class CalculationUtil {
    /**
     * @desc 两个整数相除, 返回保留位数的String
     * @author lgm
     * @time 2017/1/3 15:22
     */
    public static String IntDivide(int divisor, int dividend, int digit) {
        String condition = "";
        for (int i = 0; i < digit; i++) {
            condition = condition + "0";
        }
        String Str = "0." + condition;
        float num = (float) divisor / dividend;
        DecimalFormat df = new DecimalFormat(Str);
        String s = df.format(num);
        return s;
    }

    public static String StringToPercent(String str) {
        String percent = "";
        DecimalFormat    df   = new DecimalFormat("######0.00");
        double fPercent=Double.parseDouble(str);
        String aa=df.format(fPercent * 100);


        percent = aa+"%";
        return percent;
    }
}
