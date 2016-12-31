package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CompanyData;
import com.hopeofseed.hopeofseed.JNXData.ConfigData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/31 10:35
 * 修改人：whisper
 * 修改时间：2016/12/31 10:35
 * 修改备注：
 */
public class ConfigDataTmp extends RspBaseBean {
    private ArrayList<ConfigData> detail = new ArrayList<>();

    public ArrayList<ConfigData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<ConfigData> detail) {
        this.detail = detail;
    }
}
