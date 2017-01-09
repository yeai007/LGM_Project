package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropUserData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/9 10:52
 * 修改人：whisper
 * 修改时间：2017/1/9 10:52
 * 修改备注：
 */
public class CropUserDataTmp extends RspBaseBean{
    private ArrayList<CropUserData> detail = new ArrayList<>();

    public ArrayList<CropUserData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CropUserData> detail) {
        this.detail = detail;
    }
}
