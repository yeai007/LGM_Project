package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.CommodityRecycleListAdapter;
import com.hopeofseed.hopeofseed.Adapter.StringGridListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXData.StringVariety;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.StringVarietyTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.CategoryTabStripNoPager;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/29 18:58
 * 修改人：whisper
 * 修改时间：2016/10/29 18:58
 * 修改备注：
 */
public class AllCommodityActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, NetCallBack {
    private static final String TAG = "AllCommodityActivity";
    RecyclerView recy_list;
    CommodityRecycleListAdapter mCommodityRecycleListAdapter;
    ArrayList<CommodityData> arr_CommodityData = new ArrayList<>();
    ArrayList<CommodityData> arr_CommodityDataTmp = new ArrayList<>();
    static String Str_search = "";
    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();
    //横向分类
    CategoryTabStripNoPager tabs;
    private ArrayList<String> catalogs = new ArrayList<String>();
    ArrayList<StringVariety> arrStringVarietyTmp = new ArrayList<>();
    ArrayList<StringVariety> arrStringVariety = new ArrayList<>();
    ImageView img_all;
    boolean IsShowAll = false;
    RecyclerView recy_catas;
    StringGridListAdapter mStringGridListAdapter;
    StringVariety first = new StringVariety();
    String UserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_commodity_activity);
        first.setVariety("全部");
        first.setCount("0");
        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserId");
        initView();
        getHotSortData();
        getData();

    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("全部商品");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        catalogs.add("全部");
        img_all = (ImageView) findViewById(R.id.img_all);
        img_all.setOnClickListener(this);
        tabs = (CategoryTabStripNoPager) findViewById(R.id.category_strip);
        tabs.setData(catalogs);
        tabs.setOnSelectListener(new CategoryTabStripNoPager.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                PageNo = 0;
                Log.e(TAG, "updateSelect: " + position);
                Str_search = arrStringVariety.get(position).getVariety();
                getData();
            }
        });

        recy_catas = (RecyclerView) findViewById(R.id.recy_catas);
/*        recy_catas.getBackground().setAlpha(20);*/
        final GridLayoutManager manager1 = new GridLayoutManager(AllCommodityActivity.this, 2);
        recy_catas.setLayoutManager(manager1);
        mStringGridListAdapter = new StringGridListAdapter(AllCommodityActivity.this, arrStringVariety);
        recy_catas.setAdapter(mStringGridListAdapter);
        recy_list = (RecyclerView) findViewById(R.id.recy_list);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);

        final GridLayoutManager manager = new GridLayoutManager(AllCommodityActivity.this, 2);
        recy_list.setLayoutManager(manager);
        mCommodityRecycleListAdapter = new CommodityRecycleListAdapter(AllCommodityActivity.this, arr_CommodityData);
        recy_list.setAdapter(mCommodityRecycleListAdapter);
    }

    private void getHotSortData() {
        Log.e(TAG, "getData: 获取热门分类数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId",UserId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityVforDisEp.php", opt_map, StringVarietyTmp.class, this);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        if (Str_search.equals("全部")) {
            opt_map.put("Str_search", "");
        } else {
            opt_map.put("Str_search", Str_search);
        }
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityByV.php", opt_map, CommodityDataTmp.class, this);
    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_CommodityData.clear();
            }
            arr_CommodityData.addAll(arr_CommodityDataTmp);
            mCommodityRecycleListAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    };
    Runnable updateTabs = new Runnable() {
        @Override
        public void run() {
            if (catalogs.size() > 0) {
                tabs.setData(catalogs);
            }
            //  Str_search = catalogs.get(0);
            arrStringVariety.clear();
            arrStringVariety.add(first);
            arrStringVariety.addAll(arrStringVarietyTmp);
            mStringGridListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                Str_search = "";
                finish();
                break;
            case R.id.img_all:
                setShow();

                break;
        }
    }

    public void setShow() {
        if (IsShowAll) {
            IsShowAll = false;
            recy_catas.setVisibility(View.GONE);
            img_all.clearAnimation();
            Animation anim = new RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setFillAfter(true); // 设置保持动画最后的状态
            anim.setDuration(500); // 设置动画时间
            anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
            img_all.startAnimation(anim);
        } else {
            IsShowAll = true;
            recy_catas.setVisibility(View.VISIBLE);
            img_all.clearAnimation();
            Animation anim = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setFillAfter(true); // 设置保持动画最后的状态
            anim.setDuration(500); // 设置动画时间
            anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
            img_all.startAnimation(anim);
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetCommodityVforDisEp")) {
            StringVarietyTmp mStringVarietyTmp = ObjectUtil.cast(rspBaseBean);
            arrStringVarietyTmp = mStringVarietyTmp.getDetail();
            int sum = 0;
            for (int i = 0; i < arrStringVarietyTmp.size(); i++) {
                catalogs.add(arrStringVarietyTmp.get(i).getVariety() + "(" + arrStringVarietyTmp.get(i).getCount() + ")");
                sum = sum + Integer.parseInt(arrStringVarietyTmp.get(i).getCount());
            }
            catalogs.set(0, "全部" + "(" + String.valueOf(sum) + ")");
            first.setCount(String.valueOf(sum));
            mHandler.post(updateTabs);
        } else {
            CommodityDataTmp mCommodityDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CommodityDataTmp = mCommodityDataTmp.getDetail();
            mHandler.post(updateList);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    public void getDataForOut(String search, int position) {
        Str_search = search;
        getData();
    }
}
