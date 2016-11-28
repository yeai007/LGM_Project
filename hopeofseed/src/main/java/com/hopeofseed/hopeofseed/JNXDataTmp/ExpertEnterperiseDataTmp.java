package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExpertEnterperiseData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/28 17:58
 * 修改人：whisper
 * 修改时间：2016/11/28 17:58
 * 修改备注：
 */
public class ExpertEnterperiseDataTmp extends RspBaseBean{
    ArrayList<ExpertEnterperiseData> detail = new ArrayList<>();

    public ArrayList<ExpertEnterperiseData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<ExpertEnterperiseData> detail) {
        this.detail = detail;
    }
}
