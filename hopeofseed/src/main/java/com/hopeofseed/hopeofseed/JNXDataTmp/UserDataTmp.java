package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/26 17:35
 * 修改人：whisper
 * 修改时间：2016/10/26 17:35
 * 修改备注：
 */
public class UserDataTmp extends RspBaseBean {
    private UserData detail = new UserData();

    public UserData getDetail() {
        return detail;
    }

    public void setDetail(UserData detail) {
        this.detail = detail;
    }
}
