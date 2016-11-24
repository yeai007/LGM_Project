package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/26 17:27
 * 修改人：whisper
 * 修改时间：2016/9/26 17:27
 * 修改备注：
 */
public class GetMyFollow extends JsonBase {
    public String UserId;
    String TAG = "DataForHttp";

    public String Type;
    public ArrayList<UserData> retRows = new ArrayList<>();

    public GetMyFollow() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "getMyFollowed.php";
    }

    @Override
    public boolean ParsReturnData() {
        boolean bRet = false;
        super.dataMessage = new Message();
        try {
            if (outJsonArray != null) {
                for (int i = 0; i < outJsonArray.length(); i++) {
                    JSONObject js = outJsonArray.getJSONObject(i);
                    UserData newsData = new UserData();
                    newsData.setUser_id(Integer.parseInt(js.getString("user_id")));
                    newsData.setUser_name(js.getString("user_name"));
                    newsData.setPassword(js.getString("password"));
                    newsData.setNickname(js.getString("nickname"));
                    newsData.setUser_mobile(js.getString("user_mobile"));
                    newsData.setUser_email(js.getString("user_email"));
                    newsData.setCreatetime(js.getString("createtime"));
                    newsData.setUser_permation(js.getString("user_permation"));
                    newsData.setUser_role(js.getString("user_role"));
                    retRows.add(newsData);
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
