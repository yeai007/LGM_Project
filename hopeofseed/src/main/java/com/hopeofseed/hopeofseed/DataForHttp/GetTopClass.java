package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.TopClass;
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
public class GetTopClass extends JsonBase {
    public String UserId;
    String TAG = "DataForHttp";

    public String Type;
    public ArrayList<TopClass> retRows = new ArrayList<>();

    public GetTopClass() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "get_topclass.php";
    }

    @Override
    public boolean ParsReturnData() {
        boolean bRet = false;
        super.dataMessage = new Message();
        try {
            if (outJsonArray != null) {
                for (int i = 0; i < outJsonArray.length(); i++) {
                    JSONObject js = outJsonArray.getJSONObject(i);
                    TopClass topClass = new TopClass();
                    topClass.setClassid(js.getString("classid"));
                    topClass.setClassname(js.getString("classname"));
                    topClass.setUser_id(js.getString("user_id"));
                    retRows.add(topClass);
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
