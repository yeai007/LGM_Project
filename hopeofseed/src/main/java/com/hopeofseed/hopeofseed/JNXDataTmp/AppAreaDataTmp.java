package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AppAreaData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/3 8:05
 * 修改人：whisper
 * 修改时间：2017/1/3 8:05
 * 修改备注：
 */
public class AppAreaDataTmp extends RspBaseBean {
    private ArrayList<AppAreaData> detail = new ArrayList<>();

    public ArrayList<AppAreaData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<AppAreaData> detail) {
        this.detail = detail;
    }
}
