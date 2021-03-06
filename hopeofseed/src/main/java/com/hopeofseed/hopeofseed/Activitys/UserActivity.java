package com.hopeofseed.hopeofseed.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.UsersPagerAdapter;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.FragmentListDatas;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.CategoryTabStrip;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.hopeofseed.hopeofseed.ui.entity.Event;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.hopeofseed.hopeofseed.util.ListFragmentConfig;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;

import static com.hopeofseed.hopeofseed.R.drawable.corner_enterprise;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/26 16:30
 * 修改人：whisper
 * 修改时间：2016/9/26 16:30
 * 修改备注：
 */
public class UserActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "UserActivity";
    ImageView img_user_avatar, img_corner;
    TextView tv_username, tv_follow_sum, tv_fans_sum, tv_address, appTitle;
    String UserId;
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();
    RelativeLayout rel_follow, rel_fans;
    ViewPager pager;
    CategoryTabStrip tabs;
    UsersPagerAdapter mUsersPagerAdapter;
    ArrayList<FragmentListDatas> fragmentList = new ArrayList<>();
    Button btn_submit_followed, btn_createchat, btn_func_menu;
    int isFriend = 0;
    Handler mHandler = new Handler();
    int isAddOrDel = 0;
    UserInfo mUserInfo;
    int UserRole = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        Intent intent = getIntent();
        UserId = intent.getStringExtra("userid");
        UserRole = intent.getIntExtra("UserRole", 0);
        JMessageClient.getUserInfo(Const.JPUSH_PREFIX + UserId, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i == 0) {
                    mUserInfo = userInfo;
                }
            }
        });
        Log.e(TAG, "onCreate: UserRole" + UserRole);
        initView();
        getData();
    }

    private void setFrament() {
        int[] fregments = null;
        switch (UserRole) {
            /**
             * 农友
             * */
            case 0:
                fregments = new int[]{9, 8, 6, 7};
                break;
            /**
             * 经销商
             * */
            case 1:
                fregments = new int[]{10, 11, 8, 6, 9, 7};
                break;
            /**
             * 企业
             * */
            case 2:
                fregments = new int[]{10, 11, 6, 8, 9,13};
                break;
            /**
             * 机构
             * */
            case 4:
                fregments = new int[]{9, 11};
                break;
            /**
             * 专家
             * */
            case 3:
                fregments = new int[]{9, 6, 8, 7};
                break;
            /**
             * 管理员
             * */
            case 5:
                fregments = new int[]{9, 6, 8, 11};
                break;
            /**
             * 媒体
             * */
            case 6:
                fregments = new int[]{9, 6, 8, 11};
                break;
        }
        ListFragmentConfig lfc = new ListFragmentConfig();
        fragmentList.addAll(lfc.getCommUser(fregments, UserId));
    }

    private void initView() {
        appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("用户详情");
        //appTitle.setVisibility(View.GONE);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("详细资料");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_follow_sum = (TextView) findViewById(R.id.tv_follow_sum);
        btn_submit_followed = (Button) findViewById(R.id.btn_submit_followed);
        btn_submit_followed.setOnClickListener(this);
        btn_createchat = (Button) findViewById(R.id.btn_createchat);
        btn_createchat.setOnClickListener(this);
        tv_fans_sum = (TextView) findViewById(R.id.tv_fans_sum);
        rel_fans = (RelativeLayout) findViewById(R.id.rel_fans);
        tv_address = (TextView) findViewById(R.id.tv_address);
        rel_fans.setOnClickListener(this);
        rel_follow = (RelativeLayout) findViewById(R.id.rel_follow);
        rel_follow.setOnClickListener(this);
        img_user_avatar = (ImageView) findViewById(R.id.img_user_avatar);
        img_corner = (ImageView) findViewById(R.id.img_corner);
        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        setFrament();
        mUsersPagerAdapter = new UsersPagerAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(mUsersPagerAdapter);
        tabs.setViewPager(pager);


    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_topleft:
                // Toast.makeText(getApplicationContext(), "OnClick", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_topright:
                switch (Integer.parseInt(mUserDataNoRealm.getUser_role())) {
                    case 0:
                        intent = new Intent(UserActivity.this, UserBliedActivity.class);
                        intent.putExtra("ID", mUserDataNoRealm.getUser_id());
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(UserActivity.this, DistributorActivity.class);
                        intent.putExtra("ID", mUserDataNoRealm.getUser_role_id());
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(UserActivity.this, EnterpriseActivity.class);
                        intent.putExtra("EnterpriseId", mUserDataNoRealm.getUser_role_id());
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(UserActivity.this, ExpertActivity.class);
                        intent.putExtra("ExpertId", mUserDataNoRealm.getUser_role_id());
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(UserActivity.this, AuthorActivity.class);
                        intent.putExtra("AuthorId", mUserDataNoRealm.getUser_role_id());
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(UserActivity.this, SystemUserActivity.class);
                        intent.putExtra("SystemId", mUserDataNoRealm.getUser_role_id());
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(UserActivity.this, MediaActivity.class);

                        intent.putExtra("MediaId", mUserDataNoRealm.getUser_role_id());
                        startActivity(intent);
                        break;
                }

                break;
            case R.id.rel_follow://关注
             /*   intent = new Intent(UserActivity.this, MyFollowed.class);
                intent.putExtra("UserId", UserId);
                startActivity(intent);*/
                break;
            case R.id.rel_fans:
             /*   intent = new Intent(UserActivity.this, MyFans.class);
                intent.putExtra("UserId", UserId);
                startActivity(intent);*/
                break;
            case R.id.btn_submit_followed://添加关注或取消关注
                if (isAddOrDel == 1) {
                    AddOrDelFollowed();
                }
                if (isAddOrDel == 0) {

                    iosDialog mIosDialog = new iosDialog.Builder(UserActivity.this)
                            .setTitle("种愿")
                            .setMessage("确认取消关注？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AddOrDelFollowed();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("种愿").create();
                    mIosDialog.show();
                }
                break;
            case R.id.btn_createchat:
                if (mUserInfo == null) {
                    Toast.makeText(getApplicationContext(), "该用户暂不支持留言", Toast.LENGTH_SHORT).show();
                } else {
                    createChat();
                }
                break;
            case R.id.btn_func_menu:
                break;

        }
    }

    private void createChat() {
        /**
         * 如果是从群聊跳转过来，使用startActivity启动聊天界面，如果是单聊跳转过来，setResult然后
         * finish掉此界面
         */
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Application.TARGET_ID, mUserInfo.getUserName());
        intent.putExtra(Application.TARGET_APP_KEY, mUserInfo.getAppKey());
        intent.setClass(this, ChatActivity.class);
        startActivity(intent);
        Conversation conv = JMessageClient.getSingleConversation(mUserInfo.getUserName(), mUserInfo.getAppKey());
        //如果会话为空，使用EventBus通知会话列表添加新会话
        if (conv == null) {
            conv = Conversation.createSingleConversation(mUserInfo.getUserName(), mUserInfo.getAppKey());
            EventBus.getDefault().post(new Event.StringEvent(mUserInfo.getUserName(), mUserInfo.getAppKey()));
        }
        // finish();
    }

    private void AddOrDelFollowed() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("AddUserId", mUserDataNoRealm.getUser_id());
        opt_map.put("isAddOrDel", String.valueOf(isAddOrDel));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddNewFriend.php", opt_map, CommResultTmp.class, this);
    }

    private void getIsFriend() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("AddUserId", mUserDataNoRealm.getUser_id());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetIsFriend.php", opt_map, CommResultTmp.class, this);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUserInfoById.php", opt_map, UserDataNoRealmTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetIsFriend")) {
            CommResultTmp mCommResultTmp = ObjectUtil.cast(rspBaseBean);
            isFriend = Integer.valueOf(mCommResultTmp.getDetail());
            mHandler.post(updateFollowedStatus);
        } else if (rspBaseBean.RequestSign.equals("AddNewFriend")) {
            CommResultTmp mCommResultTmp = ObjectUtil.cast(rspBaseBean);
            getIsFriend();
        } else {

            mUserDataNoRealm = ((UserDataNoRealmTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);
            updateView();
        }
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    Runnable updateFollowedStatus = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            Log.e(TAG, "run: swich" + isFriend);
            switch (isFriend) {
                case 0://双方未关注
                    btn_submit_followed.setText("+关注");
                    btn_submit_followed.setTextColor(Color.parseColor("#FF0000"));
                    isAddOrDel = 1;
                    break;
                case 1://当前关注用户，用户未关注当前页帐号
                    btn_submit_followed.setText("+关注");
                    btn_submit_followed.setTextColor(Color.parseColor("#272636"));
                    isAddOrDel = 1;
                    break;
                case 2://用户关注当前页帐号
                    btn_submit_followed.setText("已关注");
                    btn_submit_followed.setTextColor(Color.parseColor("#272636"));
                    isAddOrDel = 0;
                    break;

                case 3://双向关注
                    btn_submit_followed.setText("互相关注(好友)");
                    btn_submit_followed.setTextColor(Color.parseColor("#272636"));
                    isAddOrDel = 0;
                    break;
            }
        }
    };
    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //setFrament(Integer.parseInt(mUserDataNoRealm.getUser_role()));
            Log.e(TAG, "handleMessage: updateview");
            //appTitle.setText(mUserDataNoRealm.getNickname());
            tv_username.setText(mUserDataNoRealm.getNickname());
            tv_follow_sum.setText(mUserDataNoRealm.getFllowed_count());
            tv_fans_sum.setText(mUserDataNoRealm.getBeen_fllowed_count());
            tv_address.setText(mUserDataNoRealm.getUserProvince() + " " + mUserDataNoRealm.getUserCity() + " " + mUserDataNoRealm.getUserZone());
            updateUserAvata(img_corner, img_user_avatar, Integer.parseInt(mUserDataNoRealm.getUser_role()), mUserDataNoRealm.getUserAvatar());
            getIsFriend();
        }
    };

    private void updateUserAvata(ImageView imageConner, ImageView ImageAvatar, int UserRole, String avatarURL) {
        imageConner.setVisibility(View.VISIBLE);
        avatarURL = Const.IMG_URL + avatarURL;
        switch (UserRole) {
            case 0:
                Glide.with(UserActivity.this).load(R.drawable.corner_user_default).centerCrop().into(imageConner);
                Glide.with(UserActivity.this).load(avatarURL).placeholder(R.drawable.header_user_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 1:
                Glide.with(UserActivity.this).load(R.drawable.corner_distributor).centerCrop().into(imageConner);
                Glide.with(UserActivity.this).load(avatarURL).placeholder(R.drawable.header_distributor_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 2:
                Glide.with(UserActivity.this).load(R.drawable.corner_enterprise).centerCrop().into(imageConner);
                Glide.with(UserActivity.this).load(avatarURL).placeholder(R.drawable.header_enterprise_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 3:
                Glide.with(UserActivity.this).load(R.drawable.corner_expert).centerCrop().into(imageConner);
                Glide.with(UserActivity.this).load(avatarURL).placeholder(R.drawable.header_expert_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 4:
                Glide.with(UserActivity.this).load(R.drawable.corner_author).centerCrop().into(imageConner);
                Glide.with(UserActivity.this).load(avatarURL).placeholder(R.drawable.header_author_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 5:
                imageConner.setVisibility(View.GONE);
                Glide.with(UserActivity.this).load(R.drawable.corner_user_default).centerCrop().into(imageConner);
                Glide.with(UserActivity.this).load(avatarURL).placeholder(R.drawable.user_media).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 6:
                imageConner.setVisibility(View.GONE);
                Glide.with(UserActivity.this).load(R.drawable.corner_user_default).centerCrop().into(imageConner);
                Glide.with(UserActivity.this).load(avatarURL).placeholder(R.drawable.user_system).dontAnimate().centerCrop().into(ImageAvatar);
                break;
        }
    }

}
