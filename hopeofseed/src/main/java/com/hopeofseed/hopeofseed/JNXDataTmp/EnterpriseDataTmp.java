package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/18 8:44
 * 修改人：whisper
 * 修改时间：2016/10/18 8:44
 * 修改备注：
 */
public class EnterpriseDataTmp extends RspBaseBean {
    ArrayList<EnterpriseData> detail = new ArrayList<>();

    public ArrayList<EnterpriseData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<EnterpriseData> detail) {
        this.detail = detail;
    }
}
