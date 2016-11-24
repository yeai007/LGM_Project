package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.ZambiaData;
import com.lgm.utils.DateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/14 15:07
 * 修改人：whisper
 * 修改时间：2016/9/14 15:07
 * 修改备注：
 */
public class AddFriend extends JsonBase {

    public String UserId;
    public String AddUserId;
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public ArrayList<ZambiaData> retRows = new ArrayList<>();

    public AddFriend() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "AddNewFriend.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        try {
            if (outJsonArray != null) {
                for (int i = 0; i < outJsonArray.length(); i++) {
                    JSONObject js = outJsonArray.getJSONObject(i);
                    ZambiaData zambiaData = new ZambiaData();
                    zambiaData.setId(js.getString("id"));
                    zambiaData.setCreate_time(js.getString("create_time"));
                    zambiaData.setCreate_user_id(js.getString("create_user_id"));
                    zambiaData.setFlag(js.getString("flag"));
                    zambiaData.setNew_id(js.getString("new_id"));
                    retRows.add(zambiaData);
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
            opt_map.put("AddUserId", AddUserId);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
