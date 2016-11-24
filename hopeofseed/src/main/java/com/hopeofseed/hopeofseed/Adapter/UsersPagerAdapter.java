package com.hopeofseed.hopeofseed.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.hopeofseed.hopeofseed.JNXData.FragmentListDatas;
import com.hopeofseed.hopeofseed.SearchFragment.NowFragment;

import com.hopeofseed.hopeofseed.SearchFragment.SeedfriendFragment;


import java.util.ArrayList;
import java.util.List;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/28 16:05
 * 修改人：whisper
 * 修改时间：2016/10/28 16:05
 * 修改备注：
 */
public class UsersPagerAdapter extends FragmentPagerAdapter {
    private static final String ARG_POSITION = "position";
    private final List<String> catalogs = new ArrayList<String>();
    private List<FragmentListDatas> mList;

    public UsersPagerAdapter(FragmentManager fm, ArrayList<FragmentListDatas> mData) {
        super(fm);
        catalogs.add("用户");
        this.mList = mData;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getFragmentName();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mFragment = null;
        mFragment = mList.get(position).getMFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        mFragment.setArguments(b);
        return mFragment;
    }

    @Override
    // To update fragment in ViewPager, we should override getItemPosition() method,
    // in this method, we call the fragment's public updating method.
    public int getItemPosition(Object object) {
        Log.d(TAG, "getItemPosition(" + object.getClass().getSimpleName() + ")");
        if (object instanceof SeedfriendFragment) {
//            ((SeedfriendFragment) object).Search(StrSearch);
        }
        return super.getItemPosition(object);
    }
}
