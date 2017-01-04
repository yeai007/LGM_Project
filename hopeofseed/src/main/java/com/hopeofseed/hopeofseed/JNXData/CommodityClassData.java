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
     * VC2COUNT : 9
     * CommodityVariety : 登海11
     * VC1COUNT : 9
     * CommodityName : 登海11
     * CommodityId : 0000000026
     */

    private String CommodityVariety_2;
    private String VC2COUNT;
    private String CommodityVariety;
    private String VC1COUNT;
    private String CommodityName;
    @PrimaryKey
    private String CommodityId;

    public String getCommodityVariety_2() {
        return CommodityVariety_2;
    }

    public void setCommodityVariety_2(String CommodityVariety_2) {
        this.CommodityVariety_2 = CommodityVariety_2;
    }

    public String getVC2COUNT() {
        return VC2COUNT;
    }

    public void setVC2COUNT(String VC2COUNT) {
        this.VC2COUNT = VC2COUNT;
    }

    public String getCommodityVariety() {
        return CommodityVariety;
    }

    public void setCommodityVariety(String CommodityVariety) {
        this.CommodityVariety = CommodityVariety;
    }

    public String getVC1COUNT() {
        return VC1COUNT;
    }

    public void setVC1COUNT(String VC1COUNT) {
        this.VC1COUNT = VC1COUNT;
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
