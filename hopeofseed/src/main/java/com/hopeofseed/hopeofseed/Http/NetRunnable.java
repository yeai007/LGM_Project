package com.hopeofseed.hopeofseed.Http;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.hopeofseed.hopeofseed.util.NullStringToEmptyAdapterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static cn.jpush.im.android.eventbus.EventBus.TAG;

/**
 * @author Sun.bl
 * @version [1.0, 2016/10/13]
 */
public class NetRunnable implements Runnable {

    private String mUrl;

    private String mMethod;

    private NetCallBack mNetCallBack;

    private Class<?> mBeanType;

    private Gson mGson;
    private HashMap<String, String> mParam;
    private List<File> mFileParam = new ArrayList<>();
    private boolean isPostFile = false;

    public NetRunnable(String url, String method, HashMap<String, String> params, Class<?> beanType, NetCallBack netCallBack) {
        mMethod = method;
        mUrl = url;
        mNetCallBack = netCallBack;
        mBeanType = beanType;
        mGson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        mParam = params;
        isPostFile = false;
    }

    public NetRunnable(String url, String method, HashMap<String, String> params, List<File> fileParam, Class<?> beanType, NetCallBack netCallBack) {
        mMethod = method;
        mUrl = url;
        mNetCallBack = netCallBack;
        mBeanType = beanType;
        mGson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        mParam = params;
        mFileParam = fileParam;
        isPostFile = true;
    }

    @Override
    public void run() {
        String json = null;
        final boolean postMethod = TextUtils.equals(mMethod, "POST");
        if (mUrl.startsWith("http")) {
            if (isPostFile) {
                json = postMethod ? HttpWork.postFiles(mUrl, mParam, mFileParam) : HttpWork.get();
               // Log.e(TAG, "run: " + json);
            } else {
                json = postMethod ? HttpWork.post(mUrl, mParam) : HttpWork.get();
            }

        } else if (mUrl.startsWith("https")) {
            json = postMethod ? HttpsWork.post() : HttpsWork.get();
        }

        if (mNetCallBack == null) {
            return;
        }

        if (TextUtils.isEmpty(json)) {
            mNetCallBack.onFail();
            return;
        }
        RspBaseBean baseBean = null;
       // Log.e(TAG, "run: " + json);
        try {
            baseBean = mGson.fromJson(json, RspBaseBean.class);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "run:data error ");
            mNetCallBack.onError("data error");
            return;
        }
        if (baseBean.result != 1) {
            mNetCallBack.onError(baseBean.resultNote);
            return;
        }
        Log.e(TAG, "run: " + json);
        Object bean = mGson.fromJson(json, mBeanType);
        if (bean == null) {
            return;
        }
        mNetCallBack.onSuccess((RspBaseBean) bean);

    }
}
