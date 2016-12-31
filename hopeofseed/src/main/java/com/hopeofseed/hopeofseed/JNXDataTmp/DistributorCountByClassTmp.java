package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorCommodity;
import com.hopeofseed.hopeofseed.JNXData.DistributorCountByClass;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 16:53
 * 修改人：whisper
 * 修改时间：2016/12/30 16:53
 * 修改备注：
 */
public class DistributorCountByClassTmp extends RspBaseBean{
    private ArrayList<DistributorCountByClass> detail = new ArrayList<>();

    public ArrayList<DistributorCountByClass> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<DistributorCountByClass> detail) {
        this.detail = detail;
    }
}
