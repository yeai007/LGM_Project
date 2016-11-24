package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;
import com.hopeofseed.hopeofseed.JNXData.FollowedFriend;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/24 15:57
 * 修改人：whisper
 * 修改时间：2016/10/24 15:57
 * 修改备注：
 */
public class FollowedFriendTmp extends RspBaseBean{
    ArrayList<FollowedFriend> detail = new ArrayList<>();

    public ArrayList<FollowedFriend> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<FollowedFriend> detail) {
        this.detail = detail;
    }
}
