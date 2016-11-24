package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.Data.Const;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/2 8:29
 * 修改人：whisper
 * 修改时间：2016/8/2 8:29
 * 修改备注：
 */
public class GetNews extends JsonBase {
    public String UserId;
    String TAG = "DataForHttp";
    public int classid;
    public ArrayList<NewsData> retRows = new ArrayList<>();

    public GetNews() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "get_News.php";
    }

    @Override
    public boolean ParsReturnData() {
        boolean bRet = false;
        super.dataMessage = new Message();

        if (outJsonArray != null) {
            Realm insertRealm = Realm.getDefaultInstance();
            RealmResults<NewsData> results_del = insertRealm.where(NewsData.class).findAll();
            insertRealm.beginTransaction();
            results_del.deleteAllFromRealm();
            insertRealm.commitTransaction();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<NewsData>>() {}.getType();
            retRows= gson.fromJson(outJsonArray.toString(),type);
            for(NewsData o:retRows){
                insertRealm.beginTransaction();
                NewsData newdata =insertRealm.copyToRealmOrUpdate(o);
                insertRealm.commitTransaction();
            }
            bRet = true;
            dataMessage.arg1 = 1;
        } else if (outJsonObject != null) {
            bRet = true;
            dataMessage.arg1 = 2;
        } else {
            bRet = false;
            dataMessage.arg1 = 0;
        }
        return bRet;
    }

    @Override
    public void PacketData() {
        try {
            opt_map.put("UserId", UserId);
            opt_map.put("classid", String.valueOf(classid));
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
