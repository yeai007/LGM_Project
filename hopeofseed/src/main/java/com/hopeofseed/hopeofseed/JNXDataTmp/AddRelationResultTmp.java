package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AddRelationResult;
import com.hopeofseed.hopeofseed.JNXData.AuthorData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/3 9:15
 * 修改人：whisper
 * 修改时间：2016/11/3 9:15
 * 修改备注：
 */
public class AddRelationResultTmp extends RspBaseBean{
    private ArrayList<AddRelationResult> detail = new ArrayList<>();

    public ArrayList<AddRelationResult> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<AddRelationResult> detail) {
        this.detail = detail;
    }
}
