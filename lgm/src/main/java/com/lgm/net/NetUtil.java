package com.lgm.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

/**
 * @FileName:net
 * @Desc:网络请求处理类
 * @Author:liguangming
 * @Date:2016/3/15
 * @Copyright:Moogeek
 */
public class NetUtil {
    static String TAG = "NetUtil";

    /**
     * POST方法获取网络数据
     *
     * @param url
     * @param param
     * @return string
     */
    public static String post(String url, HashMap<String, String> param) {
        URL post_url = null;
        String postResult = "";
        StringBuffer params_str = new StringBuffer();
        PrintWriter printWriter = null;
        List<Attributes.Name> params = null;
        try {
            post_url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            Iterator it = param.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry element = (Map.Entry) it.next();
                params_str.append(element.getKey());
                params_str.append("=");
                params_str.append(element.getValue());
                params_str.append("&");
            }
            if (params_str.length() > 0) {
                params_str.deleteCharAt(params_str.length() - 1);
            }
            conn = (HttpURLConnection) post_url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(params_str.length()));
            printWriter = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            printWriter.write(params_str.toString());
            // flush输出流的缓冲
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.setConnectTimeout(6 * 1000);
            if (conn.getResponseCode() != 200) // 从Internet获取网页,发送请求,将网页以流的形式读回来
            {
                throw new RuntimeException("请求url失败");
            } else {
                InputStream is = conn.getInputStream();
                InputStreamReader in = new InputStreamReader(is);
                BufferedReader buffer = new BufferedReader(in);
                String inputLine = null;
                while (((inputLine = buffer.readLine()) != null)) {
                    postResult += inputLine;
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            return postResult;
        } finally {
            Log.d(TAG, "sendURL：" + url);
            Log.d(TAG, "sendMessage：" + param);
            Log.d(TAG, "result：" + postResult);
        }
        return postResult;
    }
}