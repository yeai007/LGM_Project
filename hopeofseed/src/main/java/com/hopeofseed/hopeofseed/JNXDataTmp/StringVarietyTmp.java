package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.StringVariety;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/16 11:22
 * 修改人：whisper
 * 修改时间：2016/12/16 11:22
 * 修改备注：
 */
public class StringVarietyTmp extends RspBaseBean {
    private ArrayList<StringVariety> detail = new ArrayList<>();

    public ArrayList<StringVariety> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<StringVariety> detail) {
        this.detail = detail;
    }
}
