package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityVarietyData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/3 17:16
 * 修改人：whisper
 * 修改时间：2016/11/3 17:16
 * 修改备注：
 */
public class CommodityVarietyDataTmp extends RspBaseBean {
    private ArrayList<CommodityVarietyData> detail = new ArrayList<>();

    public ArrayList<CommodityVarietyData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommodityVarietyData> detail) {
        this.detail = detail;
    }
}
