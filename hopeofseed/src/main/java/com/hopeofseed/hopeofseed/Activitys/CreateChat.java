package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Adapter.MyFriendsListAdapter;
import com.hopeofseed.hopeofseed.DataForHttp.AddGroup;
import com.hopeofseed.hopeofseed.DataForHttp.GetMyFriends;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.splashActivity;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.api.BasicCallback;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/1 15:45
 * 修改人：whisper
 * 修改时间：2016/9/1 15:45
 * 修改备注：
 */
public class CreateChat extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateChat";
    ListView lv_friends;
    MyFriendsListAdapter myFriendsListAdapter;
    ArrayList<UserData> arr_FriendData = new ArrayList<>();
    String groupid, jpushgroupid;
    private String appKey;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                createChat();
                break;
        }
    }

    private void createChat() {
        HashMap<Integer, Boolean> isSelected = myFriendsListAdapter.getIsSelected();
        Iterator iter = isSelected.entrySet().iterator();
        String add_user = null;
        boolean isFirst = true;
        ArrayList<String> usernameList = new ArrayList<String>();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            Log.e(TAG, "onClick: " + String.valueOf(key) + String.valueOf(val));

            if (String.valueOf(val).trim().equals("true")) {
                if (isFirst) {
                    add_user = String.valueOf(arr_FriendData.get((int) key).getUser_id());
                    isFirst = false;
                } else {
                    add_user = add_user + "," + String.valueOf(key);
                }
                usernameList.add(String.valueOf(arr_FriendData.get((int) key).getUser_name()));
            }
        }
        if (isSelected.size() == 1) {
            addJpushGroup(add_user, usernameList);
        } else {
            addJpushGroup("群聊", usernameList);
        }
        Log.e(TAG, "createChat: success" + add_user);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chat);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("添加联系人");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("聊天");
        lv_friends = (ListView) findViewById(R.id.lv_friends);
        lv_friends = (ListView) findViewById(R.id.lv_friends);
        myFriendsListAdapter = new MyFriendsListAdapter(getApplicationContext(), arr_FriendData);
        lv_friends.setAdapter(myFriendsListAdapter);
        getMyFriends();
    }

    private void getMyFriends() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetMyFriends getMyFriends = new GetMyFriends();
                Boolean bRet = getMyFriends.RunData();
                Message msg = getMyFriendsHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                    msg.obj = getMyFriends.retRows;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler getMyFriendsHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    ArrayList<UserData> arr_UserData_tmp = new ArrayList<>();
                    arr_UserData_tmp = ObjectUtil.cast(msg.obj);
                    arr_FriendData.clear();
                    arr_FriendData.addAll(arr_UserData_tmp);
                    myFriendsListAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "创建成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:

                    break;
            }
        }
    };

    private void addJpushGroup(final String group_name, final ArrayList<String> usernameList) {
        String name = group_name;
        String desc = "add jpush group test";
        //创建群组
        JMessageClient.createGroup(name, desc, new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, long l) {
                if (i == 0) {
                    Log.e(TAG, "gotResult: " + "创建成功" + l);
                    Toast.makeText(getApplicationContext(), "创建成功" + l, Toast.LENGTH_SHORT).show();
                    addJpushMember(l, usernameList, group_name);
                } else {

                    Toast.makeText(getApplicationContext(), "创建失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addJpushMember(final Long jpushgroupid, final ArrayList<String> usernameList, final String group_name) {
        appKey = splashActivity.getAppKey(getApplicationContext());
        JMessageClient.addGroupMembers(jpushgroupid, appKey, usernameList, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    addNewDiscoveryGroup(String.valueOf(jpushgroupid), group_name);
                } else {

                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                    Log.i("AddRemoveGroupMemberActivity", "JMessageClient.addGroupMembers " + ", responseCode = " + i + " ; Desc = " + s);
                }
            }
        });
    }

    private void addNewDiscoveryGroup(final String JpushGroupId, final String group_name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddGroup addGroup = new AddGroup();
                addGroup.GroupName = group_name;
                addGroup.JpushGroupId = JpushGroupId;
                Boolean bRet = addGroup.RunData();
                Message msg = addGroupHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                    msg.obj = addGroup.dataMessage.obj;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler addGroupHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:

                    break;
                case 1:
                    GroupData GroupData_tmp = new GroupData();
                    GroupData_tmp = ObjectUtil.cast(msg.obj);
                    Toast.makeText(getApplicationContext(), "创建成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateChat.this, GroupChatActivity.class);
                    intent.putExtra("GROUPID", GroupData_tmp.getGroupid().trim());
                    intent.putExtra("JPUSHGROUPID", GroupData_tmp.getJpushGroupId().trim());
                    startActivity(intent);
                    break;
                case 2:

                    break;
            }
        }
    };


}
