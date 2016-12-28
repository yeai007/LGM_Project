package com.hopeofseed.hopeofseed.ui.chatting.controller;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Activitys.CreateGroupActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.utils.DialogCreator;
import com.hopeofseed.hopeofseed.ui.chatting.utils.HandleResponseCode;
import com.hopeofseed.hopeofseed.ui.view.CreateGroupView;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;

public class CreateGroupController implements OnClickListener, NetCallBack {
    private static final String TAG = "CreateGroupController";
    private CreateGroupView mCreateGroupView;
    private CreateGroupActivity mContext;
    private Dialog mDialog = null;
    private String mGroupName;
    String JpushGroupId;
    GroupInfo thisGroupInfo;

    public CreateGroupController(CreateGroupView view, CreateGroupActivity context) {
        this.mCreateGroupView = view;
        this.mContext = context;
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.creat_group_return_btn:
                mContext.finish();
                break;
            case R.id.jmui_commit_btn:
                submit();
                break;
            case R.id.btn_topleft:
                mContext.finish();
                break;
            case R.id.btn_topright:
                submit();
                break;
        }
    }

    private void submit() {
        hideInput();
        mGroupName = mCreateGroupView.getGroupName();
        if (mGroupName.equals("")) {
            mCreateGroupView.groupNameError(mContext);
            return;
        }
        if (mContext.StrProvince.equals("")) {
            mCreateGroupView.groupAddressError(mContext);
            return;
        }
        mDialog = DialogCreator.createLoadingDialog(mContext, mContext.getString(R.string.creating_hint));
        final String desc = "";
        mDialog.show();
        JMessageClient.createGroup(mGroupName, desc, new CreateGroupCallback() {
            @Override
            public void gotResult(final int status, String msg, final long groupId) {
                mDialog.dismiss();
                if (status == 0) {
                    JpushGroupId = String.valueOf(groupId);
                    getGroupInfo();
                } else {
                    HandleResponseCode.onHandle(mContext, status, false);
                    Log.i("CreateGroupController", "status : " + status);
                }
            }
        });
    }

    private void getGroupInfo() {

        JMessageClient.getGroupInfo(Long.parseLong(JpushGroupId), new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String s, GroupInfo groupInfo) {
                thisGroupInfo = groupInfo;
                addNewGroup();
            }
        });
    }

    private void addNewGroup() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("GroupOwner", thisGroupInfo.getGroupOwner());
        opt_map.put("GroupName", thisGroupInfo.getGroupName());
        opt_map.put("JpushGroupId", String.valueOf(thisGroupInfo.getGroupID()));
        opt_map.put("Province", mContext.StrProvince);
        opt_map.put("City", mContext.StrCity);
        opt_map.put("Zone", mContext.StrZone);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "addGroup.php", opt_map, CommResultTmp.class, this);
    }

    private void AddMessage() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("MessageTypeId", "6");
        opt_map.put("MessageTypeName", "群组");
        opt_map.put("MessageToClass", "");
        opt_map.put("MessageTitle", this.thisGroupInfo.getGroupName());
        opt_map.put("MessageContent", JMessageClient.getMyInfo().getNickname() + "创建了群组：" + thisGroupInfo.getGroupName());
        opt_map.put("MessageFrom", String.valueOf(this.thisGroupInfo.getGroupID()));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddMessage.php", opt_map, CommResultTmp.class, this);
    }


    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("AddGroup")) {
            CommResultTmp commResultTmp = ObjectUtil.cast(rspBaseBean);
            Log.e(TAG, "onSuccess: " + commResultTmp.getDetail());
            if (Integer.parseInt(commResultTmp.getDetail()) > 0) {
                Log.e(TAG, "gotResult: 创建群组成功，并跳转：" + thisGroupInfo.getGroupID() + mGroupName);
                mContext.startChatActivity(thisGroupInfo.getGroupID(), mGroupName);
            } else {
                Toast.makeText(mContext, "创建失败，请重新创建", Toast.LENGTH_SHORT).show();
            }
            AddMessage();
        }
        if (rspBaseBean.RequestSign.equals("AddMessage")) {
            CommResultTmp commResultTmp = ObjectUtil.cast(rspBaseBean);
            Log.e(TAG, "onSuccess: " + commResultTmp.getDetail());
            if (Integer.parseInt(commResultTmp.getDetail()) > 0) {
                Log.e(TAG, "gotResult: 创建群组消息成功：" + commResultTmp.getDetail());

            } else {
                Toast.makeText(mContext, "创建失败，请重新创建", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    public void hideInput() {
        InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(mCreateGroupView.getWindowToken(), 0);
    }
}
