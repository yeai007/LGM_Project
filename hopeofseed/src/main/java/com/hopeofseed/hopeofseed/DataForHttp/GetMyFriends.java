package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.lgm.utils.DateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/16 11:07
 * 修改人：whisper
 * 修改时间：2016/9/16 11:07
 * 修改备注：
 */
public class GetMyFriends extends JsonBase {
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public ArrayList<UserData> retRows = new ArrayList<>();

    public GetMyFriends() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "GetMyFriends.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonArray != null) {
            for (int i = 0; i < outJsonArray.length(); i++) {
                JSONObject js_result = outJsonArray.getJSONObject(i);
                UserData userData = new UserData();
                userData.setUser_id(Integer.parseInt(js_result.getString("user_id")));
                userData.setUser_name(js_result.getString("user_name"));
                userData.setPassword(js_result.getString("password"));
                userData.setNickname(js_result.getString("nickname"));
                userData.setUser_mobile(js_result.getString("user_mobile"));
                userData.setUser_email(js_result.getString("user_email"));
                userData.setCreatetime(js_result.getString("createtime"));
                userData.setUser_permation(js_result.getString("user_permation"));
                userData.setUser_role(js_result.getString("user_role"));
                retRows.add(userData);
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