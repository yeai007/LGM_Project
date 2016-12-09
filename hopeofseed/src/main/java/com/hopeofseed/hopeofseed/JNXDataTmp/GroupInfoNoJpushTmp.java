package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.GroupInfoNoJpush;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/9 13:04
 * 修改人：whisper
 * 修改时间：2016/12/9 13:04
 * 修改备注：
 */
public class GroupInfoNoJpushTmp extends RspBaseBean{
    ArrayList<GroupInfoNoJpush> detail = new ArrayList<>();

    public ArrayList<GroupInfoNoJpush> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<GroupInfoNoJpush> detail) {
        this.detail = detail;
    }
}
