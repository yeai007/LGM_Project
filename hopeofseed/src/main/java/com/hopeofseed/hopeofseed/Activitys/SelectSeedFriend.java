package com.hopeofseed.hopeofseed.Activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.SeedfriendDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import citypickerview.widget.CityPicker;


/**
 * Created by whisper on 2016/10/7.
 */
public class SelectSeedFriend extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SelectSeedFriend";
    private String StrProvince, StrCity, StrZone;
    Button go;
    RecyclerView recycler_list;
    Handler mHandler = new Handler();
    private int PageNo = 0;
    boolean isLoading = false;
    private SwipeRefreshLayout mRefreshLayout;
    SeedfriendDataAdapter mSeedfriendDataAdapter;
    ArrayList<UserDataNoRealm> arr_UserDataNoRealm = new ArrayList<>();
    ArrayList<UserDataNoRealm> arr_UserDataNoRealmTmp = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_seed_friend);
        initView();
        initList();
        getData();

    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("找种友");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CityPicker cityPicker = new CityPicker.Builder(SelectSeedFriend.this).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province(Const.Province)
                        .city(Const.City)
                        .district(Const.Zone)
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(true)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .build();

                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        go.setText("" + citySelected[0] + "  " + citySelected[1] + "  "
                                + citySelected[2]);
                        StrProvince = citySelected[0];
                        StrCity = citySelected[1];
                        StrZone = citySelected[2];
                        PageNo = 0;
                        getData();
                    }
                });
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

    private void getData() {
        Log.e(TAG, "getData: 获取品种数据");
        HashMap<String, String> opt_map = new HashMap<>();
        if(TextUtils.isEmpty(StrProvince))
        {StrProvince="";}
        if(TextUtils.isEmpty(StrCity))
        {StrCity="";}
        if(TextUtils.isEmpty(StrZone))
        {StrZone="";}
        opt_map.put("StrProvince", StrProvince);
        opt_map.put("StrCity", StrCity);
        opt_map.put("StrZone", StrZone);
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetNearBySeedFriend.php", opt_map, UserDataNoRealmTmp.class, new NetCallBack() {
            @Override
            public void onSuccess(RspBaseBean rspBaseBean) {
                UserDataNoRealmTmp mUserDataNoRealmTmp = ObjectUtil.cast(rspBaseBean);
                arr_UserDataNoRealmTmp = mUserDataNoRealmTmp.getDetail();
                mHandler.post(updateList);
            }

            @Override
            public void onError(String error) {
                mRefreshLayout.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onFail() {
                mRefreshLayout.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_UserDataNoRealm.clear();
            }
            arr_UserDataNoRealm.addAll(arr_UserDataNoRealmTmp);
            mSeedfriendDataAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };

    private void initList() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        final LinearLayoutManager manager = new LinearLayoutManager(SelectSeedFriend.this);
        recycler_list.setLayoutManager(manager);
        mSeedfriendDataAdapter = new SeedfriendDataAdapter(SelectSeedFriend.this, arr_UserDataNoRealm);
        recycler_list.setAdapter(mSeedfriendDataAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData();
                    } else {
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
    public void onRefresh() {
        PageNo = 0;
        getData();
    }
}