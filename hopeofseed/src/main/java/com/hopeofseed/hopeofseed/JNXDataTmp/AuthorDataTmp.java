package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AuthorData;
import com.hopeofseed.hopeofseed.model.Bean;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 18:54
 * 修改人：whisper
 * 修改时间：2016/10/23 18:54
 * 修改备注：
 */
public class AuthorDataTmp extends RspBaseBean{
    private ArrayList<AuthorData> detail = new ArrayList<>();

    public ArrayList< AuthorData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList< AuthorData> detail) {
        this.detail = detail;
    }
}
