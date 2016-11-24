package com.hopeofseed.hopeofseed.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.DataForHttp.DeleteGroup;
import com.hopeofseed.hopeofseed.DataForHttp.UpdateGroupInfo;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/14 16:49
 * 修改人：whisper
 * 修改时间：2016/9/14 16:49
 * 修改备注：
 */
public class GroupSetting extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GroupSetting";
    String GroupId;
    public static final int REQUSET = 101;
    GroupInfo mGroupInfo;
    TextView tv_gourpname, tv_card, tv_introduction;
    ArrayList<UserInfo> groupMembers = new ArrayList<>();
    RecyclerView member_recycler;
    GridAdapter gridAdapter;
    private ArrayList<String> images = new ArrayList<>();
    String JpushGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_setting);
        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GROUPID");
        JpushGroupId = intent.getStringExtra("JpushGroupId");
        mGroupInfo = intent.getParcelableExtra("groupinfo");
        initView();
        getGroupInfo();
    }

    private void getGroupInfo() {
        groupMembers.clear();
        JMessageClient.getGroupInfo(Long.parseLong(JpushGroupId), new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String s, GroupInfo groupInfo) {

                Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                mGroupInfo = groupInfo;
                Log.e(TAG, "gotResult: " + mGroupInfo.getGroupMembers().size());
                tv_card.setText(mGroupInfo.getGroupOwner());
                tv_gourpname.setText(mGroupInfo.getGroupName());
                tv_introduction.setText(mGroupInfo.getGroupDescription());
                groupMembers.addAll(mGroupInfo.getGroupMembers());

            }
        });
    }

    private void initView() {
        (findViewById(R.id.del_group)).setOnClickListener(this);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("更新");
        (findViewById(R.id.btn_topright)).setOnClickListener(this);
        tv_gourpname = (TextView) findViewById(R.id.tv_gourpname);
        tv_card = (TextView) findViewById(R.id.tv_card);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        images.add("add");
        member_recycler = (RecyclerView) findViewById(R.id.member_recycler);
        member_recycler.setLayoutManager(new GridLayoutManager(this, 6));
        gridAdapter = new GridAdapter(groupMembers);
        member_recycler.setAdapter(gridAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                if (tv_gourpname.getText().toString().trim().equals(mGroupInfo.getGroupName()) || tv_introduction.getText().toString().trim().equals(mGroupInfo.getGroupDescription())) {
                    updateGroupInfo();
                } else {
                }
                break;
            case R.id.del_group:
                deleteJpushGroup();
                break;
        }
    }

    private void deleteJpushGroup() {
        JMessageClient.exitGroup(Long.parseLong(JpushGroupId), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.i(TAG, " JMessageClient.exitGroup" + ", responseCode = " + i + " ; LoginDesc = " + s);
                if (i == 0) {
                    deleteGroup();
                } else {

                }
            }
        });
    }

    private void deleteGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DeleteGroup deleteGroup = new DeleteGroup();
                deleteGroup.UserId = String.valueOf(Const.currentUser.user_id);
                deleteGroup.GroupId = GroupId;
                Boolean bRet = deleteGroup.RunData();
                Message msg = deleteGroupHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }

                msg.sendToTarget();
            }
        }).start();
    }

    private Handler deleteGroupHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:

                    break;
                case 1:
                    Intent intent = new Intent(GroupSetting.this, SelectGroup.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:

                    break;
            }
        }
    };

    private void updateGroupInfo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateGroupInfo updateGroupInfo = new UpdateGroupInfo();
                updateGroupInfo.UserId = String.valueOf(Const.currentUser.user_id);
                updateGroupInfo.GroupId = GroupId.replaceAll("^(0+)", "");
                updateGroupInfo.GroupName = tv_gourpname.getText().toString().trim();
                updateGroupInfo.GroupDescription = tv_introduction.getText().toString().trim();
                Boolean bRet = updateGroupInfo.RunData();
                Message msg = updateGroupInfoHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }

                msg.sendToTarget();
            }
        }).start();
    }

    private Handler updateGroupInfoHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:

                    break;
                case 1:
                    final Boolean[] update_result = {true};
                    Log.e(TAG, "handleMessage: getGroupName" + mGroupInfo.getGroupName());
                    Log.e(TAG, "handleMessage: getGroupDescription" + mGroupInfo.getGroupDescription());
                    if (tv_gourpname.getText().toString().trim().equals(mGroupInfo.getGroupName())) {
                        JMessageClient.updateGroupName(Long.parseLong(JpushGroupId), tv_gourpname.getText().toString().trim(), new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                Log.i(TAG, " JMessageClient.exitGroup" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                if (i == 0) {
                                    Log.e(TAG, "gotResult: update groupname success");
                                } else {
                                    update_result[0] = false;
                                }
                            }
                        });
                    } else {
                    }
                    if (tv_introduction.getText().toString().trim().equals(mGroupInfo.getGroupDescription())) {
                        JMessageClient.updateGroupDescription(Long.parseLong(JpushGroupId), tv_introduction.getText().toString().trim(), new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                Log.i(TAG, " JMessageClient.exitGroup" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                if (i == 0) {
                                    Log.e(TAG, "gotResult: update groupdescription success");
                                } else {
                                    update_result[0] = false;
                                }
                            }
                        });
                        if (update_result[0]) {
                            Log.e(TAG, "handleMessage: successed");
                        } else {
                            Log.e(TAG, "handleMessage: failed");
                        }
                    }
                    finish();
                    break;
                case 2:

                    break;
            }
        }
    };

    private void AddNewMembers() {
        Intent intent = new Intent(this, AddNewGroupMember.class);
        intent.putExtra("GROUPID", GroupId);
        intent.putExtra("JpushGroupId", JpushGroupId);
        startActivityForResult(intent, REQUSET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == GroupSetting.REQUSET && resultCode == RESULT_OK) {
            //添加成功
            getGroupInfo();
        }

    }

    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        ArrayList<UserInfo> mList = new ArrayList<>();

        public GridAdapter(ArrayList<UserInfo> groupMembers) {
            super();
            this.mList = groupMembers;
        }

        @Override
        public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_rec, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GridAdapter.ViewHolder holder, int position) {
            if (position == 0) {
                Glide.with(GroupSetting.this)
                        .load(R.drawable.add_img)
                        .centerCrop()
                        .into(holder.imageView);
            } else {
                Log.e(TAG, "onBindViewHolder: " + mList.get(position - 1).getUserName()+mList.get(position-1).getAvatar());
                Glide.with(GroupSetting.this)
                        .load(mList.get(position - 1).getAvatarFile())
                        .centerCrop()
                        .into(holder.imageView);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size() + 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                imageView.setOnClickListener(this);
                imageView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {

                // Toast.makeText(getApplicationContext(), getPosition(), Toast.LENGTH_SHORT).show();
                if (getPosition() == 0) {
                    Log.e(TAG, "onClick: 添加成员");
                    // ImageSelectorActivity.start(PubishMainActivity.this, 9, 1, true, false, false);
                    AddNewMembers();
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (getPosition() == 0) {
                    Log.e(TAG, "onClick: 删除成员:0");

                } else {
                    Log.e(TAG, "onClick: 删除成员");

                    iosDialog.Builder builder = new iosDialog.Builder(
                            GroupSetting.this);
                    builder.setTitle("删除成员");
                    builder.setMessage("删除" + mList.get(getPosition() - 1).getUserName() + "?");
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DeleteMembers(mList.get(getPosition() - 1).getUserName());
                                    dialogInterface.dismiss();
                                    groupMembers.remove(getPosition()-1);
                                    gridAdapter.notifyItemRemoved(getPosition());
                                }
                            });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }
                return false;
            }

            private Boolean DeleteMembers(String deleteMemberId) {
                final Boolean[] isDelete = {false};
                Log.e(TAG, "DeleteMembers: " + deleteMemberId);
                ArrayList<String> deleteUserList = new ArrayList<>();
                deleteUserList.add(deleteMemberId);
                JMessageClient.removeGroupMembers(Long.parseLong(JpushGroupId), deleteUserList, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {

                        Log.i(TAG, " JMessageClient.exitGroup" + ", responseCode = " + i + " ; LoginDesc = " + s);
                        if (i == 0) {
                            isDelete[0] = true;
                        } else {
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return isDelete[0];
            }
        }

    }
}
