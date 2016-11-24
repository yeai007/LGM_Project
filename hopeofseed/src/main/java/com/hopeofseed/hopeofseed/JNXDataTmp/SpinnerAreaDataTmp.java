package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.SpinnerAreaData;


import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/12 9:27
 * 修改人：whisper
 * 修改时间：2016/11/12 9:27
 * 修改备注：
 */
public class SpinnerAreaDataTmp extends RspBaseBean {
    private ArrayList<SpinnerAreaData> detail = new ArrayList<>();

    public ArrayList<SpinnerAreaData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<SpinnerAreaData> detail) {
        this.detail = detail;
    }
}
