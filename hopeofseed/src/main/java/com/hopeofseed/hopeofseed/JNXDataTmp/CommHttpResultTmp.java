package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommHttpResult;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 15:59
 * 修改人：whisper
 * 修改时间：2016/10/23 15:59
 * 修改备注：
 */
public class CommHttpResultTmp extends RspBaseBean{
    private ArrayList<CommHttpResult> detail = new ArrayList<>();

    public ArrayList<CommHttpResult> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommHttpResult> detail) {
        this.detail = detail;
    }
}
