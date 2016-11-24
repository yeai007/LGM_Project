package com.hopeofseed.hopeofseed.Http;

import android.util.Log;

import com.hopeofseed.hopeofseed.util.DebugUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

public class HttpConnection {
    public static String LogTAG = "HttpConnection";

    public static String SendPostList(String url, HashMap<String, String> param) {
        /** Called when the activity is first created. */
        URL post_url = null;
        String postResult = "";
        String content = "";
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
            for (Object o : param.entrySet()) {
                Map.Entry element = (Map.Entry) o;
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
                Log.d("HttpConnection", "请求url失败");
                throw new RuntimeException("请求url失败");
            } else {
                InputStream is = conn.getInputStream();
                if (is == null) {
                    Log.d("HttpConnection", "InputStream:null");
                }
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
            Log.d("HttpConnection", "发送信息URL：" + url);
            Log.d("HttpConnection", "发送信息：" + param);
            DebugUtil.loglong("HttpConnection返回信息：", postResult);
        }

        return postResult;
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param actionUrl
     * @param params
     * @param files
     * @return
     * @throws IOException
     */
    public static String uploadImgAndPara(String actionUrl, Map<String, String> params,
                                          Map<String, File> files) throws IOException {
        String postResult = "";
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);
        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
        Log.d(LogTAG, "待发数据：" + params.toString());
        DataOutputStream outStream = new DataOutputStream(conn
                .getOutputStream());
        outStream.write(sb.toString().getBytes());
        InputStream in = null;
        // 发送文件数据
        if (files != null) {
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1
                        .append("Content-Disposition: form-data; name=\"file[]\"; filename=\""
                                + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                in = conn.getInputStream();
                int ch;
                StringBuilder sb2 = new StringBuilder();
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
                Log.d(LogTAG, "返回数据：" + sb2.toString());
                postResult = sb2.toString();
            }
            outStream.close();
            conn.disconnect();

        }
        return postResult;
    }
}
