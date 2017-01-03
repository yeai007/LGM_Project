package com.hopeofseed.hopeofseed.util;

import com.hopeofseed.hopeofseed.JNXData.AppAreaData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

import citypickerview.model.CityModel;
import citypickerview.model.DistrictModel;
import citypickerview.model.ProvinceModel;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/3 9:48
 * 修改人：whisper
 * 修改时间：2017/1/3 9:48
 * 修改备注：
 */
public class AppAreaDatasUtil {
    static ProvinceModel provinceModel = new ProvinceModel();
    static CityModel cityModel = new CityModel();
    static DistrictModel districtModel = new DistrictModel();
    private static List<ProvinceModel> provinceList = new ArrayList<>();

    public static List<ProvinceModel> getProvinceList() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<AppAreaData> results1 =
                mRealm.where(AppAreaData.class).equalTo("parentid", 0).findAll();
        for (AppAreaData item : results1) {
            provinceModel = new ProvinceModel();
            provinceModel.setName(item.getAreaname());
            provinceModel.setCityList(new ArrayList<CityModel>());
            RealmResults<AppAreaData> results_city =
                    mRealm.where(AppAreaData.class).equalTo("parentid", item.getAreaid()).findAll();
            for (AppAreaData item_city : results_city) {
                cityModel = new CityModel();
                cityModel.setName(item_city.getAreaname());
                cityModel.setDistrictList(new ArrayList<DistrictModel>());
                RealmResults<AppAreaData> results_district =
                        mRealm.where(AppAreaData.class).equalTo("parentid", item_city.getAreaid()).findAll();
                for (AppAreaData item_district : results_district) {
                    districtModel = new DistrictModel();
                    districtModel.setName(item_district.getAreaname());
                    districtModel.setZipcode("000000");
                    cityModel.getDistrictList().add(districtModel);
                }
                provinceModel.getCityList().add(cityModel);
            }

            provinceList.add(provinceModel);
        }
        return provinceList;
    }
}
