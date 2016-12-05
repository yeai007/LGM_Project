package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.CommentAboutMeRecyclerAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentAboutMeData;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommentAboutMeDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserMessageDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.recy_news;

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
    ArrayList<NewsData> arr_NewsData = new ArrayList<>();
    RecyclerView recycler_list;
    CommentAboutMeRecyclerAdapter mAdapter;
    ArrayList<CommentAboutMeData> arrCommentAboutMeDataTmp = new ArrayList<>();
    ArrayList<CommentAboutMeData> arrCommentAboutMeData = new ArrayList<>();
    Handler mHandler = new Handler();
    private int PageNo = 0;
    boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_about_me);
        initView();
        getData();
    }

    private void getData() {
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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);


        mAdapter = new CommentAboutMeRecyclerAdapter(getApplicationContext(), arrCommentAboutMeData);

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

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == recycler_list.getAdapter().getItemCount()) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        isLoading = true;
                        PageNo = PageNo + 1;
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
        arrCommentAboutMeDataTmp = ((CommentAboutMeDataTmp) ObjectUtil.cast(rspBaseBean)).getDetail();

        mHandler.post(updateList);

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
}
