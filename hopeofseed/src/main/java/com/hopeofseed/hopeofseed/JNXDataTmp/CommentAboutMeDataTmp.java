package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentAboutMeData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/5 18:07
 * 修改人：whisper
 * 修改时间：2016/12/5 18:07
 * 修改备注：
 */
public class CommentAboutMeDataTmp extends RspBaseBean{
    private ArrayList<CommentAboutMeData> detail = new ArrayList<>();

    public ArrayList<CommentAboutMeData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommentAboutMeData> detail) {
        this.detail = detail;
    }
}
