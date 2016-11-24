package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.lgm.utils.DateTools;

import org.json.JSONException;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/9 9:44
 * 修改人：whisper
 * 修改时间：2016/10/9 9:44
 * 修改备注：
 */
public class AddNewYieldData extends JsonBase {
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public String YieldVariety, YieldSum, YieldArea, YieldYield, YieldEssay, YieldImg;

    public AddNewYieldData() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "AddNewYieldData.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {
            String result = outJsonObject.getString("result");
            if (Integer.parseInt(result) > 0) {
                bRet = true;
                dataMessage.arg1 = 2;
            } else {
                bRet = false;
            }
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
            opt_map.put("YieldVariety", YieldVariety);
            opt_map.put("YieldSum", YieldSum);
            opt_map.put("YieldArea", YieldArea);
            opt_map.put("YieldYield", YieldYield);
            opt_map.put("YieldEssay", YieldEssay);
            opt_map.put("YieldCreateUser", String.valueOf(Const.currentUser.user_id));
            opt_map.put("YieldImg", YieldImg);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}