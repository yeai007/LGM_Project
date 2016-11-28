package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.CommodityListAdapter;
import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/27 11:52
 * 修改人：whisper
 * 修改时间：2016/9/27 11:52
 * 修改备注：
 */
public class EnterpriseCommodityActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "MyCommodity";
    PullToRefreshListView lv_groups;
    CommodityListAdapter groupListAadpter;
    public ArrayList<CommodityData> arr_CommodityData = new ArrayList<>();
    CommodityDataTmp commodityDataTmp = new CommodityDataTmp();
    private static int CREATE_GROUP = 141;
    Button btn_topright;
    int page = 0;
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    CommodityListFragment mCommodityListFragment;
    DistributorListForCommodityFragment mDistributorListForCommodityFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterprise_commodity);
        initView();
        initViewPager();

    }


    private void initViewPager() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mCommodityListFragment = new CommodityListFragment();
        mDistributorListForCommodityFragment = new DistributorListForCommodityFragment();
        fragmentList.add(mCommodityListFragment);
        fragmentList.add(mDistributorListForCommodityFragment);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        vp_main.addOnPageChangeListener(onPageChangeListener);
        vp_main.setAdapter(mainViewPagerAdapter);
        vp_main.setOffscreenPageLimit(2);
        vp_main.setCurrentItem(page);
        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
    }

    //底部菜单方法
    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == (findViewById(R.id.one)).getId()) {
                vp_main.setCurrentItem(0);
            } else if (checkedId == (findViewById(R.id.two)).getId()) {
                vp_main.setCurrentItem(1);
            }
        }
    };
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0://商品
                    vp_main.setCurrentItem(0);
                    break;
                case 1://经销商

                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initGroups() {
        getGroups();
        lv_groups.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                String str = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_groups.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_groups.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_groups.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    getGroups();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_groups.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_groups.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_groups.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    getGroups();
                }

            }
        });
        lv_groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), arr_CommodityData.get(position - 1).getCommodityId(), Toast.LENGTH_SHORT).show();
                Adapter adapter = parent.getAdapter();
                Log.e(TAG, "onItemClick: " + view.getId());
                switch (view.getId()) {
                    case R.id.tv_name:
                        Toast.makeText(getApplicationContext(), "点击了名称", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_distributor_setting:
                        Toast.makeText(getApplicationContext(), "点击了维护经销商", Toast.LENGTH_SHORT).show();
                        break;
                }

             /*   //显示商品详情
                Intent intent = new Intent(MyCommodity.this, CommodityActivity.class);
                intent.putExtra("CommodityId", arr_CommodityData.get(position - 1).getCommodity_id());
                startActivity(intent);*/
            }
        });
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("我的商品");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("添加商品");

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                //添加商品
                intent = new Intent(EnterpriseCommodityActivity.this, AddCommodity.class);
                intent.putExtra("commodityId", "0");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == CREATE_GROUP && resultCode == RESULT_OK) {
            //添加成功
            getGroups();
        }

    }

    private void getGroups() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetMyCommodity.php", opt_map, CommodityDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        commodityDataTmp = ObjectUtil.cast(rspBaseBean);
        updateView();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.arg1 = 1;
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:
                    lv_groups.onRefreshComplete();
                    break;
                case 1:
                    arr_CommodityData.clear();
                    arr_CommodityData.addAll(commodityDataTmp.getDetail());
                    groupListAadpter.notifyDataSetChanged();
                    lv_groups.onRefreshComplete();
                    break;
                case 2:

                    break;
            }
        }
    };
}
