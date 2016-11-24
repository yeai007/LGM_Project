package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AutoData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/14 8:02
 * 修改人：whisper
 * 修改时间：2016/11/14 8:02
 * 修改备注：
 */
public class AutoDataTmp extends RspBaseBean{
    private ArrayList<AutoData> detail = new ArrayList<>();

    public ArrayList< AutoData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList< AutoData> detail) {
        this.detail = detail;
    }
}
