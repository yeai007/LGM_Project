package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityAddressData;
import com.hopeofseed.hopeofseed.JNXData.CommodityClassData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 9:37
 * 修改人：whisper
 * 修改时间：2016/12/30 9:37
 * 修改备注：
 */
public class CommodityClassDataTmp extends RspBaseBean{
    private ArrayList<CommodityClassData> detail = new ArrayList<>();

    public ArrayList<CommodityClassData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommodityClassData> detail) {
        this.detail = detail;
    }
}
