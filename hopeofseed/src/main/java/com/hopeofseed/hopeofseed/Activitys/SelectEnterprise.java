package com.hopeofseed.hopeofseed.Activitys;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.AutoTextViewAdapter;
import com.hopeofseed.hopeofseed.Adapter.EnterpriseAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AutoData;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AutoDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.EnterpriseDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import citypickerview.widget.CityPicker;

import static com.hopeofseed.hopeofseed.R.id.lv_list;

/**
 * Created by whisper on 2016/10/7.
 */
public class SelectEnterprise extends AppCompatActivity implements View.OnClickListener, NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    AutoCompleteTextView actv_busscropt;
    AutoTextViewAdapter mAutoTextDataAdapter;
    private static final String TAG = "SelectEnterprise";
    RecyclerView recycler_list;
    EnterpriseAdapter mEnterpriseAdapter;
    ArrayList<EnterpriseData> arr_EnterpriseData = new ArrayList<>();
    ArrayList<EnterpriseData> arr_EnterpriseDataTmp = new ArrayList<>();
    private int PageNo = 0;
    ArrayList<AutoData> arrAutoData = new ArrayList<>();
    ArrayList<AutoData> arrAutoDataTmp = new ArrayList<>();
    Handler mHandler = new Handler();
    String StrSearch = "";
    private String StrProvince = "", StrCity = "", StrZone = "";
    CityPicker cityPicker;
    boolean isLoading = false;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_enterprise);
        initView();
        initList();
        initAutoData();
        getData();

    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("找企业");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        actv_busscropt = (AutoCompleteTextView) findViewById(R.id.actv_busscropt);
        mAutoTextDataAdapter = new AutoTextViewAdapter(getApplicationContext(), arrAutoData);
        actv_busscropt.setAdapter(mAutoTextDataAdapter);
        actv_busscropt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                Log.e(TAG, "onItemClick: " + (String) obj);
                StrSearch = ObjectUtil.cast(obj);
                PageNo = 0;
                getData();
            }
        });
        actv_busscropt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actv_busscropt.getWindowToken(), 0); //强制隐藏键盘
                    actv_busscropt.clearFocus();

                    getData();

                    return true;
                }
                return false;
            }
        });
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("更新");
        btn_topright.setOnClickListener(this);
        final Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityPicker = new CityPicker.Builder(SelectEnterprise.this).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province(Const.Province)
                        .city(Const.City)
                        .district(Const.Zone)
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(false)
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

    private void initList() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SelectEnterprise.this);
        recycler_list.setLayoutManager(layoutManager);
        mEnterpriseAdapter = new EnterpriseAdapter(getApplicationContext(), arr_EnterpriseData);
        recycler_list.setAdapter(mEnterpriseAdapter);
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
                    }
                }
            }
        });
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrSearch", actv_busscropt.getText().toString().trim());
        opt_map.put("StrProvince", StrProvince);
        opt_map.put("StrCity", StrCity);
        opt_map.put("StrZone", StrZone);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetEnterpriseByBusScrope.php", opt_map, EnterpriseDataTmp.class, this);
    }

    private void initAutoData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCropAutoData.php", opt_map, AutoDataTmp.class, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                getData();
                break;
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetEnterpriseByBusScrope")) {
            EnterpriseDataTmp enterpriseDataTmp = ObjectUtil.cast(rspBaseBean);
            Log.e(TAG, "onSuccess: " + enterpriseDataTmp.toString());
            arr_EnterpriseDataTmp = enterpriseDataTmp.getDetail();
           mHandler.post(updateList);
        } else if (rspBaseBean.RequestSign.equals("GetCropAutoData")) {
            AutoDataTmp mAutoDataTmp = ObjectUtil.cast(rspBaseBean);
            arrAutoDataTmp = mAutoDataTmp.getDetail();
            mHandler.post(refreshAutoData);
        }
    }

    Runnable refreshAutoData = new Runnable() {
        @Override
        public void run() {
            arrAutoData.clear();
            arrAutoData.addAll(arrAutoDataTmp);
            mAutoTextDataAdapter.notifyDataSetChanged();
        }
    };


    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

Runnable updateList=new Runnable() {
    @Override
    public void run() {
        if (PageNo == 0) {
            arr_EnterpriseData.clear();
        }
        arr_EnterpriseData.addAll(arr_EnterpriseDataTmp);
        mEnterpriseAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
        isLoading = false;
    }
};

    @Override
    public void onRefresh() {
        PageNo = 0;
        getData();
        isLoading = false;
    }
}
