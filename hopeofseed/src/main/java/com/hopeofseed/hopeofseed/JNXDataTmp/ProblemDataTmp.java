package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ProblemData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 10:14
 * 修改人：whisper
 * 修改时间：2016/10/23 10:14
 * 修改备注：
 */
public class ProblemDataTmp extends RspBaseBean {
    private ArrayList<ProblemData> detail = new ArrayList<>();

    public ArrayList<ProblemData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<ProblemData> detail) {
        this.detail = detail;
    }

}
