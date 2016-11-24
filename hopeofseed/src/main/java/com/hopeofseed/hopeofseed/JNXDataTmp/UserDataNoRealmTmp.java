package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 18:37
 * 修改人：whisper
 * 修改时间：2016/10/23 18:37
 * 修改备注：
 */
public class UserDataNoRealmTmp extends RspBaseBean{
    private ArrayList<UserDataNoRealm> detail = new ArrayList<>();

    public ArrayList<UserDataNoRealm> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<UserDataNoRealm> detail) {
        this.detail = detail;
    }
}
