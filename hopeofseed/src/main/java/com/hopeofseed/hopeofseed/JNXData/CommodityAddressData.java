package com.hopeofseed.hopeofseed.JNXData;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 9:32
 * 修改人：whisper
 * 修改时间：2016/12/30 9:32
 * 修改备注：
 */
public class CommodityAddressData {

    /**
     * id : 0
     * count : 1
     * DistributorProvince : 山东省
     * DistributorProvinceId : 15
     * ProvinceCount : 17
     * DistributorCity : 济南市
     * DistributorCityId : 159
     * CityCount : 1
     * DistributorZone : 长清区
     * DistributorZoneId : 1665
     */

    private int id;
    private int count;
    private String DistributorProvince;
    private int DistributorProvinceId;
    private int ProvinceCount;
    private String DistributorCity;
    private int DistributorCityId;
    private int CityCount;
    private String DistributorZone;
    private int DistributorZoneId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDistributorProvince() {
        return DistributorProvince;
    }

    public void setDistributorProvince(String DistributorProvince) {
        this.DistributorProvince = DistributorProvince;
    }

    public int getDistributorProvinceId() {
        return DistributorProvinceId;
    }

    public void setDistributorProvinceId(int DistributorProvinceId) {
        this.DistributorProvinceId = DistributorProvinceId;
    }

    public int getProvinceCount() {
        return ProvinceCount;
    }

    public void setProvinceCount(int ProvinceCount) {
        this.ProvinceCount = ProvinceCount;
    }

    public String getDistributorCity() {
        return DistributorCity;
    }

    public void setDistributorCity(String DistributorCity) {
        this.DistributorCity = DistributorCity;
    }

    public int getDistributorCityId() {
        return DistributorCityId;
    }

    public void setDistributorCityId(int DistributorCityId) {
        this.DistributorCityId = DistributorCityId;
    }

    public int getCityCount() {
        return CityCount;
    }

    public void setCityCount(int CityCount) {
        this.CityCount = CityCount;
    }

    public String getDistributorZone() {
        return DistributorZone;
    }

    public void setDistributorZone(String DistributorZone) {
        this.DistributorZone = DistributorZone;
    }

    public int getDistributorZoneId() {
        return DistributorZoneId;
    }

    public void setDistributorZoneId(int DistributorZoneId) {
        this.DistributorZoneId = DistributorZoneId;
    }
}
