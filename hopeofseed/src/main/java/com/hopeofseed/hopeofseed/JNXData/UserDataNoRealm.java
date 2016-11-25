package com.hopeofseed.hopeofseed.JNXData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 18:35
 * 修改人：whisper
 * 修改时间：2016/10/23 18:35
 * 修改备注：
 */
public class UserDataNoRealm implements Parcelable {

    /**
     * user_id : 36
     * user_name : 15628801370
     * nickname : 用户01370
     * user_mobile : 0
     * user_email :
     * createtime : 2016-10-20 06:34:19
     * user_permation : 0
     * user_role : 1
     * user_field : 水稻
     * user_role_id : 2
     * fllowed_count : 1
     * been_fllowed_count : 0
     */
    /**
     * UserProvince :
     * UserCity :
     * UserZone :
     * UserAddressDetail :
     * UserSex :
     * UserLastLat :
     * UserLastLon :
     */
    private String user_id;
    private String user_name;
    private String nickname;
    private String user_mobile;
    private String user_email;
    private String createtime;
    private String user_permation;
    private String user_role;
    private String user_field;
    private String user_role_id;
    private String UserProvince="";
    private String UserCity="";
    private String UserZone="";
    private String UserAddressDetail="";
    private String UserSex;
    private String UserLastLat;
    private String UserLastLon;
    private String fllowed_count;
    private String been_fllowed_count;



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUser_permation() {
        return user_permation;
    }

    public void setUser_permation(String user_permation) {
        this.user_permation = user_permation;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_field() {
        return user_field;
    }

    public void setUser_field(String user_field) {
        this.user_field = user_field;
    }

    public String getUser_role_id() {
        return user_role_id;
    }

    public void setUser_role_id(String user_role_id) {
        this.user_role_id = user_role_id;
    }

    public String getFllowed_count() {
        return fllowed_count;
    }

    public void setFllowed_count(String fllowed_count) {
        this.fllowed_count = fllowed_count;
    }

    public String getBeen_fllowed_count() {
        return been_fllowed_count;
    }

    public void setBeen_fllowed_count(String been_fllowed_count) {
        this.been_fllowed_count = been_fllowed_count;
    }
    public String getUserProvince() {
        return UserProvince;
    }

    public void setUserProvince(String UserProvince) {
        this.UserProvince = UserProvince;
    }

    public String getUserCity() {
        return UserCity;
    }

    public void setUserCity(String UserCity) {
        this.UserCity = UserCity;
    }

    public String getUserZone() {
        return UserZone;
    }

    public void setUserZone(String UserZone) {
        this.UserZone = UserZone;
    }

    public String getUserAddressDetail() {
        return UserAddressDetail;
    }

    public void setUserAddressDetail(String UserAddressDetail) {
        this.UserAddressDetail = UserAddressDetail;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String UserSex) {
        this.UserSex = UserSex;
    }

    public String getUserLastLat() {
        return UserLastLat;
    }

    public void setUserLastLat(String UserLastLat) {
        this.UserLastLat = UserLastLat;
    }

    public String getUserLastLon() {
        return UserLastLon;
    }

    public void setUserLastLon(String UserLastLon) {
        this.UserLastLon = UserLastLon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.user_name);
        dest.writeString(this.nickname);
        dest.writeString(this.user_mobile);
        dest.writeString(this.user_email);
        dest.writeString(this.createtime);
        dest.writeString(this.user_permation);
        dest.writeString(this.user_role);
        dest.writeString(this.user_field);
        dest.writeString(this.user_role_id);
        dest.writeString(this.UserProvince);
        dest.writeString(this.UserCity);
        dest.writeString(this.UserZone);
        dest.writeString(this.UserAddressDetail);
        dest.writeString(this.UserSex);
        dest.writeString(this.UserLastLat);
        dest.writeString(this.UserLastLon);
        dest.writeString(this.fllowed_count);
        dest.writeString(this.been_fllowed_count);
    }

    public UserDataNoRealm() {
    }

    protected UserDataNoRealm(Parcel in) {
        this.user_id = in.readString();
        this.user_name = in.readString();
        this.nickname = in.readString();
        this.user_mobile = in.readString();
        this.user_email = in.readString();
        this.createtime = in.readString();
        this.user_permation = in.readString();
        this.user_role = in.readString();
        this.user_field = in.readString();
        this.user_role_id = in.readString();
        this.UserProvince = in.readString();
        this.UserCity = in.readString();
        this.UserZone = in.readString();
        this.UserAddressDetail = in.readString();
        this.UserSex = in.readString();
        this.UserLastLat = in.readString();
        this.UserLastLon = in.readString();
        this.fllowed_count = in.readString();
        this.been_fllowed_count = in.readString();
    }

    public static final Creator<UserDataNoRealm> CREATOR = new Creator<UserDataNoRealm>() {
        @Override
        public UserDataNoRealm createFromParcel(Parcel source) {
            return new UserDataNoRealm(source);
        }

        @Override
        public UserDataNoRealm[] newArray(int size) {
            return new UserDataNoRealm[size];
        }
    };
}
