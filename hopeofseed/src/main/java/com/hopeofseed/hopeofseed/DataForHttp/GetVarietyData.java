package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.JNXData.VarietyData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/2 8:29
 * 修改人：whisper
 * 修改时间：2016/8/2 8:29
 * 修改备注：
 */
public class GetVarietyData extends JsonBase {
    public String UserId;
    String TAG = "DataForHttp";

    public String Type;
    public ArrayList<VarietyData> retRows = new ArrayList<>();

    public GetVarietyData() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "s.php";
    }

    @Override
    public boolean ParsReturnData() {
        boolean bRet = false;
        super.dataMessage = new Message();
        try {
            if (outJsonArray != null) {
                for (int i = 0; i < outJsonArray.length(); i++) {
                    JSONObject js = outJsonArray.getJSONObject(i);
                    VarietyData varietyData = new VarietyData();
                    varietyData.setVarietyid(js.getString("varietyid"));
                    varietyData.setVarietyname(js.getString("varietyname"));
                    varietyData.setVarietyclassid(js.getString("varietyclassid"));
                    varietyData.setArrvarietyid(js.getString("arrvarietyid"));
                    varietyData.setCreatetime(js.getString("createtime"));
                    varietyData.setHotlevel(js.getString("hotlevel"));
                    varietyData.setHotorder(js.getString("hotorder"));
                    varietyData.setArrvarietyname(js.getString("arrvarietyname"));
                    varietyData.setBrand_ids(js.getString("brand_ids"));
                    varietyData.setBrand_names(js.getString("brand_names"));
                    retRows.add(varietyData);
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
        } catch (JSONException e) {
            bRet = false;
            dataMessage.arg1 = 0;
            e.printStackTrace();
        }
        return bRet;
    }

    @Override
    public void PacketData() {
        try {
            opt_map.put("UserId", UserId);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
