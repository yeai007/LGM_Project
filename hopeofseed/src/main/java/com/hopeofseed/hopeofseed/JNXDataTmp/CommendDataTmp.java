package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.Commend2Data;
import com.hopeofseed.hopeofseed.JNXData.CommendData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/20 13:00
 * 修改人：whisper
 * 修改时间：2016/11/20 13:00
 * 修改备注：
 */
public class CommendDataTmp extends RspBaseBean {
    private ArrayList<CommendData> detail = new ArrayList<>();

    public ArrayList<CommendData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommendData> detail) {
        this.detail = detail;
    }
}
