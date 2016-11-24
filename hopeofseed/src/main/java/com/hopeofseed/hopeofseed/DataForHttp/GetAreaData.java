package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.AreaData;

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
public class GetAreaData extends JsonBase {
    String TAG = "DataForHttp";

    public String Type;
    public ArrayList<AreaData> retRows = new ArrayList<>();

    public GetAreaData() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "getAreaData.php";
    }

    @Override
    public boolean ParsReturnData() {
        boolean bRet = false;
        super.dataMessage = new Message();
        try {
            if (outJsonArray != null) {
                Log.e(TAG, "ParsReturnData: "+1);
                for (int i = 0; i < outJsonArray.length(); i++) {
                    JSONObject js = outJsonArray.getJSONObject(i);
                    AreaData areaData = new AreaData();
                    areaData.setAreaid(js.getString("areaid"));
                    areaData.setAreaname(js.getString("areaname"));
                    areaData.setParentid(js.getString("parentid"));
                    areaData.setArrparentid(js.getString("arrparentid"));
                    areaData.setChild(js.getString("child"));
                    areaData.setArrchildid(js.getString("arrchildid"));
                    areaData.setListorder(js.getString("listorder"));
                    retRows.add(areaData);
                }
                bRet = true;
                dataMessage.arg1 = 1;
            } else if (outJsonObject != null) {
                outJsonArray= outJsonObject.getJSONArray("result");
                Log.e(TAG, "ParsReturnData: "+2);
                bRet = true;
                dataMessage.arg1 = 2;
            } else {
                Log.e(TAG, "ParsReturnData: "+3);
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
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}
