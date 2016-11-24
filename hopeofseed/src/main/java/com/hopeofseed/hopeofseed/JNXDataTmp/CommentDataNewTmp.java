package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentDataNew;


import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/21 17:24
 * 修改人：whisper
 * 修改时间：2016/11/21 17:24
 * 修改备注：
 */
public class CommentDataNewTmp extends RspBaseBean {
    private ArrayList<CommentDataNew> detail = new ArrayList<>();

    public ArrayList<CommentDataNew> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CommentDataNew> detail) {
        this.detail = detail;
    }
}
