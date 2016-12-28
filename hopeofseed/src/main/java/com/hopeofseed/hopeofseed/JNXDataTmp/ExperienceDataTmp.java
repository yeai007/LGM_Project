package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExperienceData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 18:18
 * 修改人：whisper
 * 修改时间：2016/10/23 18:18
 * 修改备注：
 */
public class ExperienceDataTmp extends RspBaseBean {
    ArrayList<ExperienceData> detail = new ArrayList<>();

    public ArrayList<ExperienceData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<ExperienceData> detail) {
        this.detail = detail;
    }
}
