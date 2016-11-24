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
public class AddNewProblem extends JsonBase {
    public Long time = DateTools.getTimeMillis();
    String TAG = "DataForHttp";
    public String ProblemVariety, ProblemTitle, ProblemContent;

    public AddNewProblem() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "AddNewProblemData.php";
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

            opt_map.put("ProblemVariety", ProblemVariety);
            opt_map.put("ProblemTitle", ProblemTitle);
            opt_map.put("ProblemContent", ProblemContent);

            opt_map.put("CreateUser", String.valueOf(Const.currentUser.user_id));

        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}