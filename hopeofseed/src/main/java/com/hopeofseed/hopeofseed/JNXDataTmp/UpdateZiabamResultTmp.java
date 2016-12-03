package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.SpinnerAreaData;
import com.hopeofseed.hopeofseed.JNXData.UpdateZiabamResult;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/3 16:50
 * 修改人：whisper
 * 修改时间：2016/12/3 16:50
 * 修改备注：
 */
public class UpdateZiabamResultTmp extends RspBaseBean{
    private ArrayList<UpdateZiabamResult> detail = new ArrayList<>();

    public ArrayList<UpdateZiabamResult> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<UpdateZiabamResult> detail) {
        this.detail = detail;
    }
}
