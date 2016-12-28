package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 19:05
 * 修改人：whisper
 * 修改时间：2016/10/23 19:05
 * 修改备注：
 */
public class ExpertDataTmp extends RspBaseBean{
    ArrayList<ExpertData> detail = new ArrayList<>();

    public ArrayList<ExpertData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<ExpertData> detail) {
        this.detail = detail;
    }
}
