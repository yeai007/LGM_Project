package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 10:14
 * 修改人：whisper
 * 修改时间：2016/10/23 10:14
 * 修改备注：
 */
public class CropDataTmp extends RspBaseBean {
    private ArrayList<CropData> detail = new ArrayList<>();

    public ArrayList<CropData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CropData> detail) {
        this.detail = detail;
    }

}
