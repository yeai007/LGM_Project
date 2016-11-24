package com.hopeofseed.hopeofseed.Http;

/**
 * @author Sun.bl
 * @version [1.0, 2016/10/13]
 */
public interface NetCallBack {

    //请求成功
    void onSuccess(RspBaseBean rspBaseBean);

    //请求错误（可以自己写相应的参数）
    void onError(String error);


    //请求失败（可以自己写相应的参数）
    void onFail();

}
