package com.hopeofseed.hopeofseed.JNXData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 9:36
 * 修改人：whisper
 * 修改时间：2016/12/30 9:36
 * 修改备注：
 */
public class CommodityClassData extends RealmObject {

    /**
     * CommodityVariety_2 : 玉米
     * CommodityVariety : 登海3687
     * CommodityName : 登海3687
     * CommodityId : 0000000020
     */

    private String CommodityVariety_2;
    private String CommodityVariety;
    private String CommodityName;
    @PrimaryKey
    private String CommodityId;

    public String getCommodityVariety_2() {
        return CommodityVariety_2;
    }

    public void setCommodityVariety_2(String CommodityVariety_2) {
        this.CommodityVariety_2 = CommodityVariety_2;
    }

    public String getCommodityVariety() {
        return CommodityVariety;
    }

    public void setCommodityVariety(String CommodityVariety) {
        this.CommodityVariety = CommodityVariety;
    }

    public String getCommodityName() {
        return CommodityName;
    }

    public void setCommodityName(String CommodityName) {
        this.CommodityName = CommodityName;
    }

    public String getCommodityId() {
        return CommodityId;
    }

    public void setCommodityId(String CommodityId) {
        this.CommodityId = CommodityId;
    }
}
