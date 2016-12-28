package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.FriendData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/27 9:45
 * 修改人：whisper
 * 修改时间：2016/12/27 9:45
 * 修改备注：
 */
public class FriendDataTmp extends RspBaseBean {
    ArrayList<FriendData> detail = new ArrayList<>();

    public ArrayList<FriendData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<FriendData> detail) {
        this.detail = detail;
    }
}
