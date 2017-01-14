package com.hopeofseed.hopeofseed.JNXData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/28 20:19
 * 修改人：whisper
 * 修改时间：2016/11/28 20:19
 * 修改备注：
 */
@RealmClass
public class HotSearchData extends RealmObject {

    /**
     * HotSearchId : 0000000001
     * HotSearchStr : 齐单
     * HotSearchLevel : 0
     * HotSearchOrder : 00000000000
     */
    @PrimaryKey
    private String HotSearchId;
    private String HotSearchStr;
    private String HotSearchLevel;
    private String HotSearchOrder;

    public String getHotSearchId() {
        return HotSearchId;
    }

    public void setHotSearchId(String HotSearchId) {
        this.HotSearchId = HotSearchId;
    }

    public String getHotSearchStr() {
        return HotSearchStr;
    }

    public void setHotSearchStr(String HotSearchStr) {
        this.HotSearchStr = HotSearchStr;
    }

    public String getHotSearchLevel() {
        return HotSearchLevel;
    }

    public void setHotSearchLevel(String HotSearchLevel) {
        this.HotSearchLevel = HotSearchLevel;
    }

    public String getHotSearchOrder() {
        return HotSearchOrder;
    }

    public void setHotSearchOrder(String HotSearchOrder) {
        this.HotSearchOrder = HotSearchOrder;
    }
}
