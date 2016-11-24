package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.Data.Const;

import org.json.JSONException;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/24 11:17
 * 修改人：whisper
 * 修改时间：2016/9/24 11:17
 * 修改备注：
 */
public class DeleteGroup extends JsonBase {
    public String UserId;
    String TAG = "DataForHttp";
    public String GroupId;

    public DeleteGroup() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "deleteGroup.php";
    }

    @Override
    public boolean ParsReturnData() {
        boolean bRet = false;
        super.dataMessage = new Message();
        try {
            if (outJsonObject != null) {
                Log.e(TAG, "ParsReturnData: " + outJsonObject.getString("result"));
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
            opt_map.put("GroupId", GroupId);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}