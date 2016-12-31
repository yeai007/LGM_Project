package com.hopeofseed.hopeofseed.JNXData;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/7 16:24
 * 修改人：whisper
 * 修改时间：2016/12/7 16:24
 * 修改备注：
 */
public class NotifyDataNorealm implements Parcelable {

    /**
     * NotifyImage : null
     * NotifyURL : null
     * NotifyTitle : 系统通知
     * NotifyToClass : null
     * NotifyType : 1
     * NotifyShowContent : 您有新的系统通知
     * NotifyContent : 您有新的系统通知
     * NotifyId : 00000000000000000001
     * NotifyShowTitle : 种愿
     * NotifyIsRead : 0
     */

    private String NotifyImage;
    private String NotifyURL;
    private String NotifyTitle;
    private String NotifyToClass;
    private String NotifyType;
    private String NotifyShowContent;
    private String NotifyContent;
    private String NotifyId;
    private String NotifyShowTitle;
    private String NotifyIsRead;
    /**
     * NotifyCreateTime : 2016-12-07 16:50:32
     */

    private String NotifyCreateTime;
    /**
     * NotifyData :
     */

    private String NotifyData;
    private String NotifyFromUser;
    private String NotifyToUser;
    /**
     * NotifyFromUserName : 种愿种讯
     */

    private String NotifyFromUserName;

    public String getNotifyFromUser() {
        return NotifyFromUser;
    }

    public void setNotifyFromUser(String NotifyFromUser) {
        this.NotifyFromUser = NotifyFromUser;
    }

    public String getNotifyToUser() {
        return NotifyToUser;
    }

    public void setNotifyToUser(String NotifyToUser) {
        this.NotifyToUser = NotifyToUser;
    }
    public String getNotifyImage() {
        return NotifyImage;
    }

    public void setNotifyImage(String NotifyImage) {
        this.NotifyImage = NotifyImage;
    }

    public String getNotifyURL() {
        return NotifyURL;
    }

    public void setNotifyURL(String NotifyURL) {
        this.NotifyURL = NotifyURL;
    }

    public String getNotifyTitle() {
        return NotifyTitle;
    }

    public void setNotifyTitle(String NotifyTitle) {
        this.NotifyTitle = NotifyTitle;
    }

    public String getNotifyToClass() {
        return NotifyToClass;
    }

    public void setNotifyToClass(String NotifyToClass) {
        this.NotifyToClass = NotifyToClass;
    }

    public String getNotifyType() {
        return NotifyType;
    }

    public void setNotifyType(String NotifyType) {
        this.NotifyType = NotifyType;
    }

    public String getNotifyShowContent() {
        return NotifyShowContent;
    }

    public void setNotifyShowContent(String NotifyShowContent) {
        this.NotifyShowContent = NotifyShowContent;
    }

    public String getNotifyContent() {
        return NotifyContent;
    }

    public void setNotifyContent(String NotifyContent) {
        this.NotifyContent = NotifyContent;
    }

    public String getNotifyId() {
        return NotifyId;
    }

    public void setNotifyId(String NotifyId) {
        this.NotifyId = NotifyId;
    }

    public String getNotifyShowTitle() {
        return NotifyShowTitle;
    }

    public void setNotifyShowTitle(String NotifyShowTitle) {
        this.NotifyShowTitle = NotifyShowTitle;
    }

    public String getNotifyIsRead() {
        return NotifyIsRead;
    }

    public void setNotifyIsRead(String NotifyIsRead) {
        this.NotifyIsRead = NotifyIsRead;
    }

    public String getNotifyCreateTime() {
        return NotifyCreateTime;
    }

    public void setNotifyCreateTime(String NotifyCreateTime) {
        this.NotifyCreateTime = NotifyCreateTime;
    }

    public NotifyDataNorealm() {
    }

    public String getNotifyData() {
        return NotifyData;
    }

    public void setNotifyData(String NotifyData) {
        this.NotifyData = NotifyData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.NotifyImage);
        dest.writeString(this.NotifyURL);
        dest.writeString(this.NotifyTitle);
        dest.writeString(this.NotifyToClass);
        dest.writeString(this.NotifyType);
        dest.writeString(this.NotifyShowContent);
        dest.writeString(this.NotifyContent);
        dest.writeString(this.NotifyId);
        dest.writeString(this.NotifyShowTitle);
        dest.writeString(this.NotifyIsRead);
        dest.writeString(this.NotifyCreateTime);
        dest.writeString(this.NotifyData);
        dest.writeString(this.NotifyFromUser);
        dest.writeString(this.NotifyToUser);
    }

    protected NotifyDataNorealm(Parcel in) {
        this.NotifyImage = in.readString();
        this.NotifyURL = in.readString();
        this.NotifyTitle = in.readString();
        this.NotifyToClass = in.readString();
        this.NotifyType = in.readString();
        this.NotifyShowContent = in.readString();
        this.NotifyContent = in.readString();
        this.NotifyId = in.readString();
        this.NotifyShowTitle = in.readString();
        this.NotifyIsRead = in.readString();
        this.NotifyCreateTime = in.readString();
        this.NotifyData = in.readString();
        this.NotifyFromUser = in.readString();
        this.NotifyToUser = in.readString();
    }

    public static final Creator<NotifyDataNorealm> CREATOR = new Creator<NotifyDataNorealm>() {
        @Override
        public NotifyDataNorealm createFromParcel(Parcel source) {
            return new NotifyDataNorealm(source);
        }

        @Override
        public NotifyDataNorealm[] newArray(int size) {
            return new NotifyDataNorealm[size];
        }
    };

    public String getNotifyFromUserName() {
        return NotifyFromUserName;
    }

    public void setNotifyFromUserName(String NotifyFromUserName) {
        this.NotifyFromUserName = NotifyFromUserName;
    }
}
