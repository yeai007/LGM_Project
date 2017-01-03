package com.hopeofseed.hopeofseed.JNXData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/3 8:05
 * 修改人：whisper
 * 修改时间：2017/1/3 8:05
 * 修改备注：
 */
public class AppAreaData extends RealmObject {

    /**
     * areaid : 1
     * areaname : 北京市
     * parentid : 0
     * arrparentid :
     * child : 0
     * arrchildid :
     * listorder : 0
     */
    @PrimaryKey
    private int  areaid;
    private String areaname;
    private int parentid;
    private String arrparentid;
    private String child;
    private String arrchildid;
    private String listorder;

    public int getAreaid() {
        return areaid;
    }

    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getArrparentid() {
        return arrparentid;
    }

    public void setArrparentid(String arrparentid) {
        this.arrparentid = arrparentid;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getArrchildid() {
        return arrchildid;
    }

    public void setArrchildid(String arrchildid) {
        this.arrchildid = arrchildid;
    }

    public String getListorder() {
        return listorder;
    }

    public void setListorder(String listorder) {
        this.listorder = listorder;
    }
}
