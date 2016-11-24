package com.hopeofseed.hopeofseed.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * bean 类
 * Created by yetwish on 2015-05-11
 */

public class Bean extends RealmObject {

    /**
     * TagID : 0001
     * TagName : 玉米
     * SecondName :
     * TagClass : 1
     * TagLevel :
     * Memo :
     * HotLevel : 0000000001
     * HotOrder : 0000000007
     */
    @PrimaryKey
    private int TagID;
    private String TagName;
    private String SecondName;
    private String TagClass;
    private String TagLevel;
    private String Memo;
    private String HotLevel;
    private String HotOrder;
    /**
     * TagAvatar : 111
     */

    private String TagAvatar;

    public int getTagID() {
        return TagID;
    }

    public void setTagID(int TagID) {
        this.TagID = TagID;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public void setSecondName(String SecondName) {
        this.SecondName = SecondName;
    }

    public String getTagClass() {
        return TagClass;
    }

    public void setTagClass(String TagClass) {
        this.TagClass = TagClass;
    }

    public String getTagLevel() {
        return TagLevel;
    }

    public void setTagLevel(String TagLevel) {
        this.TagLevel = TagLevel;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String Memo) {
        this.Memo = Memo;
    }

    public String getHotLevel() {
        return HotLevel;
    }

    public void setHotLevel(String HotLevel) {
        this.HotLevel = HotLevel;
    }

    public String getHotOrder() {
        return HotOrder;
    }

    public void setHotOrder(String HotOrder) {
        this.HotOrder = HotOrder;
    }

    public String getTagAvatar() {
        return TagAvatar;
    }

    public void setTagAvatar(String TagAvatar) {
        this.TagAvatar = TagAvatar;
    }
}
