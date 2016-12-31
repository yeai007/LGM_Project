package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommHttpResult;
import com.hopeofseed.hopeofseed.JNXData.CommodityAddressData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 9:33
 * 修改人：whisper
 * 修改时间：2016/12/30 9:33
 * 修改备注：
 */
public class CommodityAddressDataTmp extends RspBaseBean {
    private ArrayList<CommodityAddressData> detail = new ArrayList<>();

    public ArrayList<CommodityAddressData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommodityAddressData> detail) {
        this.detail = detail;
    }
}
