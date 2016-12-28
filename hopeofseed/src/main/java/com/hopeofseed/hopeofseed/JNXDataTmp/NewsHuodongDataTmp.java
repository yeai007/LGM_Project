package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsHuodongData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/1 9:06
 * 修改人：whisper
 * 修改时间：2016/12/1 9:06
 * 修改备注：
 */
public class NewsHuodongDataTmp extends RspBaseBean{
    private ArrayList<NewsHuodongData> detail;

    public ArrayList<NewsHuodongData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<NewsHuodongData> detail) {
        this.detail = detail;
    }
}
