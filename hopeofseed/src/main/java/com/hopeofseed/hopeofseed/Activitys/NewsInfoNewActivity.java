package com.hopeofseed.hopeofseed.Activitys;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.CommendListAdapterNew;
import com.hopeofseed.hopeofseed.Adapter.NewsImageAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentDataNew;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXData.NewsExperienceData;
import com.hopeofseed.hopeofseed.JNXData.NewsHuodongData;
import com.hopeofseed.hopeofseed.JNXData.NewsProblemData;
import com.hopeofseed.hopeofseed.JNXData.NewsYieldData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommentDataNewTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsExperienceDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsHuodongDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsProblemDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsYieldDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.curView.InputPopupWindow;
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
public class NewsInfoNewActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    private static final String TAG = "NewsInfoNewActivity";
    private MyHoveringScrollView view_hover;
    NewsData newsData = new NewsData();
    int page = 0;
    String NEW_ID;
    String InfoId;
    ScrollViewListView lv_list;
    CommendListAdapterNew mCommendListAdapter;
    int classid = 1;
    ArrayList<CommentDataNew> arrCommentOrForward = new ArrayList<>();
    ArrayList<CommentDataNew> arrCommentOrForwardTmp = new ArrayList<>();
    Handler mHandle = new Handler();
    TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time, tv_title;
    ImageView img_user, img_corner;
    RecyclerView resultRecyclerView;
    InputPopupWindow menuWindow;
    String RecordId, CommendUserId;
    RelativeLayout rel_forward, rel_comment, rel_zambia, rel_content, rel_share_new;
    int NewClass;
    NewsExperienceData newsExperienceData = new NewsExperienceData();
    NewsYieldData nNewsYieldData = new NewsYieldData();
    NewsProblemData mNewsProblemData = new NewsProblemData();
    NewsHuodongData mNewsHuodongData = new NewsHuodongData();
    RadioButton rb_forward;
    boolean isInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_info_activity_new);
        Intent intent = getIntent();
        isInfo = intent.getBooleanExtra("isInfo", false);
        NewClass = intent.getIntExtra("NewClass", 0);
        initScroll();
        initView();

        if (isInfo) {
            InfoId = intent.getStringExtra("InfoId");
            initDataInfoId();
            getDataInfo();
        } else {
            NEW_ID = intent.getStringExtra("NEWID");
            initData();
            getData();
        }
    }

    private void refreshData() {
        if (isInfo) {
            getDataInfo();
        } else {
            getData();
        }
    }
    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NEW_ID);
        opt_map.put("ClassId", String.valueOf(classid));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommentByNewIdNew.php", opt_map, CommentDataNewTmp.class, this);
    }
    private void getDataInfo() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("InfoId", InfoId);
        opt_map.put("NewClass",String.valueOf(NewClass));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommentByInfoId.php", opt_map, CommentDataNewTmp.class, this);
    }

    private void initDataInfoId() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("InfoId", InfoId);
        opt_map.put("NewClass", String.valueOf(NewClass));
        HttpUtils hu = new HttpUtils();
        switch (NewClass) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByInfoId.php", opt_map, NewsExperienceDataTmp.class, this);
                break;
            case 4:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByInfoId.php", opt_map, NewsYieldDataTmp.class, this);
                break;
            case 5:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByInfoId.php", opt_map, NewsProblemDataTmp.class, this);
                break;
            case 6:
                break;
            case 7:
                hu.httpPost(Const.BASE_URL + "getNewOhterInfoByInfoId.php", opt_map, NewsHuodongDataTmp.class, this);
                break;
            case 8:
                break;
        }
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NEW_ID);
        HttpUtils hu = new HttpUtils();
        switch (NewClass) {
            case 0:
                hu.httpPost(Const.BASE_URL + "getNewByID.php", opt_map, NewsDataTmp.class, this);
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
                hu.httpPost(Const.BASE_URL + "getNewByID.php", opt_map, NewsDataTmp.class, this);
                break;
            default:
                hu.httpPost(Const.BASE_URL + "getNewByID.php", opt_map, NewsDataTmp.class, this);
                break;
        }
    }

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("详情");
        Button btn_topleft = (Button) findViewById(R.id.btn_topleft);
        btn_topleft.setOnClickListener(this);
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
        rel_forward = (RelativeLayout) findViewById(R.id.rel_forward);
        rel_comment = (RelativeLayout) findViewById(R.id.rel_comment);
        rel_comment.setOnClickListener(this);
        rel_forward.setOnClickListener(this);
        rel_zambia = (RelativeLayout) findViewById(R.id.rel_zambia);
        rel_content = (RelativeLayout) findViewById(R.id.rel_content);
        rel_share_new = (RelativeLayout) findViewById(R.id.rel_share_new);
        rel_share_new.setOnClickListener(this);
        rb_forward = (RadioButton) findViewById(R.id.rb_forward);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
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
            case R.id.rel_share_new:
                Log.e(TAG, "onClick: rel_share_new" + newsData.getFromid());
                intent = new Intent(NewsInfoNewActivity.this, NewsInfoNewActivity.class);
                intent.putExtra("NEWID", newsData.getFromid());
                startActivity(intent);

                break;
        }
    }

    private void initScroll() {
        view_hover = (MyHoveringScrollView) findViewById(R.id.view_hover);
        view_hover.setTopView(R.id.top);
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
                refreshData();
            } else {
                Log.e(TAG, "onSuccess: fail");
            }
            // mHandle.post(refeshData);
        } else if (rspBaseBean.RequestSign.equals("GetCommentByNewIdNew")||rspBaseBean.RequestSign.equals("GetCommentByInfoId")) {
            arrCommentOrForwardTmp = ((CommentDataNewTmp) rspBaseBean).getDetail();
            mHandle.post(updatelist);
        } else if (rspBaseBean.RequestSign.equals("getNewOhterInfoByID")||rspBaseBean.RequestSign.equals("getNewOhterInfoByInfoId")) {
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
            mHandle.post(updateTheNewDataOhter);
        } else {
            //获取信息数据结果
            newsData = ((NewsDataTmp) rspBaseBean).getDetail().get(0);
            mHandle.post(updateTheNewDataOhter);
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
            } else {
                tv_title.setVisibility(View.GONE);
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
    Runnable updateTheNewDataOhter = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "updateUI:NewClass " + NewClass);
            switch (NewClass) {
                case 0:
                    updateUI();
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
                    updateUI();
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
        menuWindow = new InputPopupWindow(NewsInfoNewActivity.this, itemsOnClick);
        //显示窗口
        menuWindow.showAtLocation(getRootView(this), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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

    private static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

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

    private void updateUI() {
        rb_forward.setText("评论 " + newsData.getCommentCount());
        updateTime(newsData.getNewcreatetime());
        user_name.setText(newsData.getNickname());
        getUserJpushInfo(Const.JPUSH_PREFIX + newsData.getUser_id(), Integer.parseInt(newsData.getUser_role()));
        String[] arrImage = newsData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        if (newsData.getNewclass().equals("8")) {
            tv_title.setText(newsData.getForwardComment());
            tv_title.setVisibility(View.VISIBLE);
            rel_content.setVisibility(View.GONE);
            rel_share_new.setVisibility(View.VISIBLE);
            ImageView img_share_new = (ImageView) findViewById(R.id.img_share_new);
            TextView tv_share_new_content = (TextView) findViewById(R.id.tv_share_new_content);
            TextView tv_share_new_title = (TextView) findViewById(R.id.tv_share_new_title);
            if (images.size() > 0) {
                Glide.with(getApplicationContext())
                        .load(Const.IMG_URL + arrImage[0])
                        .centerCrop()
                        .into(img_share_new);
            } else {
            }
            tv_share_new_title.setText(newsData.getTitle());
            if (newsData.getTitle().equals(newsData.getContent())) {
                tv_share_new_content.setVisibility(View.GONE);
            } else {
                tv_share_new_content.setText(newsData.getContent());

            }


        } else {
            tv_title.setVisibility(View.GONE);
            tv_content.setText(newsData.getContent());
            tv_content.setSingleLine(false);
            tv_content.setMaxLines(3);
            tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

            resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
            resultRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
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
        }
        view_hover.setTopView(R.id.top);
    }

    private void updateUI(NewsExperienceData mNewsExperienceData) {
        rb_forward.setText("评论 " + mNewsExperienceData.getCommentCount());
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
        view_hover.setTopView(R.id.top);
    }

    private void updateUI(NewsYieldData mNewsYieldData) {
        rb_forward.setText("评论 " + mNewsYieldData.getCommentCount());
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
        view_hover.setTopView(R.id.top);
    }

    private void updateUI(NewsProblemData newsProblemData) {
        rb_forward.setText("评论 " + newsProblemData.getCommentCount());
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
        view_hover.setTopView(R.id.top);
    }

    private void updateUI(NewsHuodongData mmNewsHuodongData) {
        rb_forward.setText("评论 " + mmNewsHuodongData.getCommentCount());
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
        view_hover.setTopView(R.id.top);
    }

}
