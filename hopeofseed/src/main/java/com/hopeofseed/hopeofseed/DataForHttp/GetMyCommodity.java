package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.lgm.utils.DateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/27 11:59
 * 修改人：whisper
 * 修改时间：2016/9/27 11:59
 * 修改备注：
 */
public class GetMyCommodity extends JsonBase {
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public ArrayList<CommodityData> retRows = new ArrayList<>();

    public GetMyCommodity() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "GetMyCommodity.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonArray != null) {
            for (int i = 0; i < outJsonArray.length(); i++) {
                JSONObject js = outJsonArray.getJSONObject(i);
                CommodityData commodityData = new CommodityData();
                commodityData.setCommodityId(js.getString("CommodityId"));
                commodityData.setCommodityTitle(js.getString("CommodityTitle"));
                commodityData.setCommodityName(js.getString("CommodityName"));
                commodityData.setCommodityPrice(js.getString("CommodityPrice"));
                commodityData.setCreatetime(js.getString("CreateTime"));
                commodityData.setCommodityFlag(js.getString("CommodityFlag"));
                commodityData.setCommodityFlagTime(js.getString("CommodityFlagTime"));
                commodityData.setCommodityDescribe(js.getString("CommodityDescribe"));
                commodityData.setOwner(js.getString("Owner"));
                commodityData.setNewId(js.getString("NewId"));
                commodityData.setCommodityImgs(js.getString("CommodityImgs"));
                commodityData.setCommodityVariety(js.getString("CommodityVariety"));
                commodityData.setCommodityClass(js.getString("CommodityClass"));
                commodityData.setOwnerClass(js.getString("OwnerClass"));
                retRows.add(commodityData);
            }
            bRet = true;
            dataMessage.arg1 = 2;
            dataMessage.obj = retRows;
        } else {
            bRet = false;
            dataMessage.arg1 = 0;
            dataMessage.obj = "获取失败清稍后再试";
        }
        return bRet;
    }

    @Override
    public void PacketData() {
        try {
            opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}