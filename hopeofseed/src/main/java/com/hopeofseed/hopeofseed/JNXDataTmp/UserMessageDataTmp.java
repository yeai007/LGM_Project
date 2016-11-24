package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserMessageData;


import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/14 17:16
 * 修改人：whisper
 * 修改时间：2016/11/14 17:16
 * 修改备注：
 */
public class UserMessageDataTmp extends RspBaseBean {
    private ArrayList<UserMessageData> detail = new ArrayList<>();

    public ArrayList<UserMessageData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<UserMessageData> detail) {
        this.detail = detail;
    }
}
