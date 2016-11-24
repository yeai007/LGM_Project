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
 * 创建时间：2016/8/2 8:29
 * 修改人：whisper
 * 修改时间：2016/8/2 8:29
 * 修改备注：
 */
public class SendWords extends JsonBase {
    public String UserId;
    public String WordsStr;
    String TAG = "SendWords";
    public String Type;

    public SendWords() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "send_Words.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {

            bRet = true;
            dataMessage.arg1 =Integer.parseInt(outJsonObject.getString("result"));
            Log.e(TAG, "ParsReturnData: " + dataMessage.arg1);
        } else {
            bRet = false;
            dataMessage.arg1 = 0;
        }
        return bRet;
    }

    @Override
    public void PacketData() {
        try {
            opt_map.put("UserId", UserId);
            Log.e(TAG, "PacketData: " + WordsStr);
            opt_map.put("WordsStr", WordsStr);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
