package com.hopeofseed.hopeofseed.Data;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Http.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/2 8:30
 * 修改人：whisper
 * 修改时间：2016/8/2 8:30
 * 修改备注：
 */
public abstract class JsonBase {
    private static final String TAG = "JsonBase";
    /**
     * 日志标志
     */
    public String LogTAG = "JSONBASE";
    /**
     * 操作符
     */
    public static String optString = "";
    /**
     * 接口地址定义
     */

    public String interfaceUrl = Const.BASE_URL;
    /**
     * 参数
     */
    public HashMap<String, String> opt_map = new HashMap<>();
    /**
     * 参数
     */
    public HashMap<String, File> opt_map_file = new HashMap<>();
    /**
     * 如果是线程模式下， 通过dataHandler发送返回数据,dataMessage中包含了返回数据
     */
    public Message dataMessage = null;
    /**
     * 操作参数数据
     */
    public JSONObject optData;
    /**
     * 传入JSONObject
     */
    public JSONObject inJsonObject;
    /**
     * 输出JSONObject
     */
    public JSONObject outJsonObject;
    /**
     * 网络返回JSON字符串
     */
    public String recvString = "";
    /**
     * 数据传递Handler
     */
    public Handler dataHandler = null;
    /**
     * 输出JSONArray
     */
    public JSONArray outJsonArray;
/*************************************************/
    /**
     * 执行获取数据操作，使用 <br>
     * Const.JSON_INTERFACE_URL <br>
     * 作为数据访问路径
     *
     * @return 是否获取成功
     */
    public boolean RunData() {
        return RunData(interfaceUrl);
    }

    /**
     * 执行获取数据操作，使用 <br>
     * Const.JSON_INTERFACE_URL <br>
     * 作为数据访问路径
     *
     * @return 是否获取成功
     */
    public boolean UploadImg() {
        return UploadImg(interfaceUrl);
    }
    /***********************************************/


    /**
     * 解析返回数据 如果是线程模式下， 通过dataHandler发送返回数据, 这时需要ParsReturnData中为dataMessage赋值
     *
     * @return
     */
    public abstract boolean ParsReturnData() throws JSONException;

    public JsonBase(String opt) {
        optString = opt;
        inJsonObject = new JSONObject();
        optData = new JSONObject();
        try {
            inJsonObject.put("optstring", optString);
        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }
    }

    /**
     * 包装待发送数据包
     */
    public abstract void PacketData();

    public String end = "\r\n";
    public String twoHyphens = "--";
    public String boundary = "*****";

    // 普通字符串数据
    @SuppressWarnings("deprecation")
    private void writeStringParams(Map<String, String> params,
                                   DataOutputStream ds) throws Exception {
        Set<String> keySet = params.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = params.get(name);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"" + end);
            ds.writeBytes(end);
            ds.writeBytes(URLEncoder.encode(value) + end);
        }
    }

    /**
     * 执行获取数据操作
     *
     * @param strHttp 需要访问的Json数据接口地址
     * @return boolean 是否获取成功
     */
    public boolean RunData(String strHttp) {
        boolean ret = false;
        try {
            String content = "";
            PacketData();
            // 通过HTTP获取数据
            Log.d(LogTAG, "待发数据：" + opt_map.toString());
            recvString = HttpConnection.SendPostList(strHttp, opt_map);
            Log.d(LogTAG, "返回数据：" + recvString);
            if (recvString.indexOf("error:") >= 0) {
                dataMessage = new Message();
                dataMessage.arg1 = 1;
                dataMessage.obj = recvString;
                if (dataMessage != null && dataHandler != null) {
                    dataHandler.sendMessage(dataMessage);
                }
                // ret=false;
                Log.e(LogTAG, recvString);
                return false;
            } else {
                try {
                    outJsonObject = new JSONObject();
                    outJsonArray = new JSONArray(recvString);
                    outJsonObject.put("result", recvString);
                } catch (Exception e) {
                    outJsonObject = new JSONObject();
                    outJsonObject.put("result", recvString);
                }
                Log.d(LogTAG, recvString);
                ret = true;
            }
            if (ret) {
                ret = ParsReturnData();
            }
            // 如果是线程模式下，通过dataHandler发送返回数据,这时需要ParsReturnData中为dataMessage赋值
            if (dataMessage != null && dataHandler != null) {
                dataHandler.sendMessage(dataMessage);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(LogTAG, e.getMessage());
        }
        return ret;
    }

    /**
     * 执行获取数据操作
     *
     * @param strHttp 需要访问的Json数据接口地址
     * @return boolean 是否获取成功
     */
    public boolean UploadImg(String strHttp) {
        boolean ret = false;
        try {
            String content = "";
            PacketData();
            // 通过HTTP获取数据
            Log.d(LogTAG, "待发数据：" + opt_map.toString());
            recvString = HttpConnection.uploadImgAndPara(strHttp, opt_map, opt_map_file);
            Log.d(LogTAG, "返回数据：" + recvString);
            if (recvString.indexOf("error:") >= 0) {
                dataMessage = new Message();
                dataMessage.arg1 = 1;
                dataMessage.obj = recvString;
                if (dataMessage != null && dataHandler != null) {
                    dataHandler.sendMessage(dataMessage);
                }
                // ret=false;
                Log.e(LogTAG, recvString);
                return false;
            } else {
                try {
                    outJsonObject = new JSONObject();
                    outJsonArray = new JSONArray(recvString);
                    outJsonObject.put("result", recvString);
                } catch (Exception e) {
                    outJsonObject = new JSONObject();
                    outJsonObject.put("result", recvString);
                }
                Log.d(LogTAG, recvString);
                ret = true;
            }
            if (ret) {
                ret = ParsReturnData();
            }
            // 如果是线程模式下，通过dataHandler发送返回数据,这时需要ParsReturnData中为dataMessage赋值
            if (dataMessage != null && dataHandler != null) {
                dataHandler.sendMessage(dataMessage);
            }
        } catch (Exception e) {
            // TODO: handle exception
           //
            // Log.e(LogTAG, e.getMessage());
        }
        return ret;
    }


}

