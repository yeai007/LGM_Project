package com.hopeofseed.hopeofseed.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName:smamoo.Adapter
 * @Desc:
 * @Author:liguangming
 * @Date:2016/5/4
 * @Copyright:2014-2016 Moogeek
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    private boolean scrollble = false;
    List<Fragment> fragmentList = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> tmp_fragmentList) {
        super(fm);
        this.fragmentList = tmp_fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
