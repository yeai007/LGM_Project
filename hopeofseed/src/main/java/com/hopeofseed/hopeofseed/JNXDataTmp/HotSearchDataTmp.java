package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.HotSearchData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/28 20:20
 * 修改人：whisper
 * 修改时间：2016/11/28 20:20
 * 修改备注：
 */
public class HotSearchDataTmp extends RspBaseBean{
    private ArrayList<HotSearchData> detail = new ArrayList<>();

    public ArrayList<HotSearchData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<HotSearchData> detail) {
        this.detail = detail;
    }
}
