package com.hopeofseed.hopeofseed.Http;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/12 16:40
 * 修改人：whisper
 * 修改时间：2016/10/12 16:40
 * 修改备注：
 */
public class RspBaseBean {

    /**
     * RequestSign : getNews
     * result : 1
     * resultNote : success
     * pages : 0
     * pageNo :
     * detail :
     */
    @SerializedName("RequestSign")
    public String RequestSign;
    @SerializedName("result")
    public int result;
    @SerializedName("resultNote")
    public String resultNote;
    @SerializedName("pages")
    public String pages;
    @SerializedName("pageNo")
    public String pageNo;
}
