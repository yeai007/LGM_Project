package com.hopeofseed.hopeofseed.JNXData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/2 16:34
 * 修改人：whisper
 * 修改时间：2016/8/2 16:34
 * 修改备注：
 */
@RealmClass
public class UserData extends RealmObject {



    /**
     * user_id : 35
     * user_name : 15628801370
     * password : 123456
     * nickname : 小狐狸
     * user_mobile : 0
     * user_email :
     * createtime : 2016-09-27 07:10:33
     * user_permation : 0
     * user_role : 1
     * user_field : 点击添加领域
     * user_role_id : 1
     */
    @PrimaryKey
    private int user_id;
    private String user_name;
    private String password;
    private String nickname;
    private String user_mobile;
    private String user_email;
    private String createtime;
    private String user_permation;
    private String user_role;
    private String user_field;
    private String user_role_id;
    private int iscurrent;
    /**
     * UserAvatar : 675e8da40952fcbfa352783e9145248b.jpg
     */

    private String UserAvatar;

    public int getIsCurrent() {
        return iscurrent;
    }

    public void setIsCurrent(int iscurrent) {
        this.iscurrent = iscurrent;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUserAvatar() {
        return UserAvatar;
    }

    public void setUserAvatar(String UserAvatar) {
        this.UserAvatar = UserAvatar;
    }
}
