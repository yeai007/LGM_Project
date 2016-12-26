package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.MediaUserData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/26 14:30
 * 修改人：whisper
 * 修改时间：2016/12/26 14:30
 * 修改备注：
 */
public class MediaUserDataTmp extends RspBaseBean{
    private ArrayList<MediaUserData> detail = new ArrayList<>();

    public ArrayList<MediaUserData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<MediaUserData> detail) {
        this.detail = detail;
    }
}
