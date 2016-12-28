package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsYieldData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/30 19:27
 * 修改人：whisper
 * 修改时间：2016/11/30 19:27
 * 修改备注：
 */
public class NewsYieldDataTmp extends RspBaseBean{
    private ArrayList<NewsYieldData> detail;

    public ArrayList<NewsYieldData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<NewsYieldData> detail) {
        this.detail = detail;
    }
}
