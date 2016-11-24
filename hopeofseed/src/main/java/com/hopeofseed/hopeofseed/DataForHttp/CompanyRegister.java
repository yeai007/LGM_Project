package com.hopeofseed.hopeofseed.DataForHttp;

import android.os.Message;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Data.JsonBase;
import com.hopeofseed.hopeofseed.JNXData.UserData;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/27 16:08
 * 修改人：whisper
 * 修改时间：2016/9/27 16:08
 * 修改备注：
 */
public class CompanyRegister extends JsonBase {
    public String UserName, PassWord, PhoneCode, CompanyName, UserClass, Province, City, County;
    String TAG = "DataForHttp";
    public String Type;
    public ArrayList<String> images = new ArrayList<>();
    public CompanyRegister() {
        super(optString);
        interfaceUrl = Const.BASE_URL + "RegCompanyUser.php";
    }

    @Override
    public boolean ParsReturnData() {
        boolean bRet = false;
        super.dataMessage = new Message();
        if (outJsonObject != null) {

            try {
                if (outJsonObject.getString("result").equals("用户未查询到")) {
                    dataMessage.arg1 = 2;
                } else {
                    Realm myRealm = Realm.getDefaultInstance();
                    JSONObject js_result = new JSONObject(outJsonObject.getString("result"));
                    RealmResults<UserData> results1 =
                            myRealm.where(UserData.class).equalTo("user_id", Integer.parseInt(js_result.getString("user_id"))).findAll();
                    if (results1.size() == 0) {
                        Log.d("results1", "写入");
                        myRealm.beginTransaction();
                        UserData userData = myRealm.createObject(UserData.class);
                        userData.setUser_id(Integer.parseInt(js_result.getString("user_id")));
                        userData.setUser_name(js_result.getString("user_name"));
                        userData.setPassword(js_result.getString("password"));
                        userData.setNickname(js_result.getString("nickname"));
                        userData.setUser_mobile(js_result.getString("user_mobile"));
                        userData.setUser_email(js_result.getString("user_email"));
                        userData.setCreatetime(js_result.getString("createtime"));
                        userData.setUser_permation(js_result.getString("user_permation"));
                        userData.setUser_role(js_result.getString("user_role"));
                        userData.setIsCurrent(1);
                        Const.currentUser.user_id = userData.getUser_id();
                        Const.currentUser.user_name = userData.getUser_name();
                        Const.currentUser.password = userData.getPassword();
                        Const.currentUser.nickname = userData.getNickname();
                        Const.currentUser.user_mobile = userData.getUser_mobile();
                        Const.currentUser.user_email = userData.getUser_email();
                        Const.currentUser.createtime = userData.getCreatetime();
                        Const.currentUser.user_permation = userData.getUser_permation();
                        Const.currentUser.user_role = userData.getUser_role();
                        myRealm.commitTransaction();
                    } else {
                        Log.d("results1", "更新");
                        myRealm.beginTransaction();
                        UserData userData = myRealm.where(UserData.class).equalTo("user_id", Integer.parseInt(js_result.getString("user_id"))).findFirst();
                        userData.setUser_id(Integer.parseInt(js_result.getString("user_id")));
                        userData.setUser_name(js_result.getString("user_name"));
                        userData.setPassword(js_result.getString("password"));
                        userData.setNickname(js_result.getString("nickname"));
                        userData.setUser_mobile(js_result.getString("user_mobile"));
                        userData.setUser_email(js_result.getString("user_email"));
                        userData.setCreatetime(js_result.getString("createtime"));
                        userData.setUser_permation(js_result.getString("user_permation"));
                        userData.setUser_role(js_result.getString("user_role"));
                        userData.setIsCurrent(1);
                        myRealm.commitTransaction();
                        RealmResults<UserData> results_update =
                                myRealm.where(UserData.class).equalTo("user_id", Integer.parseInt(js_result.getString("user_id"))).findAll();
                        for (UserData c : results_update) {
                            Const.currentUser.user_id = c.getUser_id();
                            Const.currentUser.user_name = c.getUser_name();
                            Const.currentUser.password = c.getPassword();
                            Const.currentUser.nickname = c.getNickname();
                            Const.currentUser.user_mobile = c.getUser_mobile();
                            Const.currentUser.user_email = c.getUser_email();
                            Const.currentUser.createtime = c.getCreatetime();
                            Const.currentUser.user_permation = c.getUser_permation();
                            Const.currentUser.user_role = c.getUser_role();
                        }

                    }
                    dataMessage.arg1 = 1;
                }
                bRet = true;

            } catch (Exception e) {
                bRet = false;
                dataMessage = new Message();
                dataMessage.arg1 = 0;
                Log.e(LogTAG, e.getMessage());
            }
        }
        return bRet;
    }

    @Override
    public void PacketData() {
        Log.e(TAG, "PacketData: "+images.size() );
        for (int i = 0; i < images.size(); i++) {
                opt_map_file.put(String.valueOf(i), new File(images.get(i)));
        }
        try {
            opt_map.put("UserName", UserName);
            opt_map.put("PassWord", PassWord);
            opt_map.put("PhoneCode", PhoneCode);
            opt_map.put("CompanyName", CompanyName);
            opt_map.put("UserClass", UserClass);
            opt_map.put("Province", Province);
            opt_map.put("City", City);
            opt_map.put("County", County);

        } catch (Exception e) {
            Log.e(LogTAG, e.getMessage());
        }

    }
}






