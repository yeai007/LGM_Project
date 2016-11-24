package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.controller.CreateGroupController;
import com.hopeofseed.hopeofseed.ui.chatting.BaseActivity;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.hopeofseed.hopeofseed.ui.view.CreateGroupView;
/*
创建群聊
 */
public class CreateGroupActivity extends BaseActivity {
    private CreateGroupView mCreateGroupView;
    private CreateGroupController mCreateGroupController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mCreateGroupView = (CreateGroupView) findViewById(R.id.create_group_view);
        mCreateGroupView.initModule();
        mCreateGroupController = new CreateGroupController(mCreateGroupView, this);
        mCreateGroupView.setListeners(mCreateGroupController);
    }


    public void startChatActivity(long groupId, String groupName) {
        Intent intent = new Intent();
        //设置跳转标志
        intent.putExtra("fromGroup", true);
        intent.putExtra(Application.GROUP_ID, groupId);
        intent.putExtra(Application.GROUP_NAME, groupName);
        intent.putExtra(Application.MEMBERS_COUNT, 1);
        intent.setClass(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

}
