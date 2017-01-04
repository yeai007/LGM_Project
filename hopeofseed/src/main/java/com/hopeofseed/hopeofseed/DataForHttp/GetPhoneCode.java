package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.lgm.utils.DateTools;
import com.lgm.utils.ObjectUtil;

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
public class GetPhoneCode extends JsonBase {
    private static String ACCOUNT = "cf_smartwn";
    private static String password = "beb3ef9f7f0421b9d15155cb7bdd0757";
    public String mobile;
    public String content;
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";

    public String Type;
    public ArrayList<NewsData> retRows = new ArrayList<>();

    public GetPhoneCode() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "getPhoneCode.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {
            Log.e(TAG, "ParsReturnData: "+outJsonObject.toString());
            String aa = outJsonObject.getString("result");
            JSONObject js = new JSONObject(ObjectUtil.xml2JSON(aa));
            JSONObject js_result = js.getJSONObject("SubmitResult");
            Log.e(TAG, "ParsReturnData: " + js_result + js_result.getString("msg"));
            bRet = true;
            dataMessage.arg1 = 2;
            dataMessage.obj = js_result.getString("msg");
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
            opt_map.put("mobile", mobile);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
