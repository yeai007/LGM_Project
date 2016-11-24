package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.lgm.utils.DateTools;

import org.json.JSONException;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/2 8:29
 * 修改人：whisper
 * 修改时间：2016/8/2 8:29
 * 修改备注：
 */
public class AddUserVariety extends JsonBase {
    public String UserId;
    public String VarietyId;
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public AddUserVariety() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "addUserVariety.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {
            String aa = outJsonObject.getString("result");
            bRet = true;
            dataMessage.arg1 = 2;
            dataMessage.obj = aa;
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
            opt_map.put("UserId", UserId);
            opt_map.put("VarietyId", VarietyId);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
