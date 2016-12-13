package com.hopeofseed.hopeofseed.Activitys;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.BaseActivity;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.hopeofseed.hopeofseed.ui.chatting.controller.ChatDetailController;
import com.hopeofseed.hopeofseed.ui.chatting.utils.HandleResponseCode;
import com.hopeofseed.hopeofseed.ui.view.ChatDetailView;
import java.lang.ref.WeakReference;
import java.util.List;

import citypickerview.widget.CityPicker;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static com.hopeofseed.hopeofseed.R.id.chat_detail_group_address;

/*
 * 在对话界面中点击聊天信息按钮进来的聊天信息界面
 */
public class ChatDetailActivity extends BaseActivity{

    private static final String TAG = "ChatDetailActivity";
    private ChatDetailView mChatDetailView;
    private ChatDetailController mChatDetailController;
    private UIHandler mUIHandler = new UIHandler(this);
    public final static String START_FOR_WHICH = "which";
    private final static int GROUP_NAME_REQUEST_CODE = 1;
    private final static int MY_NAME_REQUEST_CODE = 2;
    private static final int ADD_FRIEND_REQUEST_CODE = 3;
    private Context mContext;
    private ProgressDialog mDialog;
    Button chat_detail_group_address;
    CityPicker cityPicker;
    public String StrProvince = "", StrCity = "", StrZone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        mContext = this;
        mChatDetailView = (ChatDetailView) findViewById(R.id.chat_detail_view);
        mChatDetailView.initModule();
        mChatDetailController = new ChatDetailController(mChatDetailView, this, mAvatarSize, mWidth);
        mChatDetailView.setListeners(mChatDetailController);
        mChatDetailView.setOnChangeListener(mChatDetailController);
        mChatDetailView.setItemListener(mChatDetailController);
        chat_detail_group_address = (Button) findViewById(R.id.chat_detail_group_address);
        chat_detail_group_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityPicker = new CityPicker.Builder(mContext).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province("山东省")
                        .city("济南市")
                        .district("全部")
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(false)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .build();

                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {

                        chat_detail_group_address.setText("" + citySelected[0] + "  " + citySelected[1] + "  " + citySelected[2]);
                        StrProvince = citySelected[0];
                        StrCity = citySelected[1];
                        StrZone = citySelected[2];
                        mChatDetailController.updateInfoNoJpush();
                    }
                });
            }
        });
    }

    public void setAddress(String Province, String City, String Zone) {
        if (!TextUtils.isEmpty(Province)) {
            StrProvince = Province;
            StrCity = City;
            StrZone = Zone;
            chat_detail_group_address.setText("" + Province + "  " + City + "  " + Zone);
        }
    }

    //设置群聊名称
    public void showGroupNameSettingDialog(int which, final long groupID, String groupName) {
        final Dialog dialog = new Dialog(this, R.style.jmui_default_dialog_style);
        View view = LayoutInflater.from(this).inflate(R.layout.jmui_dialog_reset_password, null);
        dialog.setContentView(view);
        if (which == 1) {
            TextView title = (TextView) view.findViewById(R.id.jmui_title_tv);
            title.setText(mContext.getString(R.string.group_name_hit));
            final EditText pwdEt = (EditText) view.findViewById(R.id.jmui_password_et);
            pwdEt.addTextChangedListener(new TextWatcher() {
                private CharSequence temp = "";
                private int editStart;
                private int editEnd;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    temp = s;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    editStart = pwdEt.getSelectionStart();
                    editEnd = pwdEt.getSelectionEnd();
                    byte[] data = temp.toString().getBytes();
                    if (data.length > 64) {
                        s.delete(editStart - 1, editEnd);
                        int tempSelection = editStart;
                        pwdEt.setText(s);
                        pwdEt.setSelection(tempSelection);
                    }
                }
            });
            pwdEt.setInputType(InputType.TYPE_CLASS_TEXT);
            pwdEt.setHint(groupName);
            pwdEt.setHintTextColor(getResources().getColor(R.color.gray));
            final Button cancel = (Button) view.findViewById(R.id.jmui_cancel_btn);
            final Button commit = (Button) view.findViewById(R.id.jmui_commit_btn);
            dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.jmui_cancel_btn:
                            dialog.cancel();
                            break;
                        case R.id.jmui_commit_btn:
                            final String newName = pwdEt.getText().toString().trim();
                            if (newName.equals("")) {
                                Toast.makeText(mContext, mContext.getString(R.string.group_name_not_null_toast), Toast.LENGTH_SHORT).show();
                            } else {
                                dismissSoftInput();
                                dialog.dismiss();
                                mDialog = new ProgressDialog(mContext);
                                mDialog.setMessage(mContext.getString(R.string.modifying_hint));
                                mDialog.show();
                                JMessageClient.updateGroupName(groupID, newName, new BasicCallback() {
                                    @Override
                                    public void gotResult(final int status, final String desc) {
                                        mDialog.dismiss();
                                        if (status == 0) {
                                            mChatDetailView.updateGroupName(newName);
                                            mChatDetailController.refreshGroupName(newName);
                                            Toast.makeText(mContext, mContext.getString(R.string.modify_success_toast), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.i(TAG, "desc :" + desc);
                                            HandleResponseCode.onHandle(mContext, status, false);
                                        }
                                    }
                                });
                            }
                            break;
                    }
                }
            };
            cancel.setOnClickListener(listener);
            commit.setOnClickListener(listener);
        }
        if (which == 2) {
            TextView title = (TextView) view.findViewById(R.id.jmui_title_tv);
            title.setText(mContext.getString(R.string.group_my_name_hit));
            title.setTextColor(Color.parseColor("#000000"));
            final EditText pwdEt = (EditText) view.findViewById(R.id.jmui_password_et);
            pwdEt.setHint(mContext.getString(R.string.change_nickname_hint));
            final Button cancel = (Button) view.findViewById(R.id.jmui_cancel_btn);
            final Button commit = (Button) view.findViewById(R.id.jmui_commit_btn);
            dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.jmui_cancel_btn:
                            dialog.cancel();
                            break;
                        case R.id.jmui_commit_btn:
                            dialog.cancel();
                            break;
                    }
                }
            };
            cancel.setOnClickListener(listener);
            commit.setOnClickListener(listener);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.putExtra(Application.NAME, mChatDetailController.getName());
        intent.putExtra(Application.MEMBERS_COUNT, mChatDetailController.getCurrentCount());
        intent.putExtra("deleteMsg", mChatDetailController.getDeleteFlag());
        setResult(Application.RESULT_CODE_CHAT_DETAIL, intent);
        finish();
        super.onBackPressed();
    }

    private void dismissSoftInput() {
        //隐藏软键盘
        InputMethodManager imm = ((InputMethodManager) mContext
                .getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (this.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GROUP_NAME_REQUEST_CODE) {
            Log.i(TAG, "resultName = " + data.getStringExtra("resultName"));
            mChatDetailView.setGroupName(data.getStringExtra("resultName"));
        } else if (requestCode == MY_NAME_REQUEST_CODE) {
            Log.i(TAG, "myName = " + data.getStringExtra("resultName"));
            mChatDetailView.setMyName(data.getStringExtra("resultName"));
        } else if (resultCode == Application.RESULT_CODE_FRIEND_INFO) {
            if (data.getBooleanExtra("returnChatActivity", false)) {
                data.putExtra("deleteMsg", mChatDetailController.getDeleteFlag());
                data.putExtra(Application.NAME, mChatDetailController.getName());
                setResult(Application.RESULT_CODE_CHAT_DETAIL, data);
                finish();
            }
        } else if (requestCode == Application.REQUEST_CODE_ALL_MEMBER) {
            mChatDetailController.refreshMemberList();
        }
        else if(requestCode==801||requestCode==802||requestCode==803)
        {
            mChatDetailController.refreshAvatar();
            mChatDetailController.refreshMemberList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChatDetailController.getAdapter() != null) {
            mChatDetailController.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 从ContactsActivity中选择朋友加入到群组中
     */
    public void showContacts() {
      /*  Intent intent = new Intent();
        intent.putExtra(TAG, 1);
        intent.setClass(this, SelectFriendActivity.class);
        startActivityForResult(intent, ADD_FRIEND_REQUEST_CODE);*/
    }


    public void startMainActivity() {
   /*     Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(this, MainActivity.class);
        startActivity(intent);*/
    }

    public void startChatActivity(long groupID, String groupName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //设置跳转标志
        intent.putExtra("fromGroup", true);
        intent.putExtra(Application.MEMBERS_COUNT, 3);
        intent.putExtra(Application.GROUP_ID, groupID);
        intent.putExtra(Application.GROUP_NAME, groupName);
        intent.setClass(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 接收群成员变化事件
     *
     * @param event 消息事件
     */
    public void onEvent(MessageEvent event) {
        final cn.jpush.im.android.api.model.Message msg = event.getMessage();
        if (msg.getContentType() == ContentType.eventNotification) {
            EventNotificationContent.EventNotificationType msgType = ((EventNotificationContent) msg
                    .getContent()).getEventNotificationType();
            switch (msgType) {
                //添加群成员事件特殊处理
                case group_member_added:
                    List<String> userNames = ((EventNotificationContent) msg.getContent()).getUserNames();
                    for (final String userName : userNames) {
                        JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
                            @Override
                            public void gotResult(int status, String desc, UserInfo userInfo) {
                                if (status == 0) {
                                    mChatDetailController.getAdapter().notifyDataSetChanged();
                                } else {
                                    HandleResponseCode.onHandle(mContext, status, false);
                                }
                            }
                        });
                    }
                    break;
                case group_member_removed:
                    break;
                case group_member_exit:
                    break;
            }
            //无论是否添加群成员，刷新界面
            Message handleMsg = mUIHandler.obtainMessage();
            handleMsg.what = Application.ON_GROUP_EVENT;
            Bundle bundle = new Bundle();
            bundle.putLong(Application.GROUP_ID, ((GroupInfo) msg.getTargetInfo()).getGroupID());
            handleMsg.setData(bundle);
            handleMsg.sendToTarget();
        }
    }


    private static class UIHandler extends Handler {

        private WeakReference<ChatDetailActivity> mActivity;

        public UIHandler(ChatDetailActivity activity) {
            mActivity = new WeakReference<ChatDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatDetailActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case Application.ON_GROUP_EVENT:
                        activity.mChatDetailController.refresh(msg.getData()
                                .getLong(Application.GROUP_ID, 0));
                        break;
                }
            }
        }
    }

}
