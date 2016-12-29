package com.hopeofseed.hopeofseed.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.FollowAndFanCount;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.FollowAndFanCountTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.util.UpdateUserAvatar;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.LoginAcitivity;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import io.realm.Realm;

import static com.hopeofseed.hopeofseed.R.id.img_user_avatar;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/27 14:40
 * 修改人：whisper
 * 修改时间：2016/7/27 14:40
 * 修改备注：
 */
public class UserInfoFragment extends Fragment implements NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    public static String UPDATE_USER_INFO = "UPDATE_USER_INFO";
    ImageView tv__avatar, img_corner;
    private static final String TAG = "UserInfoFragment";
    private static int UPDATE_USER_AVATAR = 120;
    TextView tv_username, tv_follow_sum, tv_fans_sum, tv_word_sum;
    RelativeLayout rel_follow, rel_fans, rel_friend_setting,rel_and;
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();
    TextView app_title;
    private UpdateBroadcastReceiver updateBroadcastReceiver;  //刷新列表广播
    private SwipeRefreshLayout mRefreshLayout;
    FollowAndFanCount followAndFanCount = new FollowAndFanCount();
    Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_userinfo, null);
        initView(v);
        initReceiver();
        getData();
        getCountData();
        return v;
    }

    private void initReceiver() {
        // 注册广播接收
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_USER_INFO);    //只有持有相同的action的接受者才能接收此广播
        getActivity().registerReceiver(updateBroadcastReceiver, filter);
    }

    private void initView(View v) {
        app_title = (TextView) v.findViewById(R.id.apptitle);
        app_title.setText("我的");
        tv_username = (TextView) v.findViewById(R.id.tv_username);
        tv_follow_sum = (TextView) v.findViewById(R.id.tv_follow_sum);
        tv_fans_sum = (TextView) v.findViewById(R.id.tv_fans_sum);
        tv_word_sum = (TextView) v.findViewById(R.id.tv_word_sum);
        (v.findViewById(R.id.btn_topleft)).setVisibility(View.GONE);
        (v.findViewById(R.id.btn_topleft)).setOnClickListener(listener);
        (v.findViewById(R.id.btn_topright)).setOnClickListener(listener);
        RelativeLayout rel_accountsetting = (RelativeLayout) v.findViewById(R.id.rel_accountsetting);
        rel_accountsetting.setOnClickListener(listener);
        RelativeLayout rel_securitysetting = (RelativeLayout) v.findViewById(R.id.rel_securitysetting);
        rel_securitysetting.setOnClickListener(listener);
        RelativeLayout rel_signout = (RelativeLayout) v.findViewById(R.id.rel_signout);
        rel_signout.setOnClickListener(listener);
        RelativeLayout rel_add_commodity = (RelativeLayout) v.findViewById(R.id.rel_add_commodity);
        rel_add_commodity.setOnClickListener(listener);
        RelativeLayout rel_expert = (RelativeLayout) v.findViewById(R.id.rel_expert);
        rel_expert.setOnClickListener(listener);
        RelativeLayout rel_push = (RelativeLayout) v.findViewById(R.id.rel_push);
        rel_push.setOnClickListener(listener);
        if (Integer.parseInt(Const.currentUser.user_role) == 0) {
            rel_expert.setVisibility(View.VISIBLE);
        }
        rel_friend_setting = (RelativeLayout) v.findViewById(R.id.rel_friend_setting);
        rel_friend_setting.setOnClickListener(listener);
        rel_fans = (RelativeLayout) v.findViewById(R.id.rel_fans);
        rel_fans.setOnClickListener(listener);
        rel_follow = (RelativeLayout) v.findViewById(R.id.rel_follow);
        rel_follow.setOnClickListener(listener);
        rel_and=(RelativeLayout)v.findViewById(R.id.rel_and);
        rel_and.setOnClickListener(listener);
        tv__avatar = (ImageView) v.findViewById(img_user_avatar);
        tv__avatar.setOnClickListener(listener);
        img_corner = (ImageView) v.findViewById(R.id.img_corner);
        getUserJpushInfo(Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id));
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);

    }

    private void getUserJpushInfo(String user_name) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (userInfo.getAvatarFile() != null) {
                    Glide.with(getActivity())
                            .load(userInfo.getAvatarFile())
                            .centerCrop()
                            .into(tv__avatar);
                }
            }
        });
    }

    private void updateCorner() {
        Log.e(TAG, "updateCorner:userclass " + mUserDataNoRealm.getUser_role());
        img_corner.setVisibility(View.VISIBLE);
        if (mUserDataNoRealm.getUser_role() != null) {
            switch (Integer.parseInt(mUserDataNoRealm.getUser_role())) {

                case 0:
                    Glide.with(getActivity())
                            .load(R.drawable.corner_user_default)
                            .centerCrop()
                            .into(img_corner);
                    break;
                case 1:
                    //  img_corner.setImageResource(R.drawable.corner_distributor);
                    Glide.with(getActivity())
                            .load(R.drawable.corner_distributor)
                            .centerCrop()
                            .into(img_corner);
                    break;
                case 2:
                    //  img_corner.setImageResource(R.drawable.corner_enterprise);
                    Glide.with(getActivity())
                            .load(R.drawable.corner_enterprise)
                            .centerCrop()
                            .into(img_corner);
                    break;
                case 3:
                    // img_corner.setImageResource(corner_expert);
                    Glide.with(getActivity())
                            .load(R.drawable.corner_expert)
                            .centerCrop()
                            .into(img_corner);
                    break;
                case 4:
                    //img_corner.setImageResource(corner_enterprise);
                    Glide.with(getActivity())
                            .load(R.drawable.corner_enterprise)
                            .centerCrop()
                            .into(img_corner);
                    break;
                case 5:
                    // img_corner.setImageResource(corner_expert);
                    img_corner.setVisibility(View.GONE);
                    break;
                case 6:
                    //img_corner.setImageResource(corner_enterprise);
                    img_corner.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Bundle bundle;
            Intent intent;
            switch (v.getId()) {
                case R.id.rel_accountsetting:
                    //修改资料
                    intent = new Intent(getActivity(), AccountSetting.class);
                    intent.putExtra("User", mUserDataNoRealm);
                    startActivity(intent);
                    break;
                case R.id.rel_securitysetting:
                    //帐号安全设置
                    intent = new Intent(getActivity(), SecuritySetting.class);
                    intent.putExtra("User", mUserDataNoRealm);
                    startActivity(intent);
                    break;
                case R.id.rel_seedvariety:
                    //品种维护
                    intent = new Intent(getActivity(), SeedVariety.class);
                    startActivity(intent);
                    break;
                case R.id.rel_add_commodity://商品
                    intent = new Intent(getActivity(), MyCommodity.class);
                    startActivity(intent);
                    break;
                case R.id.rel_signout:
                    //退出当前账号
                    Realm updateRealm = Realm.getDefaultInstance();
                    updateRealm.beginTransaction();//开启事务
                    UserData updateUserData = updateRealm.where(UserData.class)
                            .equalTo("iscurrent", 1)//查询出name为name1的User对象
                            .findFirst();//修改查询出的第一个对象的名字
                    if (updateUserData != null) {
                        updateUserData.setIsCurrent(0);
                    }
                    updateRealm.commitTransaction();
                    intent = new Intent(getActivity(), LoginAcitivity.class);
                    startActivity(intent);
                    JMessageClient.logout();
                    JPushInterface.stopPush(Application.getContext());
                    break;
                case img_user_avatar:
                    intent = new Intent(getActivity(), UpdateUserAvatar.class);
                    startActivityForResult(intent, UPDATE_USER_AVATAR);
                    break;
                case R.id.rel_follow://关注
/*                    intent = new Intent(getActivity(), MyFollowed.class);
                    intent.putExtra("UserId", mUserDataNoRealm.getUser_id());
                    startActivity(intent);*/
                    intent = new Intent(getActivity(), MyFriendSetting.class);
                    intent.putExtra("page", 1);
                    startActivity(intent);
                    break;
                case R.id.rel_fans:
                    intent = new Intent(getActivity(), MyFriendSetting.class);
                    intent.putExtra("page", 2);
                    startActivity(intent);
                    break;
                case R.id.rel_and:
                    intent = new Intent(getActivity(), MyFriendSetting.class);
                    intent.putExtra("page", 0);
                    startActivity(intent);
                    break;
                /**
                 * 申请成为专家
                 * */
                case R.id.rel_expert:
                    intent = new Intent(getActivity(), ApplyExpertActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rel_push:
                    intent = new Intent(getActivity(), CustomPushActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rel_friend_setting:
                    intent = new Intent(getActivity(), MyFriendSetting.class);
                    startActivity(intent);
                    break;
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            getUserJpushInfo(Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id));
        }
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUserInfoById.php", opt_map, UserDataNoRealmTmp.class, this);
    }

    private void getCountData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetFollowAndFanCount.php", opt_map, FollowAndFanCountTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetFollowAndFanCount")) {
            followAndFanCount = ((FollowAndFanCountTmp) rspBaseBean).getDetail();
            mHandler.post(updateCount);
        } else {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            mUserDataNoRealm = ((UserDataNoRealmTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);

            mHandler.post(updateView);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateCount = new Runnable() {
        @Override
        public void run() {
            tv_word_sum.setText(followAndFanCount.getFollowFanCount());
            tv_follow_sum.setText(followAndFanCount.getFollowCount());
            tv_fans_sum.setText(followAndFanCount.getFanCount());
        }
    };

    Runnable updateView = new Runnable() {
        @Override
        public void run() {
            mRefreshLayout.setRefreshing(false);
            tv_username.setText(mUserDataNoRealm.getNickname());
            app_title.setText(mUserDataNoRealm.getNickname());
            updateCorner();
            getUserJpushInfo(Const.JPUSH_PREFIX + mUserDataNoRealm.getUser_id());
        }
    };


    @Override
    public void onRefresh() {
        getData();
    }

    class UpdateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    }

}
