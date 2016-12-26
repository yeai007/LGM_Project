package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Adapter.DiscoversGridViewAdapter;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.AppPermissions;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

import static com.hopeofseed.hopeofseed.Application.REQUEST_CODE_LOCATION;

/**
 * 项目名称：liguangming
 * 类描述：发现主页面
 * 创建人：whisper
 * 创建时间：2016/7/27 14:41
 * 修改人：whisper
 * 修改时间：2016/7/27 14:41
 * 修改备注：
 */
public class DiscoverFragment extends Fragment implements NetCallBack, View.OnClickListener {
    private static final String TAG = "DiscoverFragment";
    RecyclerView gv_discover;
    DiscoversGridViewAdapter discoversGridViewAdapter;
    ArrayList<String> arr_title = new ArrayList<>();
    public static final int SELECT_VARIETIES = 0;//找品种
    public static final int SELECT_DISTRIBUTOR = 1;//找经销商
    public static final int SELECT_EXPERT = 2;//找专家
    public static final int SELECT_BUSINESS = 3;//找企业
    public static final int SELECT_AUTHOR = 4;//找机构
    public static final int SELECT_SEED_FRIEND = 5;//找种友
    RelativeLayout rel_search;

    /**
     *
     * */
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    /**
     * 页面集合
     */
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    NearGroupFragment mNearGroupFragment;
    HuoDongFragment mHuoDongFragment;
    RadioButton radio_group;
    RadioButton radio_huodong;
    int page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_discover, null);
        initViewPager(v);
        initData();
        initView(v);
        getPermission();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void getPermission() {
        MPermissions.requestPermissions(DiscoverFragment.this, REQUEST_CODE_LOCATION, AppPermissions.getLocationPermissions());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(REQUEST_CODE_LOCATION)
    public void requestLocationSuccess() {
        //Toast.makeText(this, "GRANT ACCESS LOCATION!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUEST_CODE_LOCATION)
    public void requestLocationFailed() {
        Toast.makeText(getActivity(), "定位服务已经被禁止!", Toast.LENGTH_SHORT).show();
    }

    private void initViewPager(View view) {
        Intent intent = getActivity().getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mNearGroupFragment = new NearGroupFragment();
        mHuoDongFragment = new HuoDongFragment();
        fragmentList.add(mNearGroupFragment);
        fragmentList.add(mHuoDongFragment);
        rg_menu = (RadioGroup) view.findViewById(R.id.rg_menu);
        mainViewPagerAdapter = new MainViewPagerAdapter(getChildFragmentManager(), fragmentList);
        vp_main = (ViewPager) view.findViewById(R.id.vp_main);
        vp_main.addOnPageChangeListener(onPageChangeListener);
        vp_main.setAdapter(mainViewPagerAdapter);
        vp_main.setOffscreenPageLimit(4);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
        vp_main.setCurrentItem(page);
    }

    private void initData() {
        arr_title.add("找品种");
        arr_title.add("附近经销商");
        arr_title.add("找专家");
        arr_title.add("找企业");
        arr_title.add("找机构");
        arr_title.add("找种友");
    }

    private void initView(View v) {
        radio_group = (RadioButton) v.findViewById(R.id.radio_group);
        radio_huodong = (RadioButton) v.findViewById(R.id.radio_huodong);
        rel_search = (RelativeLayout) v.findViewById(R.id.rel_search);
        rel_search.setOnClickListener(listener);
        gv_discover = (RecyclerView) v.findViewById(R.id.gv_discover);
        gv_discover.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        discoversGridViewAdapter = new DiscoversGridViewAdapter(getActivity(), arr_title);
        gv_discover.setAdapter(discoversGridViewAdapter);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_search:
                    Intent intent = new Intent(getActivity(), SearchAcitvity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0://新鲜事
                    vp_main.setCurrentItem(0);
                    radio_group.setChecked(true);
                    break;
                case 1://消息
                    vp_main.setCurrentItem(1);
                    radio_huodong.setChecked(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    //底部菜单方法
    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == R.id.radio_group) {
                vp_main.setCurrentItem(0);
            } else if (checkedId == R.id.radio_huodong) {
                vp_main.setCurrentItem(1);
            }
        }
    };

}
