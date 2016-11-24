package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentOrForward;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/16 17:05
 * 修改人：whisper
 * 修改时间：2016/10/16 17:05
 * 修改备注：
 */
public class CommentOrForwardTmp extends RspBaseBean {
    private ArrayList<CommentOrForward> detail = new ArrayList<>();

    public ArrayList<CommentOrForward> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommentOrForward> detail) {
        this.detail = detail;
    }
}
