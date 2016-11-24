package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.PoliticData;
import com.hopeofseed.hopeofseed.JNXData.ProblemData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/11 18:10
 * 修改人：whisper
 * 修改时间：2016/11/11 18:10
 * 修改备注：
 */
public class PoliticDataTmp extends RspBaseBean{
    private ArrayList<PoliticData> detail = new ArrayList<>();

    public ArrayList<PoliticData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<PoliticData> detail) {
        this.detail = detail;
    }
}
