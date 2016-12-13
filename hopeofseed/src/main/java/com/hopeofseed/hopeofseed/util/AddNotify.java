package com.hopeofseed.hopeofseed.util;


import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;

import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/12 11:00
 * 修改人：whisper
 * 修改时间：2016/12/12 11:00
 * 修改备注：
 */
public class AddNotify implements NetCallBack{
    /**
     * 添加新成员通知事件
     * */
    public void AddNewGroupMember(String GroupId, List<String> usernameList) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("usernameList", usernameList.toString());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddNotify", opt_map,pushFileResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }
}
