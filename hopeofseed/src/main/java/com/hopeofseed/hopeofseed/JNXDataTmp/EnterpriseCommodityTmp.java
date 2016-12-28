package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseCommodity;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/26 17:05
 * 修改人：whisper
 * 修改时间：2016/11/26 17:05
 * 修改备注：
 */
public class EnterpriseCommodityTmp extends RspBaseBean {
    private ArrayList<EnterpriseCommodity> detail = new ArrayList<>();

    public ArrayList<EnterpriseCommodity> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<EnterpriseCommodity> detail) {
        this.detail = detail;
    }
}
