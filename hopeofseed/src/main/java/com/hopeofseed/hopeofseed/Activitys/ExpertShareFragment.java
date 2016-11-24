package com.hopeofseed.hopeofseed.Activitys;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.ExpertDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.NewsListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hopeofseed.hopeofseed.R.id.lv_list;
import static com.hopeofseed.hopeofseed.R.id.lv_news;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/9 19:39
 * 修改人：whisper
 * 修改时间：2016/10/9 19:39
 * 修改备注：
 */
public class ExpertShareFragment extends Fragment implements NetCallBack {
    PullToRefreshListView lv_news;
    ArrayList<ExpertData> arr_ExpertData = new ArrayList<>();
    android.os.Handler mHandler = new android.os.Handler();
    private String StrProvince, StrCity, StrZone, StrPolitic;
    private int PageNo = 0;
    NewsListAdapter newListAadpter;
    ArrayList<NewsData> arr_NewsData = new ArrayList<>();
    ArrayList<NewsData> arr_NewsDataTmp = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_expert_share, null);
        initView(v);
        initNews(v);
        getNewsData();
        return v;
    }


    private void initNews(View v) {
//        getNewsData();
        lv_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_news.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_news.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_news.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    PageNo = 0;
                    getNewsData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_news.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_news.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_news.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    PageNo = PageNo + 1;
                    getNewsData();
                }

            }
        });

    }

    private void initView(View v) {
        lv_news = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        newListAadpter = new NewsListAdapter(getActivity(), arr_NewsData);
        lv_news.setAdapter(newListAadpter);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        lv_news.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), ExpertActivity.class);
            intent.putExtra("ExpertId", String.valueOf(arr_ExpertData.get(i - 1).getExpertId()));
            startActivity(intent);
        }
    };

    public void getNewsData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("StrProvince", StrProvince);
        opt_map.put("StrCity", StrCity);
        opt_map.put("StrZone", StrZone);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrPolitic", StrPolitic);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetExpertShare.php", opt_map, NewsDataTmp.class, this);
    }


    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        arr_NewsDataTmp = ((NewsDataTmp) rspBaseBean).getDetail();
        mHandler.post(runnableNotifyList);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable runnableNotifyList = new Runnable() {
        @Override
        public void run() {
            arr_NewsData.clear();
            arr_NewsData.addAll(arr_NewsDataTmp);
            newListAadpter.notifyDataSetChanged();
            lv_news.onRefreshComplete();
        }
    };

    public void setRefreshData(String... citySelected) {
        StrProvince = citySelected[0];
        StrCity = citySelected[1];
        StrZone = citySelected[2];
        getNewsData();
    }
}
