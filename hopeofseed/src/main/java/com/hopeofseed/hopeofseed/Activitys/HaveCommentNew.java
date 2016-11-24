package com.hopeofseed.hopeofseed.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.Adapter.NewsImageAdapter;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.Adapter.CommentForwarAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentOrForward;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommentOrForwardTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.curView.InputPopupWindow;
import com.hopeofseed.hopeofseed.curView.pulishDYNPopupWindow;
import com.lgm.utils.DateTools;
import com.lgm.utils.ObjectUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.hopeofseed.hopeofseed.R.id.vp_main;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/16 12:14
 * 修改人：whisper
 * 修改时间：2016/10/16 12:14
 * 修改备注：
 */
public class HaveCommentNew extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "HaveCommentNew";
    String NEW_ID;
    InputPopupWindow menuWindow;
    NewsData newsData = new NewsData();
    TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time;
    RelativeLayout rel_forward, rel_comment, rel_zambia;
    ImageView img_user, img_corner;
    RecyclerView resultRecyclerView;
    Handler mHandle = new Handler();
    private RadioGroup rg_menu;
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    int page = 0;
    List<Fragment> fragmentList;
    ForwardListFragment mForwardListFragment;
    CommendListFragment mCommendListFragment;
    EditText lin_input;
    String RecordId, CommendUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.have_comment_new_activity);
        Intent intent = getIntent();
        NEW_ID = intent.getStringExtra("NEWID");
        Log.e(TAG, "onCreate: " + NEW_ID);

        initView();
        initViewPager();
        initData();
    }

    private void initViewPager() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mForwardListFragment = new ForwardListFragment(NEW_ID);
        mCommendListFragment = new CommendListFragment(NEW_ID);
        fragmentList.add(mForwardListFragment);
        fragmentList.add(mCommendListFragment);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_main = (ViewPager) findViewById(R.id.vp_list);
        vp_main.addOnPageChangeListener(onPageChangeListener);
        vp_main.setAdapter(mainViewPagerAdapter);
        vp_main.setOffscreenPageLimit(2);
        vp_main.setCurrentItem(page);
        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NEW_ID);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getNewByID.php", opt_map, NewsDataTmp.class, this);
    }


    private void initView() {
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);


        img_user = (ImageView) findViewById(R.id.img_user);
        img_corner = (ImageView) findViewById(R.id.img_corner);
        send_time = (TextView) findViewById(R.id.send_time);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_zambia = (TextView) findViewById(R.id.tv_zambia);
        rel_forward = (RelativeLayout) findViewById(R.id.rel_forward);
        rel_comment = (RelativeLayout) findViewById(R.id.rel_comment);
        rel_comment.setOnClickListener(this);
        rel_zambia = (RelativeLayout) findViewById(R.id.rel_zambia);
        user_name = (TextView) findViewById(R.id.user_name);
        tv_forward = (TextView) findViewById(R.id.tv_forward);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
    }

    //底部菜单方法
    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == (findViewById(R.id.rb_forwar)).getId()) {
                vp_main.setCurrentItem(0);
            } else if (checkedId == (findViewById(R.id.rb_comment)).getId()) {
                vp_main.setCurrentItem(1);
            }
        }
    };


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.rel_comment:
                intent = new Intent(getApplicationContext(), CommentNew.class);
                intent.putExtra("NEWID", NEW_ID);
                startActivity(intent);
                break;
            case R.id.rel_forward:
                intent = new Intent(getApplicationContext(), ForwardNew.class);
                intent.putExtra("NEWID", NEW_ID);
                startActivity(intent);
                break;
        }
    }


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0://列表显示
                    vp_main.setCurrentItem(0);
                    break;
                case 1://地图显示

                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    Runnable updateTheNewData = new Runnable() {
        @Override
        public void run() {

            updateTime(newsData.getNewcreatetime());
            tv_content.setText(newsData.getContent());
            user_name.setText(newsData.getNickname());
            String[] arrImage = newsData.getAssimgurl().split(";");
            List<String> images = java.util.Arrays.asList(arrImage);
            resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
            resultRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
            Log.e(TAG, "getView: imagessizi" + images.size());

            if (images.size() == 1) {
                if (TextUtils.isEmpty(images.get(0))) {
                    resultRecyclerView.setVisibility(View.GONE);
                } else {
                    resultRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                resultRecyclerView.setVisibility(View.VISIBLE);
            }
            NewsImageAdapter gridAdapter1 = new NewsImageAdapter(getApplicationContext(), images);
            resultRecyclerView.setAdapter(gridAdapter1);
            getUserJpushInfo(Const.JPUSH_PREFIX + newsData.getUser_id(), Integer.parseInt(newsData.getUser_role()));
        }
    };

    private void updateTime(String time) {
        Long[] longDiff = null;
        String NowTime = null;
        try {
            NowTime = DateTools.getNowTime();
            longDiff = DateTools.getDiffTime(NowTime, time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String diffDay = String.valueOf(longDiff[1]);
        Log.e(TAG, "getView: diffDay:" + diffDay);
        String diffHour = String.valueOf(longDiff[2]);
        Log.e(TAG, "getView: diffHour:" + diffHour);
        String diffMinutes = String.valueOf(longDiff[3]);
        Log.e(TAG, "getView: diffHour:" + diffMinutes);
        if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) == 0) {
            if (Integer.parseInt(diffMinutes) < 5) {
                send_time.setText("刚刚");
            } else {
                send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            send_time.setText(diffHour + "小时前");
        } else {
            send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }

    private void getUserJpushInfo(String user_name, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                switch (user_role) {
                    case 0:
                        Glide.with(getApplicationContext())
                                .load(R.drawable.corner_user_default)
                                .centerCrop()
                                .into(img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.header_user_default)
                                    .centerCrop()
                                    .into(img_user);

                        } else {
                            Glide.with(getApplicationContext())
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(img_user);
                        }
                        break;
                    case 1:
                        Glide.with(getApplicationContext())
                                .load(R.drawable.corner_distributor)
                                .centerCrop()
                                .into(img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.header_distributor_default)
                                    .centerCrop()
                                    .into(img_user);

                        } else {
                            Glide.with(getApplicationContext())
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(img_user);
                        }
                        break;
                    case 2:
                        Glide.with(getApplicationContext())
                                .load(R.drawable.corner_enterprise)
                                .centerCrop()
                                .into(img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.header_enterprise_default)
                                    .centerCrop()
                                    .into(img_user);

                        } else {
                            Glide.with(getApplicationContext())
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(img_user);
                        }
                        break;
                    case 3:
                        Glide.with(getApplicationContext())
                                .load(R.drawable.corner_expert)
                                .centerCrop()
                                .into(img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.header_expert_default)
                                    .centerCrop()
                                    .into(img_user);

                        } else {
                            Glide.with(getApplicationContext())
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(img_user);
                        }
                        break;
                    case 4:
                        Glide.with(getApplicationContext())
                                .load(R.drawable.corner_author)
                                .centerCrop()
                                .into(img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.header_author_default)
                                    .centerCrop()
                                    .into(img_user);

                        } else {
                            Glide.with(getApplicationContext())
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(img_user);
                        }
                        break;
                }

            }
        });
    }

    public void showInput(String recordId, String commendUserID) {
        RecordId = recordId;
        CommendUserId = commendUserID;
        menuWindow = new InputPopupWindow(HaveCommentNew.this, itemsOnClick);
        //显示窗口
        menuWindow.showAtLocation(getRootView(this), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                    submitCommend();
                    menuWindow.dismiss();
                    break;
            }

        }

    };

    private void submitCommend() {
        Toast.makeText(getApplicationContext(), RecordId, Toast.LENGTH_SHORT).show();
        AddCommend2Data();

    }

    //添加二级评论
    private void AddCommend2Data() {


        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommentFromNewId", NEW_ID);
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("RecordId", RecordId);
        opt_map.put("CommentFromUser", CommendUserId);
        opt_map.put("Comment", menuWindow.getInput());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "CommentNew.php", opt_map, CommResultTmp.class, this);
/**
 * 二级评论
 * */
    /*    HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NEW_ID);
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("RecordId", RecordId);
        opt_map.put("CommendUserId", CommendUserId);
        opt_map.put("Commend", menuWindow.getInput());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddNewCommend2Data.php", opt_map, CommResultTmp.class, this);*/
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {

        if (rspBaseBean.RequestSign.equals("commentNew")) {
            CommResultTmp mCommResultTmp = ObjectUtil.cast(rspBaseBean);
            if (Integer.parseInt(mCommResultTmp.getDetail()) > 0) {
                Log.e(TAG, "onSuccess: success");
            } else {
                Log.e(TAG, "onSuccess: fail");
            }
            mHandle.post(refeshData);
        } else {
            //获取信息数据结果
            Log.e(TAG, "onSuccess: " + newsData);
            newsData = ((NewsDataTmp) rspBaseBean).getDetail().get(0);
            mHandle.post(updateTheNewData);
        }
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
    }

    @Override
    public void onFail() {

    }

    Runnable refeshData = new Runnable() {
        @Override
        public void run() {
            mCommendListFragment.onrefreshList();
        }
    };
}
