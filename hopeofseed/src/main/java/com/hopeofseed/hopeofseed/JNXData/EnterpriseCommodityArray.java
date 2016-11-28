package com.hopeofseed.hopeofseed.JNXData;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/26 17:04
 * 修改人：whisper
 * 修改时间：2016/11/26 17:04
 * 修改备注：
 */
public class EnterpriseCommodityArray {
    private ArrayList<CommodityDataNoUser> mCommodityData = new ArrayList<>();
    /**
     * EnterpriseId : 0000000002
     * EnterpriseName : 山东亿丰种业有限公司
     * EnterpriseBusScrope : 小麦,大豆,玉米,棉花,花生
     * EnterpriseLevel : 1
     * EnterpriseTelephone : 0531-88670163
     * EnterpriseProvince : 山东省
     * EnterpriseCity : 济南市
     * EnterpriseZone : 历城区
     * EnterpriseAddressDetail : 山东省济南市历城区工业北路202号
     * EnterpriseFlag : 1
     * EnterpriseIntroduce : 山东亿丰种业有限公司是经省农业厅
     * EnterpriseLat : 36.714216
     * EnterpriseLon : 117.084013
     * user_id : 47
     */

    private String EnterpriseId;
    private String EnterpriseName;
    private String EnterpriseBusScrope;
    private String EnterpriseLevel;
    private String EnterpriseTelephone;
    private String EnterpriseProvince;
    private String EnterpriseCity;
    private String EnterpriseZone;
    private String EnterpriseAddressDetail;
    private String EnterpriseFlag;
    private String EnterpriseIntroduce;
    private String EnterpriseLat;
    private String EnterpriseLon;
    private String user_id;

    public ArrayList<CommodityDataNoUser> getCommodityData() {
        return mCommodityData;
    }

    public void setCommodityData(ArrayList<CommodityDataNoUser> mCommodityData) {
        this.mCommodityData = mCommodityData;
    }

    public String getEnterpriseId() {
        return EnterpriseId;
    }

    public void setEnterpriseId(String EnterpriseId) {
        this.EnterpriseId = EnterpriseId;
    }

    public String getEnterpriseName() {
        return EnterpriseName;
    }

    public void setEnterpriseName(String EnterpriseName) {
        this.EnterpriseName = EnterpriseName;
    }

    public String getEnterpriseBusScrope() {
        return EnterpriseBusScrope;
    }

    public void setEnterpriseBusScrope(String EnterpriseBusScrope) {
        this.EnterpriseBusScrope = EnterpriseBusScrope;
    }

    public String getEnterpriseLevel() {
        return EnterpriseLevel;
    }

    public void setEnterpriseLevel(String EnterpriseLevel) {
        this.EnterpriseLevel = EnterpriseLevel;
    }

    public String getEnterpriseTelephone() {
        return EnterpriseTelephone;
    }

    public void setEnterpriseTelephone(String EnterpriseTelephone) {
        this.EnterpriseTelephone = EnterpriseTelephone;
    }

    public String getEnterpriseProvince() {
        return EnterpriseProvince;
    }

    public void setEnterpriseProvince(String EnterpriseProvince) {
        this.EnterpriseProvince = EnterpriseProvince;
    }

    public String getEnterpriseCity() {
        return EnterpriseCity;
    }

    public void setEnterpriseCity(String EnterpriseCity) {
        this.EnterpriseCity = EnterpriseCity;
    }

    public String getEnterpriseZone() {
        return EnterpriseZone;
    }

    public void setEnterpriseZone(String EnterpriseZone) {
        this.EnterpriseZone = EnterpriseZone;
    }

    public String getEnterpriseAddressDetail() {
        return EnterpriseAddressDetail;
    }

    public void setEnterpriseAddressDetail(String EnterpriseAddressDetail) {
        this.EnterpriseAddressDetail = EnterpriseAddressDetail;
    }

    public String getEnterpriseFlag() {
        return EnterpriseFlag;
    }

    public void setEnterpriseFlag(String EnterpriseFlag) {
        this.EnterpriseFlag = EnterpriseFlag;
    }

    public String getEnterpriseIntroduce() {
        return EnterpriseIntroduce;
    }

    public void setEnterpriseIntroduce(String EnterpriseIntroduce) {
        this.EnterpriseIntroduce = EnterpriseIntroduce;
    }

    public String getEnterpriseLat() {
        return EnterpriseLat;
    }

    public void setEnterpriseLat(String EnterpriseLat) {
        this.EnterpriseLat = EnterpriseLat;
    }

    public String getEnterpriseLon() {
        return EnterpriseLon;
    }

    public void setEnterpriseLon(String EnterpriseLon) {
        this.EnterpriseLon = EnterpriseLon;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
