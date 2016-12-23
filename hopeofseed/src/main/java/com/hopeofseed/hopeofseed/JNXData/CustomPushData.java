package com.hopeofseed.hopeofseed.JNXData;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/23 9:26
 * 修改人：whisper
 * 修改时间：2016/12/23 9:26
 * 修改备注：
 */
public class CustomPushData {

    /**
     * CustomPushId : 0000000001
     * CustomPushName : 推送1
     * CustomPushSql : select user_id from app_user where user_id>1126
     * CustomPushFromUserId : 1118
     * CustomPushCreateTime : 2016-12-23 09:21:47
     * CustomPushFlag : 1
     */

    private String CustomPushId;
    private String CustomPushName;
    private String CustomPushFromUserId;
    private String CustomPushCreateTime;
    private String CustomPushFlag;

    public String getCustomPushId() {
        return CustomPushId;
    }

    public void setCustomPushId(String CustomPushId) {
        this.CustomPushId = CustomPushId;
    }

    public String getCustomPushName() {
        return CustomPushName;
    }

    public void setCustomPushName(String CustomPushName) {
        this.CustomPushName = CustomPushName;
    }

    public String getCustomPushFromUserId() {
        return CustomPushFromUserId;
    }

    public void setCustomPushFromUserId(String CustomPushFromUserId) {
        this.CustomPushFromUserId = CustomPushFromUserId;
    }

    public String getCustomPushCreateTime() {
        return CustomPushCreateTime;
    }

    public void setCustomPushCreateTime(String CustomPushCreateTime) {
        this.CustomPushCreateTime = CustomPushCreateTime;
    }

    public String getCustomPushFlag() {
        return CustomPushFlag;
    }

    public void setCustomPushFlag(String CustomPushFlag) {
        this.CustomPushFlag = CustomPushFlag;
    }
}
