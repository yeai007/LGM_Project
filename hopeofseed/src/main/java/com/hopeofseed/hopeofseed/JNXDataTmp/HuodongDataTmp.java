package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.HuodongData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/13 10:30
 * 修改人：whisper
 * 修改时间：2016/12/13 10:30
 * 修改备注：
 */
public class HuodongDataTmp extends RspBaseBean{
    private ArrayList<HuodongData> detail = new ArrayList<>();

    public ArrayList<HuodongData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<HuodongData> detail) {
        this.detail = detail;
    }
}
