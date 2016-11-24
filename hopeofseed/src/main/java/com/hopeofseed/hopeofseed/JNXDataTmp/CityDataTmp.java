package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CityData;


import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/4 20:09
 * 修改人：whisper
 * 修改时间：2016/11/4 20:09
 * 修改备注：
 */
public class CityDataTmp extends RspBaseBean{
    private ArrayList<CityData> detail = new ArrayList<>();

    public ArrayList<CityData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CityData> detail) {
        this.detail = detail;
    }
}
