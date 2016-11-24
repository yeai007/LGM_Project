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
 * 创建时间：2016/9/17 16:09
 * 修改人：whisper
 * 修改时间：2016/9/17 16:09
 * 修改备注：
 */
public class AddGroupMember extends JsonBase {
    public String AddUsers, GroupId;
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";

    public AddGroupMember() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "addGroupMember.php";
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
            opt_map.put("AddUsers", AddUsers);
            opt_map.put("GroupId", String.valueOf(Integer.parseInt(GroupId)));
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}