package com.hopeofseed.hopeofseed.util;

import android.os.Bundle;

import com.hopeofseed.hopeofseed.JNXData.FragmentListDatas;
import com.hopeofseed.hopeofseed.SearchFragment.AuthorFragment;
import com.hopeofseed.hopeofseed.SearchFragment.CommdityFragment;
import com.hopeofseed.hopeofseed.SearchFragment.CropFragment;
import com.hopeofseed.hopeofseed.SearchFragment.DistributorFragment;
import com.hopeofseed.hopeofseed.SearchFragment.EnterpriseFragment;
import com.hopeofseed.hopeofseed.SearchFragment.ExperienceFragment;
import com.hopeofseed.hopeofseed.SearchFragment.ExpertFragment;
import com.hopeofseed.hopeofseed.SearchFragment.HuodongFragment;
import com.hopeofseed.hopeofseed.SearchFragment.NowFragment;
import com.hopeofseed.hopeofseed.SearchFragment.ProblemFragment;
import com.hopeofseed.hopeofseed.SearchFragment.SeedfriendFragment;
import com.hopeofseed.hopeofseed.SearchFragment.YieldFragment;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/28 16:56
 * 修改人：whisper
 * 修改时间：2016/10/28 16:56
 * 修改备注：
 */
public class ListFragmentConfig {
    String UserId = "";

    public ArrayList<FragmentListDatas> getCommUser(int[] arr_i, String userId) {
        this.UserId = userId;
        ArrayList<FragmentListDatas> fragmentList = new ArrayList<>();
        for (int i = 0; i < arr_i.length; i++) {
            fragmentList.add(getFragment(arr_i[i]));
        }
        return fragmentList;
    }

    private FragmentListDatas getFragment(int i) {
        FragmentListDatas mData = new FragmentListDatas();
        switch (i) {
            case 0://用户
                SeedfriendFragment mSeedfriendFragment = SeedfriendFragment.newInstance("");
                mData.setMFragment(mSeedfriendFragment);
                mData.setFragmentName("用户");
                break;
            case 1:
                CropFragment mCropFragment =  CropFragment.newInstance("");
                mData.setMFragment(mCropFragment);
                mData.setFragmentName("品种");
                break;
            case 2:
                DistributorFragment mDistributorFragment =  DistributorFragment.newInstance("");
                mData.setMFragment(mDistributorFragment);
                mData.setFragmentName("经销商");
                break;
            case 3:
                ExpertFragment mExpertFragment =  ExpertFragment.newInstance("");
                mData.setMFragment(mExpertFragment);
                mData.setFragmentName("专家");
                break;

            case 4:
                EnterpriseFragment mEnterpriseFragment =  EnterpriseFragment.newInstance("");
                mData.setMFragment(mEnterpriseFragment);
                mData.setFragmentName("企业");
                break;
            case 5:
                AuthorFragment mAuthorFragment = AuthorFragment.newInstance("");
                mData.setMFragment(mAuthorFragment);
                mData.setFragmentName("机构");
                break;
            case 6:
                ExperienceFragment mExperienceFragment = ExperienceFragment.newInstance("", this.UserId);
                mData.setMFragment(mExperienceFragment);
                mData.setFragmentName("农技经验");
                break;
            case 7://实时
                ProblemFragment mProblemFragment = ProblemFragment.newInstance("", this.UserId);
                mData.setMFragment(mProblemFragment);
                mData.setFragmentName("问题反馈");
                break;
            case 8://实时
                YieldFragment mYieldFragment = YieldFragment.newInstance("", this.UserId);
                mData.setMFragment(mYieldFragment);
                mData.setFragmentName("产量表现");
                break;
            case 9:
                NowFragment mNowFragment = NowFragment.newInstance(this.UserId);
                mData.setMFragment(mNowFragment);
                mData.setFragmentName("实时");
                break;

            case 10:
                CommdityFragment mCommdityFragment = CommdityFragment.newInstance(this.UserId);
                mData.setMFragment(mCommdityFragment);
                mData.setFragmentName("商品");
                break;
            case 11:
                HuodongFragment mHuodongFragment = HuodongFragment.newInstance("", this.UserId);
                mData.setMFragment(mHuodongFragment);
                mData.setFragmentName("活动");
                break;
       /*     case 0:
                NowFragment mNowFragment = new NowFragment();
                mData.setMFragment((Fragment) mNowFragment);
                mData.setFragmentName("实时");
                return mData;
            case 0:
                NowFragment mNowFragment = new NowFragment();
                mData.setMFragment((Fragment) mNowFragment);
                mData.setFragmentName("实时");
                return mData;
            break;*/
            case 12:
                ExperienceFragment mExperienceFragment1 =  ExperienceFragment.newInstance("", this.UserId);
                mData.setMFragment(mExperienceFragment1);
                mData.setFragmentName("典型经验");
                break;
        }
        return mData;
    }
}
