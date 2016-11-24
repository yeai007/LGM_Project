package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Adapter.MyFriendsListAdapter;
import com.hopeofseed.hopeofseed.DataForHttp.AddGroupMember;
import com.hopeofseed.hopeofseed.DataForHttp.GetMyFriends;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/14 16:47
 * 修改人：whisper
 * 修改时间：2016/9/14 16:47
 * 修改备注：
 */
public class AddNewGroupMember extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddNewGroupMember";
    ListView lv_friends;
    MyFriendsListAdapter myFriendsListAdapter;
    ArrayList<UserData> arr_FriendData = new ArrayList<>();
    String GroupId;
    String JpushGroupId;
    ArrayList<String> jpushAddUser = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_groupmember);
        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GROUPID");
        JpushGroupId = intent.getStringExtra("JpushGroupId");
        initView();
    }

    private void initView() {
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        TextView btn_topright = (TextView) findViewById(R.id.btn_topright);
        btn_topright.setText("添加");
        btn_topright.setOnClickListener(this);
        lv_friends = (ListView) findViewById(R.id.lv_friends);
        myFriendsListAdapter = new MyFriendsListAdapter(getApplicationContext(), arr_FriendData);
        lv_friends.setAdapter(myFriendsListAdapter);
        getMyFriends();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                HashMap<Integer, Boolean> isSelected = myFriendsListAdapter.getIsSelected();
                Iterator iter = isSelected.entrySet().iterator();
                String add_user = null;

                boolean isFirst = true;
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
                        jpushAddUser.add(arr_FriendData.get((int) key).getUser_name());
                    }
                }
                addGroupMember(add_user);
                break;
        }
    }

    private void addGroupMember(final String add_user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddGroupMember addGroupMember = new AddGroupMember();
                addGroupMember.AddUsers = add_user;
                addGroupMember.GroupId = GroupId;
                Boolean bRet = addGroupMember.RunData();
                Message msg = addGroupMemberHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
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

                    break;
                case 2:

                    break;
            }
        }
    };
    private Handler addGroupMemberHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    Log.e(TAG, "handleMessage: " + jpushAddUser);
                    JMessageClient.addGroupMembers(Long.parseLong(JpushGroupId), jpushAddUser, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            Log.i(TAG, " JMessageClient.exitGroup" + ", responseCode = " + i + " ; LoginDesc = " + s);
                            if (i == 0) {
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    break;
                case 2:

                    break;
            }
        }
    };

}
