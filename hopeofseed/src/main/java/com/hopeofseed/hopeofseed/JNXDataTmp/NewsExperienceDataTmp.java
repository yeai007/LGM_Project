package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsExperienceData;

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
public class NewsExperienceDataTmp extends RspBaseBean {
    private ArrayList<NewsExperienceData> detail;

    public ArrayList<NewsExperienceData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<NewsExperienceData> detail) {
        this.detail = detail;
    }
}
