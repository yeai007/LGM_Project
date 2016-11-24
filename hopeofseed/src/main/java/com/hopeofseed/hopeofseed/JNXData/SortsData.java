package com.hopeofseed.hopeofseed.JNXData;

import io.realm.RealmObject;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/23 14:57
 * 修改人：whisper
 * 修改时间：2016/10/23 14:57
 * 修改备注：
 */
public class SortsData  {

    /**
     * varietyid : 0000000009
     * varietyname : 玉米
     * varietyclassid : 2
     * arrvarietyid :
     * createtime : 2016-08-18 10:31:18
     * hotlevel : 1
     * hotorder : 0
     * arrvarietyname :
     * brand_names : 郑单938,登海7号
     * brand_ids : 0000000002,0000000001
     */

    private String varietyid;
    private String varietyname;
    private String varietyclassid;
    private String arrvarietyid;
    private String createtime;
    private String hotlevel;
    private String hotorder;
    private String arrvarietyname;

    public String getVarietyid() {
        return varietyid;
    }

    public void setVarietyid(String varietyid) {
        this.varietyid = varietyid;
    }

    public String getVarietyname() {
        return varietyname;
    }

    public void setVarietyname(String varietyname) {
        this.varietyname = varietyname;
    }

    public String getVarietyclassid() {
        return varietyclassid;
    }

    public void setVarietyclassid(String varietyclassid) {
        this.varietyclassid = varietyclassid;
    }

    public String getArrvarietyid() {
        return arrvarietyid;
    }

    public void setArrvarietyid(String arrvarietyid) {
        this.arrvarietyid = arrvarietyid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getHotlevel() {
        return hotlevel;
    }

    public void setHotlevel(String hotlevel) {
        this.hotlevel = hotlevel;
    }

    public String getHotorder() {
        return hotorder;
    }

    public void setHotorder(String hotorder) {
        this.hotorder = hotorder;
    }

    public String getArrvarietyname() {
        return arrvarietyname;
    }

    public void setArrvarietyname(String arrvarietyname) {
        this.arrvarietyname = arrvarietyname;
    }
}
