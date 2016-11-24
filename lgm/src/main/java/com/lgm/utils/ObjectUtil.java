package com.lgm.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @FileName:com.mgkj.Utils
 * @Desc:
 * @Author:liguangming
 * @Date:2016/3/28
 * @Copyright:2014-2016 Moogeek
 */
public class ObjectUtil {
    /**
     * @param obj
     * @return T
     * @desc 解决未检查Object警告
     */
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /*
    * @desc UTF-8转GBK
    * @author lgm
    * @time 2016/8/10 15:13
    * */
    public static String UTFtoGBK(String strUtf) {
        String strGBK = null;
        try {
            strGBK = URLEncoder.encode(strUtf, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strGBK;
    }

    /*
* @desc 去除双引号
* @author lgm
* @time 2016/8/10 15:13
* */
    public static String RemoveQuotes(String oldStr) {
        String newStr = null;
        newStr = oldStr.replaceAll("\"", "\\\"");
        return newStr;
    }

    /*
    * @desc String 字符串转换
    * @author lgm 
    * @time 2016/8/18 8:54
    * */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /*
    * @desc xml转JSON
    * @author lgm
    * @time 2016/8/18 9:30
    * */
    public static String xml2JSON(String xml) {
        try {
            JSONObject obj = XML.toJSONObject(xml);
            return obj.toString();
        } catch (JSONException e) {
            System.err.println("xml->json失败" + e.getLocalizedMessage());
            return "";
        }
    }
}
