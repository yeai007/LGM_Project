package com.hopeofseed.hopeofseed.JNXData;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/26 9:15
 * 修改人：whisper
 * 修改时间：2016/8/26 9:15
 * 修改备注：
 */
public class AreaData {

    /**
     * areaid : 1
     * areaname : 北京市
     * parentid : 0
     * arrparentid : 0
     * child : 1
     * arrchildid : 1,371,372,380,381,382,383,384,385,386,387,388,389,390,391,392,393,394,395,396,397
     * listorder : 1
     */

    private String areaid;
    private String areaname;
    private String parentid;
    private String arrparentid;
    private String child;
    private String arrchildid;
    private String listorder;

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
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
