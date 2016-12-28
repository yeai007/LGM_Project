package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.SearchResultData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/22 17:15
 * 修改人：whisper
 * 修改时间：2016/10/22 17:15
 * 修改备注：
 */
public class SearchResultDataTmp extends RspBaseBean {
    private ArrayList<SearchResultData> detail = new ArrayList<>();

    public ArrayList<SearchResultData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<SearchResultData> detail) {
        this.detail = detail;
    }
}
