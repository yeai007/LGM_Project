package com.hopeofseed.hopeofseed.util;

import com.hopeofseed.hopeofseed.JNXData.FragmentListDatas;
import com.hopeofseed.hopeofseed.JNXData.UserMenu;
import com.hopeofseed.hopeofseed.R;
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
 * 创建时间：2016/12/31 17:22
 * 修改人：whisper
 * 修改时间：2016/12/31 17:22
 * 修改备注：
 */
public class ListUserMenu {
    int UserRole;

    public ArrayList<UserMenu> getCommUser(int[] arr_i) {

        ArrayList<UserMenu> UserMenuList = new ArrayList<>();
        for (int i = 0; i < arr_i.length; i++) {
            UserMenuList.add(getUserMenu(arr_i[i]));
        }
        return UserMenuList;
    }

    private UserMenu getUserMenu(int i) {
        UserMenu item = new UserMenu();
        switch (i) {
            case 0:
                item.setTitle("文字");
                item.setResId(R.drawable.push_word);
                break;
            case 1:
                item.setTitle("活动");
                item.setResId(R.drawable.push_huodong);
                break;
            case 2:
                item.setTitle("农技经验");
                item.setResId(R.drawable.push_exper);
                break;
            case 3:
                item.setTitle("产量表现");
                item.setResId(R.drawable.push_yield);
                break;

            case 4:
                item.setTitle("发问");
                item.setResId(R.drawable.push_problem);
                break;
            case 5:
                item.setTitle("商品");
                item.setResId(R.drawable.push_commodity);
                break;
            case 6:
                item.setTitle("云通知");
                item.setResId(R.drawable.img_push);
                break;
            case 7:
                item.setTitle("统计分析");
                item.setResId(R.drawable.img_report);
                break;
            case 8:
                item.setTitle("更多");
                item.setResId(R.drawable.push_more);
                break;
            case 9:

                break;

            case 10:

                break;
            case 11:

                break;

        }
        return item;
    }
}
