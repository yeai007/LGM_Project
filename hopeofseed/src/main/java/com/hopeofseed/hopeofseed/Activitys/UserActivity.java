package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXData.FragmentListDatas;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.CategoryTabStrip;
import com.hopeofseed.hopeofseed.util.ListFragmentConfig;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

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
    ImageView img_user_avatar, img_corner;
    TextView tv_username, tv_follow_sum, tv_fans_sum, tv_address, appTitle;
    String UserId;
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();
    RelativeLayout rel_follow, rel_fans;
    ViewPager pager;
    CategoryTabStrip tabs;
    UsersPagerAdapter mUsersPagerAdapter;
    ArrayList<FragmentListDatas> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        Intent intent = getIntent();
        UserId = intent.getStringExtra("userid");
        initView();
        getData();
    }

    private void setFrament() {
        int[] fregments = {6, 7, 8, 9, 10};
        ListFragmentConfig lfc = new ListFragmentConfig();
        fragmentList.addAll(lfc.getCommUser(fregments));
    }

    private void initView() {
        appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setVisibility(View.GONE);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("更多资料");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_follow_sum = (TextView) findViewById(R.id.tv_follow_sum);
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
                Toast.makeText(getApplicationContext(), "OnClick", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_topright:
                switch (Integer.parseInt(mUserDataNoRealm.getUser_role())) {
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
                }

                break;
            case R.id.rel_follow://关注
                intent = new Intent(UserActivity.this, MyFollowed.class);
                intent.putExtra("UserId", UserId);
                startActivity(intent);
                break;
            case R.id.rel_fans:
                intent = new Intent(UserActivity.this, MyFans.class);
                intent.putExtra("UserId", UserId);
                startActivity(intent);
                break;
        }
    }

    private void getUserJpushInfo(String user_name) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (userInfo.getAvatarFile() != null) {
                    Glide.with(getApplicationContext())
                            .load(userInfo.getAvatarFile())
                            .centerCrop()
                            .into(img_user_avatar);
                }
            }
        });
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUserInfoById.php", opt_map, UserDataNoRealmTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        mUserDataNoRealm = ((UserDataNoRealmTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);

        updateView();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: updateview");
            appTitle.setText(mUserDataNoRealm.getNickname());
            tv_username.setText(mUserDataNoRealm.getNickname());
            tv_follow_sum.setText(mUserDataNoRealm.getFllowed_count());
            tv_fans_sum.setText(mUserDataNoRealm.getBeen_fllowed_count());
            tv_address.setText(mUserDataNoRealm.getUserProvince() + " " + mUserDataNoRealm.getUserCity() + " " + mUserDataNoRealm.getUserZone());
            updateCorner();
            getUserJpushInfo(Const.JPUSH_PREFIX + mUserDataNoRealm.getUser_id());

        }
    };

    private void updateCorner() {
        Log.e(TAG, "updateCorner:userclass " + mUserDataNoRealm.getUser_role());

        if (mUserDataNoRealm.getUser_role() != null) {
            switch (Integer.parseInt(mUserDataNoRealm.getUser_role())) {

                case 0:

                    break;
                case 1:
                    img_corner.setImageResource(R.drawable.corner_distributor);
                    break;
                case 2:
                    img_corner.setImageResource(R.drawable.corner_enterprise);
                    break;
                case 3:
                    img_corner.setImageResource(R.drawable.corner_expert);
                    break;
                case 4:
                    img_corner.setImageResource(R.drawable.corner_enterprise);
                    break;
            }
        }
    }
}
