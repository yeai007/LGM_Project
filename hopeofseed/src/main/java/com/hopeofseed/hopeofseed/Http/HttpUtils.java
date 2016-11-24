package com.hopeofseed.hopeofseed.Http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sun.bl
 * @version [1.0, 2016/10/13]
 */
public class HttpUtils {
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public HttpUtils() {
    }

    public void httpPost(String url, HashMap<String, String> params, Class<?> beanType, NetCallBack netCallBack) {
        NetRunnable runnable = new NetRunnable(url, "POST", params, beanType, netCallBack);
        cachedThreadPool.execute(runnable);
    }

    public void httpGet() {

    }

    public void httpPostFiles(String url, HashMap<String, String> params, List<File> fileParams, Class<?> beanType, NetCallBack netCallBack) {
        NetRunnable runnable = new NetRunnable(url, "POST", params, fileParams, beanType, netCallBack);
        cachedThreadPool.execute(runnable);
    }

}
