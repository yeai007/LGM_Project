package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.GroupData;

import java.util.ArrayList;

/**
 * Created by whisper on 2016/10/17.
 */

public class GroupDataTmp extends RspBaseBean {
    ArrayList<GroupData> detail = new ArrayList<>();

    public ArrayList<GroupData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<GroupData> detail) {
        this.detail = detail;
    }
}
