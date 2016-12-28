package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsProblemData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/30 19:27
 * 修改人：whisper
 * 修改时间：2016/11/30 19:27
 * 修改备注：
 */
public class NewsProblemDataTmp extends RspBaseBean{
    private ArrayList<NewsProblemData> detail;

    public ArrayList<NewsProblemData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<NewsProblemData> detail) {
        this.detail = detail;
    }
}
