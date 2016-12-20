package com.hopeofseed.hopeofseed.JNXData;

import cn.jpush.im.android.api.model.Conversation;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/19 10:30
 * 修改人：whisper
 * 修改时间：2016/12/19 10:30
 * 修改备注：
 */
public class ConvAndGroupData {

    private GroupData GroupData;

    public GroupData getGroupData() {
        return this.GroupData;
    }

    public void setGroupData(GroupData result) {
        this.GroupData = result;
    }

    private Conversation Conversation;

    public Conversation getConversation() {
        return this.Conversation;
    }

    public void setConversation(Conversation result) {
        this.Conversation = result;
    }
}
