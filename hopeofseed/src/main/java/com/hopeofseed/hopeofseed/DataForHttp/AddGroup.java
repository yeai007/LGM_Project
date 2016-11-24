package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.lgm.utils.DateTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/13 15:29
 * 修改人：whisper
 * 修改时间：2016/9/13 15:29
 * 修改备注：
 */
public class AddGroup extends JsonBase {
    public String GroupName;
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public String JpushGroupId;

    public AddGroup() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "addGroup.php";
    }

    @Override
    public boolean ParsReturnData() throws JSONException {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {
            JSONObject js = new JSONObject(outJsonObject.getString("result"));
            GroupData groupData = new GroupData();
            groupData.setGroupid(js.getString("groupid"));
            groupData.setGroupname(js.getString("groupname"));
            groupData.setMembersid(js.getString("membersid"));
            groupData.setCreatetime(js.getString("createtime"));
            groupData.setFlag(js.getString("flag"));
            groupData.setGrouplevel(js.getString("grouplevel"));
            groupData.setCreateuser(js.getString("createuser"));
            groupData.setCreateusername(js.getString("createusername"));
            groupData.setCreateusernickname(js.getString("createusernickname"));
            groupData.setJpushGroupId(js.getString("jpushgroupid"));

            bRet = true;
            dataMessage.arg1 = 2;
            dataMessage.obj = groupData;
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
            opt_map.put("UserName", String.valueOf(Const.currentUser.user_name));
            opt_map.put("NickName", String.valueOf(Const.currentUser.nickname));
            opt_map.put("GroupName", GroupName);
            opt_map.put("JpushGroupId", JpushGroupId);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}

