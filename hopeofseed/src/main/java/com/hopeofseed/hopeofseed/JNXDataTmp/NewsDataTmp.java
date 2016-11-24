package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/13 9:38
 * 修改人：whisper
 * 修改时间：2016/10/13 9:38
 * 修改备注：
 */
public class NewsDataTmp extends RspBaseBean {
    private ArrayList<NewsData> detail = new ArrayList<>();

    public ArrayList<NewsData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<NewsData> detail) {
        this.detail = detail;
    }
}
