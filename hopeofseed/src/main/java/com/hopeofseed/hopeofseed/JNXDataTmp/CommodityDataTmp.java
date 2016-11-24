package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;

import java.util.ArrayList;

/**
 * Created by whisper on 2016/10/17.
 */

public class CommodityDataTmp extends RspBaseBean {
    private ArrayList<CommodityData> detail = new ArrayList<>();

    public ArrayList<CommodityData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommodityData> detail) {
        this.detail = detail;
    }
}
