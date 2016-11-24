package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/25 17:25
 * 修改人：whisper
 * 修改时间：2016/9/25 17:25
 * 修改备注：
 */
public class SendWordAndImg extends JsonBase {
    public String UserId;
    public String WordsStr;
    String TAG = "SendWords";
    public String Type;
    public ArrayList<String> images = new ArrayList<>();
    public SendWordAndImg() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "send_WordAndImg.php";
    }
    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {
            Log.e(TAG, "ParsReturnData: "+outJsonObject.getString("result"));
            JSONObject js_result = new JSONObject(outJsonObject.getString("result"));
            String result=js_result.getString("status");
            Log.e(TAG, "ParsReturnData: "+result);
            if(result.equals("0")) {
                bRet = true;
            }
            else
            {
                bRet = false;
            }

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
            opt_map.put("UserId", UserId);
            Log.e(TAG, "PacketData: " + WordsStr);
            opt_map.put("WordsStr", WordsStr);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
