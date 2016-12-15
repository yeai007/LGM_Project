package com.hopeofseed.hopeofseed.Activitys;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.DistributorAdapter;
import com.hopeofseed.hopeofseed.Adapter.ExperienceAdapter;
import com.hopeofseed.hopeofseed.Adapter.ExpertDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.ExperienceData;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import static com.hopeofseed.hopeofseed.R.id.lv_list;
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
public class NearExpertFragment extends Fragment implements NetCallBack {
    RecyclerView recycler_list;
    ExpertDataAdapter mExpertDataAdapter;
    ArrayList<ExpertData> arrExpertData= new ArrayList<>();
    android.os.Handler mHandler = new android.os.Handler();
    private String StrProvince, StrCity, StrZone, StrPolitic;
    private int PageNo = 0;
    ArrayList<ExpertData> arrExpertDataTmp = new ArrayList<>();
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_expert_near, null);
        initView(v);
        getData();
        return v;
    }

    private void initView(View v) {
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mExpertDataAdapter = new ExpertDataAdapter(getActivity(), arrExpertData);
        recycler_list.setAdapter(mExpertDataAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        Log.e(TAG, "onScrolled: loadingmaore");
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData();
                    } else if (arrExpertDataTmp.size() < 20) {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }

    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), ExpertActivity.class);
            intent.putExtra("ExpertId", String.valueOf(arrExpertData.get(i - 1).getExpertId()));
            startActivity(intent);
        }
    };

    private void getData() {
        Log.e(TAG, "getData: 获取品种数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("StrProvince", StrProvince);
        opt_map.put("StrCity", StrCity);
        opt_map.put("StrZone", StrZone);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrPolitic", StrPolitic);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetNearByExpert.php", opt_map, ExpertDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        arrExpertDataTmp = ((ExpertDataTmp) rspBaseBean).getDetail();
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
            if (PageNo == 0) {
                arrExpertData.clear();
            }
            arrExpertData.addAll(arrExpertDataTmp);
            Log.e(TAG, "run: notity");
            mExpertDataAdapter.notifyDataSetChanged();
        }
    };

    public void setRefreshData(String... citySelected) {
        StrProvince = citySelected[0];
        StrCity = citySelected[1];
        StrZone = citySelected[2];
        getData();
    }
}
