package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.lgm.utils.DateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/18 15:42
 * 修改人：whisper
 * 修改时间：2016/9/18 15:42
 * 修改备注：
 */
public class AddNewCommodity extends JsonBase {
    public String commodity_title, commodity_name, commodity_price, commodity_describe, commodity_variety, commodity_class, Variety_1, Variety_2;
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public CommodityData comodityData = new CommodityData();
    public ArrayList<String> images = new ArrayList<>();

    public AddNewCommodity() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "addCommodity.php";

    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {
            Log.e(TAG, "ParsReturnData: " + outJsonObject.getString("result"));
            JSONObject js_result = new JSONObject(outJsonObject.getString("result"));
            String result = js_result.getString("status");
            Log.e(TAG, "ParsReturnData: " + result);
            bRet = result.equals("0");

            Log.e(TAG, "ParsReturnData: " + dataMessage.arg1);
        } else {
            bRet = false;
            dataMessage.arg1 = 0;
        }
        return bRet;
    }

    @Override
    public void PacketData() {
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).equals("add")) {
            } else {
                opt_map_file.put(String.valueOf(i), new File(images.get(i)));
            }
        }
        try {
            opt_map.put("commodity_title", commodity_title);
            opt_map.put("commodity_name", commodity_name);
            opt_map.put("commodity_price", commodity_price);
            opt_map.put("commodity_describe", commodity_describe);
            opt_map.put("commodity_variety", commodity_variety);
            opt_map.put("commodity_class", commodity_class);
            opt_map.put("userid", String.valueOf(Const.currentUser.user_id));
            opt_map.put("OwnerClass", Const.currentUser.user_role);
            opt_map.put("Variety_1", Variety_1);
            opt_map.put("Variety_2", Variety_2);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }

}
