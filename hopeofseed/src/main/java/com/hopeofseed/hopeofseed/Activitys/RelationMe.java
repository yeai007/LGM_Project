package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.NewsListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.GetNews;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/5 15:05
 * 修改人：whisper
 * 修改时间：2016/9/5 15:05
 * 修改备注：
 */
public class RelationMe extends AppCompatActivity implements View.OnClickListener {
    ArrayList<NewsData> arr_NewsData = new ArrayList<>();
    NewsListAdapter newListAadpter;
    PullToRefreshListView lv_news;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_me_activity);
        initView();
        initNews();
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("@我的");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
    }

    private void initNews() {
        lv_news = (PullToRefreshListView) findViewById(R.id.lv_news);
        newListAadpter = new NewsListAdapter(getApplicationContext(),arr_NewsData);
        lv_news.setAdapter(newListAadpter);
        lv_news.setMode(PullToRefreshBase.Mode.BOTH);
        getNewsData();
        lv_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                String str = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_news.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_news.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_news.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    getNewsData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_news.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_news.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_news.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    getNewsData();
                }

            }
        });
    }

    public void getNewsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetNews getNews = new GetNews();
                getNews.UserId = String.valueOf(Const.currentUser.user_id);
                Boolean bRet = getNews.RunData();
                Message msg = getNewsHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.obj = getNews.retRows;
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler getNewsHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:
                    lv_news.onRefreshComplete();
                    break;
                case 1:
                    ArrayList<NewsData> arr_NewsData_tmp = new ArrayList<>();
                    arr_NewsData_tmp = ObjectUtil.cast(msg.obj);
                    arr_NewsData.clear();
                    arr_NewsData.addAll(arr_NewsData_tmp);
                    newListAadpter.notifyDataSetChanged();
                    lv_news.onRefreshComplete();
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }
}
