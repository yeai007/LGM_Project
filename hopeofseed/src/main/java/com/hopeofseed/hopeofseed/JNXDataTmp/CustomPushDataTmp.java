package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CustomPushData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/23 9:27
 * 修改人：whisper
 * 修改时间：2016/12/23 9:27
 * 修改备注：
 */
public class CustomPushDataTmp extends RspBaseBean {

    private ArrayList<CustomPushData> detail = new ArrayList<>();

    public ArrayList<CustomPushData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CustomPushData> detail) {
        this.detail = detail;
    }
}
