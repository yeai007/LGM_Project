package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Adapter.ChatAdapter;
import com.hopeofseed.hopeofseed.Adapter.GroupConversationListAdapter;
import com.hopeofseed.hopeofseed.ui.ChatInput;
import com.hopeofseed.hopeofseed.ui.ChatView;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.ImagePreviewActivity;
import com.hopeofseed.hopeofseed.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/14 15:36
 * 修改人：whisper
 * 修改时间：2016/9/14 15:36
 * 修改备注：
 */
public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener, ChatView {
    String GroupId;
    String JpushGroupId;
    EditText input_text;
    private ChatAdapter adapter;
    private ListView listView;
    private static final String TAG = "GroupChatActivity";
    GroupConversationListAdapter groupConversationListAdapter;
    private List<Message> messageList = new ArrayList<>();
    private ChatInput input;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;
    private Uri fileUri;
    TextView apptitle;
    GroupInfo thisGroupInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat_activity);
        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GROUPID");
        JpushGroupId = intent.getStringExtra("JPUSHGROUPID");
        Log.e(TAG, "onCreate:JpushGroupId: " + JpushGroupId);
        initView();
        JMessageClient.enterGroupConversation(Long.parseLong(JpushGroupId));
        getGroupInfo();
        UpdateListData();
        JMessageClient.registerEventReceiver(this);
    }

    private void getGroupInfo() {

        JMessageClient.getGroupInfo(Long.parseLong(JpushGroupId), new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String s, GroupInfo groupInfo) {
                thisGroupInfo = groupInfo;
                apptitle.setText(groupInfo.getGroupName());
            }
        });
    }


    private void initView() {
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("设置");
        apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("群聊");
        input = (ChatInput) findViewById(R.id.input_panel);
        input.setChatView(this);
        adapter = new ChatAdapter(this, R.layout.item_message, messageList);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //input.setInputMode(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int firstItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItem == 0) {
                    //如果拉到顶端读取更多消息
                    //  presenter.getMessage(messageList.size() > 0 ? messageList.get(0).getMessage() : null);
                    UpdateListData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                JMessageClient.exitConversaion();
                finish();
                break;
            case R.id.btn_topright:
                Intent intent = new Intent(this, GroupSetting.class);
                intent.putExtra("JpushGroupId", JpushGroupId);
                intent.putExtra("GROUPID", GroupId);
                startActivity(intent);
                break;
        }
    }


    private void UpdateListData() {

        android.os.Message msg = UpdateListDataHandle.obtainMessage();
        msg.arg1 = 1;
        msg.sendToTarget();
    }

    private Handler UpdateListDataHandle = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {

                case 0:
                    break;
                case 1:
                    Conversation cst = JMessageClient.getGroupConversation(Long.parseLong(JpushGroupId));
                    if (cst == null) {
                        Log.e(TAG, "handleMessage1: null");
                    } else {
                        // Log.e(TAG, "sendMessage: " + cst.getAllMessage());
                        messageList.clear();
                        messageList.addAll(cst.getAllMessage());
                        adapter.notifyDataSetChanged();
                        listView.setSelection(messageList.size());
                    }
                    break;
                case 2:

                    break;
            }
        }
    };

    public void onEvent(NotificationClickEvent event) {
        Intent notificationIntent = new Intent(this, NotifyActivity.class);
        startActivity(notificationIntent);// 自定义跳转到指定页面
    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
                Log.e(TAG, "onEvent: " + "接收消息：" + textContent.getText());
                UpdateListData();
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                }
                break;
        }
    }

    @Override
    public void showMessage(Message message) {

    }

    @Override
    public void showMessage(List<Message> messages) {

    }

    @Override
    public void clearAllMessage() {

    }

    @Override
    public void onSendMessageSuccess(Message message) {

    }

    @Override
    public void onSendMessageFail(int code, String desc, Message message) {

    }

    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    @Override
    public void sendPhoto() {

    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText() {
        String id = JpushGroupId;
        String text = input.getText().toString();
        if (!TextUtils.isEmpty(id)) {
            long gid = Long.parseLong(id);
            Message message = JMessageClient.createGroupTextMessage(gid, text);
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        //Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                        // Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                        UpdateListData();
                        input.setText("");
                    } else {
                        // Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                        Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                        input.setText("");
                    }
                }
            });
            JMessageClient.sendMessage(message);

        } else {
            Toast.makeText(getApplicationContext(), "请输入群组id", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendFile() {

    }

    @Override
    public void startSendVoice() {

    }

    @Override
    public void endSendVoice() {

    }

    @Override
    public void sendVideo(String fileName) {

    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void sending() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && fileUri != null) {
                showImagePreview(fileUri.getPath());
            }
        } else if (requestCode == IMAGE_STORE) {
            if (resultCode == RESULT_OK && data != null) {
                showImagePreview(FileUtil.getFilePath(this, data.getData()));
            }

        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                //sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW) {
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri", false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists() && file.length() > 0) {
                    if (file.length() > 1024 * 1024 * 10) {
                        Toast.makeText(this, getString(R.string.chat_file_too_large), Toast.LENGTH_SHORT).show();
                    } else {
                        String id = JpushGroupId;
                        if (TextUtils.isEmpty(id)) {
                            Toast.makeText(getApplicationContext(), "群ID为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        long gid = Long.parseLong(id.trim());
                        try {

                            Message imageMessage = JMessageClient.createGroupImageMessage(gid, file);
                            imageMessage.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                                        UpdateListData();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                        Log.i("CreateGroupImageMsgActivity", "JMessageClient.createGroupImageMessage " + ", responseCode = " + i + " ; LoginDesc = " + s);
                                    }
                                }
                            });
                            JMessageClient.sendMessage(imageMessage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    }
                } else {
                    Toast.makeText(this, getString(R.string.chat_file_not_exist), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showImagePreview(String path) {
        if (path == null) return;
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

}

