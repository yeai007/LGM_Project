package com.hopeofseed.hopeofseed.Activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.CommentAboutMeRecyclerAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentAboutMeData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommentAboutMeDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.curView.InputPopupWindow;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/5 15:07
 * 修改人：whisper
 * 修改时间：2016/9/5 15:07
 * 修改备注：
 */
public class CommentAboutMe extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "CommentAboutMe";
    RecyclerView recycler_list;
    CommentAboutMeRecyclerAdapter mAdapter;
    ArrayList<CommentAboutMeData> arrCommentAboutMeDataTmp = new ArrayList<>();
    ArrayList<CommentAboutMeData> arrCommentAboutMeData = new ArrayList<>();
    Handler mHandler = new Handler();
    private int PageNo = 0;
    boolean isLoading = false;
    //评论页回复功能
    InputPopupWindow menuWindow;
    String RecordId, CommendUserId, NewId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_about_me);
        initView();
        getData();
    }

    private void getData() {
        Log.e(TAG, "getData: "+String.valueOf(PageNo));
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommentsReceived.php", opt_map, CommentAboutMeDataTmp.class, this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("评论");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(CommentAboutMe.this, LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new CommentAboutMeRecyclerAdapter(CommentAboutMe.this, arrCommentAboutMeData);
        recycler_list.setAdapter(mAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        isLoading = true;
                        PageNo = PageNo + 1;
                        Log.e(TAG, "onScrolled:onrefresh");
                        getData();
                    } else if (arrCommentAboutMeData.size() < 20) {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("commentNew")) {
            CommResultTmp mCommResultTmp = ObjectUtil.cast(rspBaseBean);
            if (Integer.parseInt(mCommResultTmp.getDetail()) > 0) {
                PageNo = 0;
                getData();
            } else {
            }
            // mHandle.post(refeshData);
        } else if(rspBaseBean.RequestSign.equals("GetCommentsReceived")){
            arrCommentAboutMeDataTmp = ((CommentAboutMeDataTmp) ObjectUtil.cast(rspBaseBean)).getDetail();

            mHandler.post(updateList);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arrCommentAboutMeData.clear();
            }
            arrCommentAboutMeData.addAll(arrCommentAboutMeDataTmp);
            mAdapter.notifyDataSetChanged();
        }
    };

    //评论页回复功能
    public void showInput(String recordId, String commendUserID, String newId) {
        RecordId = recordId;
        CommendUserId = commendUserID;
        NewId = newId;
        menuWindow = new InputPopupWindow(CommentAboutMe.this, itemsOnClick);
        //显示窗口
        menuWindow.showAtLocation(getRootView(CommentAboutMe.this), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
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
        AddCommend2Data();

    }

    //添加二级评论
    private void AddCommend2Data() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommentFromNewId", NewId);
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
}
