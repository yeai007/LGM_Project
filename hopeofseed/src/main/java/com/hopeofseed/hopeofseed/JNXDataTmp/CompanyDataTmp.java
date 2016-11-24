package com.hopeofseed.hopeofseed.JNXDataTmp;

import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CompanyData;

import java.util.ArrayList;

/**
 * Created by whisper on 2016/10/16.
 */

public class CompanyDataTmp extends RspBaseBean{
    private ArrayList<CompanyData> detail = new ArrayList<>();

    public ArrayList<CompanyData> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<CompanyData> detail) {
        this.detail = detail;
    }
}
