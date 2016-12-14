package com.hopeofseed.hopeofseed.Http;

import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Sun.bl
 * @version [1.0, 2016/10/13]
 */
public class HttpWork {
    private static final String TAG = "HttpWork";

    public static String post(String url, HashMap<String, String> param) {
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
            Log.d("HttpConnection", "Http返回错误"+e.toString());
/*            return postResult;*/

        } finally {
            Log.e("HttpConnection", "发送信息URL：" + url);
            Log.e("HttpConnection", "发送信息：" + param);
            Log.e("HttpConnection返回信息：", postResult);
        }

        return postResult;

    }

    public static String get() {

        return null;
    }

    public static String postFiles(String url, HashMap<String, String> param, List<File> mFileParam) {
        ArrayList<HttpParams.FileWrapper> fileWrappers = new ArrayList<HttpParams.FileWrapper>();
        if (!(mFileParam == null)) {
            for (File file : mFileParam) {
                HttpParams.FileWrapper fileWrapper = new HttpParams.FileWrapper(file, file.getName(), null);
                fileWrappers.add(fileWrapper);
            }
        }
        String result =
                null;
        try {
            Response response = null;
            if (!(mFileParam == null)) {
                response = OkGo.post(url)
                        .params(param)
                        .addFileWrapperParams("file[]", fileWrappers)
                        .execute();
            } else {
                response = OkGo.post(url)
                        .params(param)
                        .execute();
            }
            if (response.isSuccessful()) {
                result = response.body().string();
            } else {
                result = "网络出错";
            }

        } catch (IOException e) {
            result = "获取数据错误";
            e.printStackTrace();
        } finally {
            Log.d("HttpConnection", "发送信息URL：" + url);
            Log.d("HttpConnection", "发送信息：" + param);
            Log.d("HttpConnection返回信息：", result);
        }
        return result;
    }
}
