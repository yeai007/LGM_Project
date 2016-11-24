package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.lgm.utils.DateTools;
import org.json.JSONException;
import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/25 10:30
 * 修改人：whisper
 * 修改时间：2016/9/25 10:30
 * 修改备注：
 */
public class UpdateGroupInfo extends JsonBase {
    public String UserId;
    public String GroupId;
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public String GroupName;
    public String GroupDescription;

    public ArrayList<NewsData> retRows = new ArrayList<>();

    public UpdateGroupInfo() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "UpdateGroupInfo.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
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
            opt_map.put("GroupName", GroupName);
            opt_map.put("GroupDescription", GroupDescription);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
