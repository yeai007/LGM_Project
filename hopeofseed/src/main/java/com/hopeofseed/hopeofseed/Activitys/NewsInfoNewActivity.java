package com.hopeofseed.hopeofseed.Activitys;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.CommendListAdapterNew;
import com.hopeofseed.hopeofseed.Adapter.NewsImageAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentDataNew;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommentDataNewTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsExperienceDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsHuodongDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsProblemDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsYieldDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.MyHoveringScrollView;
import com.hopeofseed.hopeofseed.ui.ScrollViewListView;
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
public class NewsInfoNewActivity extends AppCompatActivity implements NetCallBack {
    private static final String TAG = "NewsInfoNewActivity";
    private MyHoveringScrollView view_hover;
    NewsData newsData = new NewsData();
    private ViewPager vp_main;
    private RadioGroup rg_menu;
    int page = 0;
    String NEW_ID;
    ScrollViewListView lv_list;
    CommendListAdapterNew mCommendListAdapter;
    int classid = 1;
    ArrayList<CommentDataNew> arrCommentOrForward = new ArrayList<>();
    ArrayList<CommentDataNew> arrCommentOrForwardTmp = new ArrayList<>();
    Handler mHandle = new Handler();

    TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time, tv_title;
    ImageView img_user, img_corner;
    RecyclerView resultRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_info_activity_new);
        Intent intent = getIntent();
        NEW_ID = intent.getStringExtra("NEWID");
        initScroll();
        initView();
        getData();
        initData();
    }
    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NEW_ID);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getNewByID.php", opt_map, NewsDataTmp.class, this);
    }
    private void initView() {
        lv_list = (ScrollViewListView) findViewById(R.id.lv_list);
        //lv_list.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mCommendListAdapter = new CommendListAdapterNew(NewsInfoNewActivity.this, arrCommentOrForward);
        lv_list.setAdapter(mCommendListAdapter);
        //   lv_list.setOnItemClickListener(myListener);
        img_user = (ImageView) findViewById(R.id.img_user);
        img_corner = (ImageView) findViewById(R.id.img_corner);
        send_time = (TextView) findViewById(R.id.send_time);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_zambia = (TextView) findViewById(R.id.tv_zambia);
        tv_title = (TextView) findViewById(R.id.tv_title);
        user_name = (TextView) findViewById(R.id.user_name);
    }

    private void initScroll() {
        view_hover = (MyHoveringScrollView) findViewById(R.id.view_hover);
        view_hover.setTopView(R.id.top);
    }
    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NEW_ID);
        opt_map.put("ClassId", String.valueOf(classid));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommentByNewIdNew.php", opt_map, CommentDataNewTmp.class, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
           // mHandle.post(refeshData);
        }else if(rspBaseBean.RequestSign.equals("GetCommentByNewIdNew"))
        {
            arrCommentOrForwardTmp = ((CommentDataNewTmp) rspBaseBean).getDetail();
            mHandle.post(updatelist);
        }
        else {
            //获取信息数据结果
            Log.e(TAG, "onSuccess: " + newsData);
            newsData = ((NewsDataTmp) rspBaseBean).getDetail().get(0);
            mHandle.post(updateTheNewData);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            arrCommentOrForward.clear();
            arrCommentOrForward.addAll(arrCommentOrForwardTmp);
            mCommendListAdapter.notifyDataSetChanged();
            //  lv_list.onRefreshComplete();
        }
    };
    Runnable updateTheNewData = new Runnable() {
        @Override
        public void run() {
            updateTime(newsData.getNewcreatetime());
            if (newsData.getNewclass().equals("8")) {
                tv_title.setText(newsData.getForwardComment());
                tv_title.setVisibility(View.VISIBLE);

            }
            tv_content.setText(newsData.getContent());
            tv_content.setSingleLine(false);
            tv_content.setMaxLines(3);
            tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

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
}
