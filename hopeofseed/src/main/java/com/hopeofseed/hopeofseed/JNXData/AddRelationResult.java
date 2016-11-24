package com.hopeofseed.hopeofseed.JNXData;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/3 9:15
 * 修改人：whisper
 * 修改时间：2016/11/3 9:15
 * 修改备注：
 */
public class AddRelationResult {

    /**
     * DistributorId : 0000000003
     * RelationCommodityId : 0000000016
     * result : 已存在
     */

    private String DistributorId;
    private String RelationCommodityId;
    private String result;

    public String getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(String DistributorId) {
        this.DistributorId = DistributorId;
    }

    public String getRelationCommodityId() {
        return RelationCommodityId;
    }

    public void setRelationCommodityId(String RelationCommodityId) {
        this.RelationCommodityId = RelationCommodityId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
