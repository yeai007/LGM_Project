package com.hopeofseed.hopeofseed.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.Adapter.NewsImageAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsExperienceData;
import com.hopeofseed.hopeofseed.JNXData.NewsHuodongData;
import com.hopeofseed.hopeofseed.JNXData.NewsProblemData;
import com.hopeofseed.hopeofseed.JNXData.NewsYieldData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsExperienceDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsHuodongDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsProblemDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsYieldDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.curView.InputPopupWindow;
import com.lgm.utils.DateTools;
import com.lgm.utils.ObjectUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/14 10:23
 * 修改人：whisper
 * 修改时间：2016/10/14 10:23
 * 修改备注：
 */
public class NewsInfoActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    private static final String TAG = "NewsInfoActivity";
    private static int CLASS_GENERAL = 0;//一般信息
    private static int CLASS_IMG = 1;//图片信息
    private static int CLASSS_VIDIO = 2;//视频信息
    private static int CLASS_EXPERIENSE = 3;//农技经验
    private static int CLASS_YIELD = 4;//分享产量
    private static int CLASS_PROBLEM = 5;//发问
    private static int CLASS_COMMODITY = 6;//商品
    private static int CLASS_HUODONG = 7;//活动
    private static int CLASS_FORSWARD = 8;//转发
    String NEW_ID;
    int NewClass;
    InputPopupWindow menuWindow;
    TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time, tv_title;
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
    String RecordId, CommendUserId;
    NewsExperienceData newsExperienceData = new NewsExperienceData();
    NewsYieldData nNewsYieldData = new NewsYieldData();
    NewsProblemData mNewsProblemData = new NewsProblemData();
    NewsHuodongData mNewsHuodongData = new NewsHuodongData();
    RelativeLayout rel_this_new;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_info_activity);
        Intent intent = getIntent();
        NEW_ID = intent.getStringExtra("NEWID");
        NewClass = intent.getIntExtra("NewClass", 0);
        Log.e(TAG, "onCreate: NewClass" + NewClass);
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
        fragmentList.add(mCommendListFragment);
        fragmentList.add(mForwardListFragment);

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
        switch (NewClass) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByID.php", opt_map, NewsExperienceDataTmp.class, this);
                break;
            case 4:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByID.php", opt_map, NewsYieldDataTmp.class, this);
                break;
            case 5:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByID.php", opt_map, NewsProblemDataTmp.class, this);
                break;
            case 6:
                break;
            case 7:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByID.php", opt_map, NewsHuodongDataTmp.class, this);
                break;
            case 8:
                break;
        }
    }

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("详情");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        img_user = (ImageView) findViewById(R.id.img_user);
        img_corner = (ImageView) findViewById(R.id.img_corner);
        send_time = (TextView) findViewById(R.id.send_time);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_zambia = (TextView) findViewById(R.id.tv_zambia);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rel_forward = (RelativeLayout) findViewById(R.id.rel_forward);
        rel_comment = (RelativeLayout) findViewById(R.id.rel_comment);
        rel_comment.setOnClickListener(this);
        rel_zambia = (RelativeLayout) findViewById(R.id.rel_zambia);
        user_name = (TextView) findViewById(R.id.user_name);
        tv_forward = (TextView) findViewById(R.id.tv_forward);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
        rel_this_new=(RelativeLayout)findViewById(R.id.rel_this_new);
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
            switch (NewClass) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    updateUI(newsExperienceData);
                    break;
                case 4:
                    updateUI(nNewsYieldData);

                    break;
                case 5:
                    updateUI(mNewsProblemData);
                    break;
                case 6:
                    break;
                case 7:
                    updateUI(mNewsHuodongData);
                    break;
                case 8:
                    break;
            }

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
        menuWindow = new InputPopupWindow(NewsInfoActivity.this, itemsOnClick);
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
        if (rspBaseBean.RequestSign.equals("getNewOhterInfoByID")) {
                switch (Integer.parseInt(rspBaseBean.resultNote)) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        NewsExperienceDataTmp newsOhterInfoDataTmp = ObjectUtil.cast(rspBaseBean);
                        newsExperienceData = newsOhterInfoDataTmp.getDetail().get(0);

                        break;
                    case 4:
                        NewsYieldDataTmp newsYieldDataTmp = ObjectUtil.cast(rspBaseBean);
                        nNewsYieldData = newsYieldDataTmp.getDetail().get(0);
                        break;
                    case 5:
                        NewsProblemDataTmp newsProblemDataTmp = ObjectUtil.cast(rspBaseBean);
                        mNewsProblemData = newsProblemDataTmp.getDetail().get(0);
                        break;
                    case 6:
                        break;
                    case 7:
                        NewsHuodongDataTmp newsHuodongDataTmp = ObjectUtil.cast(rspBaseBean);
                        mNewsHuodongData = newsHuodongDataTmp.getDetail().get(0);
                        break;
                    case 8:
                        break;
                }
            mHandle.post(updateTheNewData);
        } else if (rspBaseBean.RequestSign.equals("commentNew")) {
            CommResultTmp mCommResultTmp = ObjectUtil.cast(rspBaseBean);
            if (Integer.parseInt(mCommResultTmp.getDetail()) > 0) {
                Log.e(TAG, "onSuccess: success");
            } else {
                Log.e(TAG, "onSuccess: fail");
            }
            mHandle.post(refeshData);
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

    private void updateUI(NewsExperienceData mNewsExperienceData) {
        tv_title.setText("【农技】【" + mNewsExperienceData.getExperienceVartityName() + "】" + "\n【" + mNewsExperienceData.getTitle() + "】");
        updateTime(mNewsExperienceData.getNewcreatetime());
        tv_content.setText(mNewsExperienceData.getContent().replace("\\n", "\n"));
        user_name.setText(mNewsExperienceData.getNickname());
        String[] arrImage = mNewsExperienceData.getAssimgurl().split(";");
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
        getUserJpushInfo(Const.JPUSH_PREFIX + mNewsExperienceData.getUser_id(), Integer.parseInt(mNewsExperienceData.getUser_role()));
    }

    private void updateUI(NewsYieldData mNewsYieldData) {
        tv_title.setText("【产量表现】【" + mNewsYieldData.getYieldVariety() + "】" + "\n【" + mNewsYieldData.getTitle() + "】");

        updateTime(mNewsYieldData.getNewcreatetime());
        tv_content.setText("总产量：" + mNewsYieldData.getYieldSum() + "\n种植面积：" + mNewsYieldData.getYieldArea() + "\n平均产量：" + mNewsYieldData.getYieldYield() + "\n表现:" + mNewsYieldData.getYieldEssay().replace("\\n", "\n"));
        user_name.setText(mNewsYieldData.getNickname());
        String[] arrImage = mNewsYieldData.getAssimgurl().split(";");
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
        getUserJpushInfo(Const.JPUSH_PREFIX + mNewsYieldData.getUser_id(), Integer.parseInt(mNewsYieldData.getUser_role()));
    }

    private void updateUI(NewsProblemData newsProblemData) {
        tv_title.setText("【问题】【" + newsProblemData.getProblemVarietyName() + "】" + "\n【" + newsProblemData.getTitle() + "】");

        updateTime(newsProblemData.getNewcreatetime());
        tv_content.setText(newsProblemData.getContent().replace("\\n", "\n"));
        user_name.setText(newsProblemData.getNickname());
        String[] arrImage = newsProblemData.getAssimgurl().split(";");
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
        getUserJpushInfo(Const.JPUSH_PREFIX + newsProblemData.getUser_id(), Integer.parseInt(newsProblemData.getUser_role()));
    }

    private void updateUI(NewsHuodongData mmNewsHuodongData) {
        tv_title.setText("【问题】【" + mmNewsHuodongData.getNewclass() + "】" + "\n【" + mmNewsHuodongData.getTitle() + "】");
        tv_title.setText("【活动】" + "\n【" + mmNewsHuodongData.getTitle() + "】");
        updateTime(mmNewsHuodongData.getNewcreatetime());
        tv_content.setText(mmNewsHuodongData.getContent().replace("\\n", "\n"));
        user_name.setText(mmNewsHuodongData.getNickname());
        String[] arrImage = mmNewsHuodongData.getAssimgurl().split(";");
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
        getUserJpushInfo(Const.JPUSH_PREFIX + mmNewsHuodongData.getUser_id(), Integer.parseInt(mmNewsHuodongData.getUser_role()));
    }
}
