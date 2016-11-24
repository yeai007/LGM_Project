package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.NewsImageAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;
import java.text.ParseException;
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
    String NewId;
    TextView user_name, tv_title, send_time;
    ImageView img_corner, img_user;
    TextView tv_content;
    NewsData newsData = new NewsData();
    Handler mHandle = new Handler();
    RecyclerView resultRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_info_activity);
        Intent intent = getIntent();
        NewId = intent.getStringExtra("NEWID");
        initView();
        getNewData();
    }

    private void initView() {
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        user_name = (TextView) findViewById(R.id.user_name);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        send_time = (TextView) findViewById(R.id.send_time);
        img_corner = (ImageView) findViewById(R.id.img_corner);
        img_user = (ImageView) findViewById(R.id.img_user);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
    }

    private void getNewData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NewId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getNewByID.php", opt_map, NewsDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("getNewById")) {
            //获取信息数据结果
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

    Runnable updateTheNewData = new Runnable() {
        @Override
        public void run() {
            user_name.setText(newsData.getNickname());
            tv_title.setText(newsData.getContent());
            String[] arrImage = newsData.getAssimgurl().split(";");
            List<String> images = java.util.Arrays.asList(arrImage);
            if (images.size() == 1) {
                if (TextUtils.isEmpty(images.get(0))) {
                    resultRecyclerView.setVisibility(View.GONE);
                } else {
                    resultRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                resultRecyclerView.setVisibility(View.VISIBLE);
            }
            NewsImageAdapter gridAdapter = new NewsImageAdapter(getApplicationContext(), images);
            resultRecyclerView.setAdapter(gridAdapter);
            updateTime(newsData.getNewcreatetime());
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

        String diffHour = String.valueOf(longDiff[2]);

        String diffMinutes = String.valueOf(longDiff[3]);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }
}
