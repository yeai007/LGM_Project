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
public class DistributorCommodityArray {

    /**
     * CommodityId : 0000000714
     * CommodityTitle : 商品123
     * CommodityName : 商品123
     * CommodityPrice : 0
     * CreateTime : 2016-11-26 16:14:08
     * CommodityFlag : 1
     * CommodityFlagTime : 2016-11-26 16:14:08
     * CommodityDescribe : 在我弄懂
     * Owner : 1127
     * NewId : 0
     * CommodityImgs : 67ef5551b38ecfeae84309a348f022ea.
     * CommodityVariety : 商品123
     * CommodityClass : 种子
     * OwnerClass : 1
     * CommodityVariety_1 : 大田
     * CommodityVariety_2 : 玉米
     * DistributorId : 0000000004
     * DistributorName : 测试A
     * DistributorTrademark :
     * DistributorLevel : 1
     * DistributorTelephone : 1212
     * DistributorFlag : 1
     * DistributorIntroduce : 测试A在花园东路
     * DistributorProvince : 山东
     * DistributorCity : 济南
     * DistributorZone : 历城区
     * DistributorAddressDetail : 花园东路
     * DistributorLat : 36.697694
     * DistributorLon : 117.104656
     * user_id : 1127
     */

    private ArrayList<CommodityDataNoUser> mCommodityData = new ArrayList<>();
    /**
     * UserAvatar : 675e8da40952fcbfa352783e9145248b.jpg
     */

    private String UserAvatar;

    public ArrayList<CommodityDataNoUser> getCommodityData() {
        return mCommodityData;
    }

    public void setCommodityData(ArrayList<CommodityDataNoUser> mCommodityData) {
        this.mCommodityData = mCommodityData;
    }

    private String DistributorId;
    private String DistributorName;
    private String DistributorTrademark;
    private String DistributorLevel;
    private String DistributorTelephone;
    private String DistributorFlag;
    private String DistributorIntroduce;
    private String DistributorProvince;
    private String DistributorCity;
    private String DistributorZone;
    private String DistributorAddressDetail;
    private String DistributorLat;
    private String DistributorLon;
    private String user_id;
    private String Distance;

    public String getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(String DistributorId) {
        this.DistributorId = DistributorId;
    }

    public String getDistributorName() {
        return DistributorName;
    }

    public void setDistributorName(String DistributorName) {
        this.DistributorName = DistributorName;
    }

    public String getDistributorTrademark() {
        return DistributorTrademark;
    }

    public void setDistributorTrademark(String DistributorTrademark) {
        this.DistributorTrademark = DistributorTrademark;
    }

    public String getDistributorLevel() {
        return DistributorLevel;
    }

    public void setDistributorLevel(String DistributorLevel) {
        this.DistributorLevel = DistributorLevel;
    }

    public String getDistributorTelephone() {
        return DistributorTelephone;
    }

    public void setDistributorTelephone(String DistributorTelephone) {
        this.DistributorTelephone = DistributorTelephone;
    }

    public String getDistributorFlag() {
        return DistributorFlag;
    }

    public void setDistributorFlag(String DistributorFlag) {
        this.DistributorFlag = DistributorFlag;
    }

    public String getDistributorIntroduce() {
        return DistributorIntroduce;
    }

    public void setDistributorIntroduce(String DistributorIntroduce) {
        this.DistributorIntroduce = DistributorIntroduce;
    }

    public String getDistributorProvince() {
        return DistributorProvince;
    }

    public void setDistributorProvince(String DistributorProvince) {
        this.DistributorProvince = DistributorProvince;
    }

    public String getDistributorCity() {
        return DistributorCity;
    }

    public void setDistributorCity(String DistributorCity) {
        this.DistributorCity = DistributorCity;
    }

    public String getDistributorZone() {
        return DistributorZone;
    }

    public void setDistributorZone(String DistributorZone) {
        this.DistributorZone = DistributorZone;
    }

    public String getDistributorAddressDetail() {
        return DistributorAddressDetail;
    }

    public void setDistributorAddressDetail(String DistributorAddressDetail) {
        this.DistributorAddressDetail = DistributorAddressDetail;
    }

    public String getDistributorLat() {
        return DistributorLat;
    }

    public void setDistributorLat(String DistributorLat) {
        this.DistributorLat = DistributorLat;
    }

    public String getDistributorLon() {
        return DistributorLon;
    }

    public void setDistributorLon(String DistributorLon) {
        this.DistributorLon = DistributorLon;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        this.Distance = distance;
    }

    public String getUserAvatar() {
        return UserAvatar;
    }

    public void setUserAvatar(String UserAvatar) {
        this.UserAvatar = UserAvatar;
    }
}
