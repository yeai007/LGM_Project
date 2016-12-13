package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import com.hopeofseed.hopeofseed.Adapter.SearchAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.HotSearchData;
import com.hopeofseed.hopeofseed.JNXData.HotSearchDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.BeanTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.HotSearchDataTmp;
import com.hopeofseed.hopeofseed.SearchFragment.AuthorFragment;
import com.hopeofseed.hopeofseed.SearchFragment.CropFragment;
import com.hopeofseed.hopeofseed.SearchFragment.DistributorFragment;
import com.hopeofseed.hopeofseed.SearchFragment.EnterpriseFragment;
import com.hopeofseed.hopeofseed.SearchFragment.ExperienceFragment;
import com.hopeofseed.hopeofseed.SearchFragment.ExpertFragment;
import com.hopeofseed.hopeofseed.SearchFragment.NowFragment;
import com.hopeofseed.hopeofseed.SearchFragment.ProblemFragment;
import com.hopeofseed.hopeofseed.SearchFragment.SeedfriendFragment;
import com.hopeofseed.hopeofseed.SearchFragment.YieldFragment;
import com.hopeofseed.hopeofseed.model.BeanNoRealm;
import com.hopeofseed.hopeofseed.ui.CategoryTabStrip;
import com.hopeofseed.hopeofseed.ui.SearchView;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.model.Bean;
import com.lgm.utils.ObjectUtil;
import java.util.ArrayList;
import android.support.v4.app.FragmentActivity;
import java.util.HashMap;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import static android.text.TextUtils.isEmpty;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class SearchAcitvity extends FragmentActivity implements SearchView.SearchViewListener, NetCallBack {
    private static final String STR_SEARCH = "STR_SEARCH";
    private static final String STR_FAXIAN = "STR_FAXIAN";
    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;

    private ArrayList<BeanNoRealm> dbData = new ArrayList<>();

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<BeanNoRealm> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    private String StrSearch = "";
    private static final String ARG_POSITION = "position";

    public static void setHintSize(int hintSize) {
        SearchAcitvity.hintSize = hintSize;
    }

    Realm myRealm = Realm.getDefaultInstance();
    private CategoryTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_activity);
        Intent intent = getIntent();
        String SearchTmp = intent.getStringExtra("StrSearch");
        if (!isEmpty(SearchTmp)) {
            StrSearch = SearchTmp;
        }
        initData();
        initViews();
        updateHintData();
        updateData();
        initPager();

    }
    private void initPager() {
        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);

    }


    /**
     * 初始化视图
     */
    private void initViews() {

        searchView = (SearchView) findViewById(R.id.main_search_layout);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
        if (!StrSearch.equals("")) {
            searchView.setEtInput(StrSearch);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //从数据库获取数据
        getDbData();
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        getResultData(null);
    }

    /**
     * 获取db 数据
     */
    private void getDbData() {
        dbData.clear();
        List<Bean> mlist = null;
        mlist = myRealm.where(Bean.class).findAll();
        for (int i = 0; i < mlist.size(); i++) {
            BeanNoRealm bnr = new BeanNoRealm();
            bnr.setTagID(mlist.get(i).getTagID());
            bnr.setTagName(mlist.get(i).getTagName());
            bnr.setSecondName(mlist.get(i).getSecondName());
            bnr.setTagClass(mlist.get(i).getTagClass());
            bnr.setTagLevel(mlist.get(i).getTagLevel());
            bnr.setMemo(mlist.get(i).getMemo());
            bnr.setHotLevel(mlist.get(i).getHotLevel());
            bnr.setHotOrder(mlist.get(i).getHotOrder());
            bnr.setTagAvatar(mlist.get(i).getTagAvatar());
            dbData.add(bnr);
        }

    }

    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {

        hintData = new ArrayList<>();
        List<HotSearchData> mlist = null;
        mlist = myRealm.where(HotSearchData.class).findAll();
        for (int i = 0; i < mlist.size(); i++) {
            HotSearchDataNoRealm bnr = new HotSearchDataNoRealm();
            bnr.setHotSearchId(mlist.get(i).getHotSearchId());
            bnr.setHotSearchLevel(mlist.get(i).getHotSearchLevel());
            bnr.setHotSearchOrder(mlist.get(i).getHotSearchOrder());
            bnr.setHotSearchStr(mlist.get(i).getHotSearchStr());
            hintData.add(bnr.getHotSearchStr());
        }

        hintAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, hintData);

    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                BeanNoRealm itemBean = new BeanNoRealm();
                itemBean = dbData.get(i);
                if (itemBean.getTagName().contains(text.trim())) {
                    autoCompleteData.add(itemBean.getTagName());
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text) {
        Log.e(TAG, "getData: 获取搜索库数据");
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     *
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {
        StrSearch = text;
        SeedfriendFragment mSeedfriendFragment = (SeedfriendFragment) adapter.instantiateItem(pager, 0);
        mSeedfriendFragment.Search(StrSearch);
        CropFragment mCropFragment = (CropFragment) adapter.instantiateItem(pager, 1);
        mCropFragment.Search(StrSearch);
        DistributorFragment mDistributorFragment = (DistributorFragment) adapter.instantiateItem(pager, 2);
        mDistributorFragment.Search(StrSearch);
        ExpertFragment mExpertFragment = (ExpertFragment) adapter.instantiateItem(pager, 3);
        mExpertFragment.Search(StrSearch);
        EnterpriseFragment mEnterpriseFragment = (EnterpriseFragment) adapter.instantiateItem(pager, 4);
        mEnterpriseFragment.Search(StrSearch);
        AuthorFragment mAuthorFragment = (AuthorFragment) adapter.instantiateItem(pager, 5);
        mAuthorFragment.Search(StrSearch);
        ExperienceFragment mExperienceFragment = (ExperienceFragment) adapter.instantiateItem(pager, 6);
        mExperienceFragment.Search(StrSearch);
        ProblemFragment mProblemFragment = (ProblemFragment) adapter.instantiateItem(pager, 7);
        mProblemFragment.Search(StrSearch);
        YieldFragment mYieldFragment = (YieldFragment) adapter.instantiateItem(pager, 8);
        mYieldFragment.Search(StrSearch);

    }

    /**
     * **********更新搜索库数据************
     */
    private void updateHintData() {
        Log.e(TAG, "getData: 获取搜索库数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetHotSearchData.php", opt_map, HotSearchDataTmp.class, this);
    }

    private void updateHintRealmData(RspBaseBean rspBaseBean) {
        HotSearchDataTmp mHotSearchDataTmp = ObjectUtil.cast(rspBaseBean);
        Realm insertRealm = Realm.getDefaultInstance();
        RealmResults<HotSearchData> results_del = insertRealm.where(HotSearchData.class).findAll();
        insertRealm.beginTransaction();
        results_del.deleteAllFromRealm();
        insertRealm.commitTransaction();
        for (HotSearchData o : mHotSearchDataTmp.getDetail()) {
            insertRealm.beginTransaction();
            HotSearchData mHotSearchData = insertRealm.copyToRealmOrUpdate(o);
            insertRealm.commitTransaction();
        }
    }

    private void updateData() {
        Log.e(TAG, "getData: 获取搜索库数据AppTags");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetAppTags.php", opt_map, BeanTmp.class, this);
    }

    private void updateRealmData(RspBaseBean rspBaseBean) {
        BeanTmp mBeanTmp = ObjectUtil.cast(rspBaseBean);
        Realm insertRealm = Realm.getDefaultInstance();
        RealmResults<Bean> results_del = insertRealm.where(Bean.class).findAll();
        insertRealm.beginTransaction();
        results_del.deleteAllFromRealm();
        insertRealm.commitTransaction();
        for (Bean o : mBeanTmp.getDetail()) {
            insertRealm.beginTransaction();
            Bean mBean = insertRealm.copyToRealmOrUpdate(o);
            insertRealm.commitTransaction();
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
/*        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());*/
        if (rspBaseBean.RequestSign.equals("GetAppTags")) {
            //获取到AppTags数据
            Log.e(TAG, "onSuccess: 获取到了AppTags" );
            updateRealmData(rspBaseBean);
        } else if (rspBaseBean.RequestSign.equals("GetSearchResult")) {
            updateView();
        } else if (rspBaseBean.RequestSign.equals("GetHotSearchData")) {
            updateHintRealmData(rspBaseBean);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final List<String> catalogs = new ArrayList<String>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            catalogs.add("用户");
            catalogs.add("品种");
            catalogs.add("经销商");
            catalogs.add("专家分享");
            catalogs.add("企业");
            catalogs.add("机构");
            catalogs.add("农技");
            catalogs.add("发问");
            catalogs.add("产量表现");
            //catalogs.add("实时");

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catalogs.get(position);
        }

        @Override
        public int getCount() {
            return catalogs.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment mFragment = null;
            Bundle b;
            switch (position) {
                case 0://用户
                    mFragment = new SeedfriendFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
                case 1://品种
//                    mFragment = CropFragment.newInstance(position, StrSearch);
                    mFragment = new CropFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    b.putInt(STR_FAXIAN, 0);
                    mFragment.setArguments(b);
                    break;
                case 2://经销商
//                    mFragment = DistributorFragment.newInstance(position, StrSearch);
                    mFragment = new DistributorFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
                case 3://专家
                    // mFragment = ExpertFragment.newInstance(position, StrSearch);
                    mFragment = new ExpertFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
                case 4://企业
                    //  mFragment = EnterpriseFragment.newInstance(position, StrSearch);
                    mFragment = new EnterpriseFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
                case 5://机构
                    //  mFragment = AuthorFragment.newInstance(position, StrSearch);
                    mFragment = new AuthorFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
//                case 7://综合
//                    mFragment = ComFragment.newInstance(position);
//                    break;
                case 6://农技经验
                    // mFragment = ExperienceFragment.newInstance(position, StrSearch);
                    mFragment = new ExperienceFragment(StrSearch);
                    b = new Bundle();
                    b.putInt("position", position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
                case 7://发问
                    //   mFragment = ProblemFragment.newInstance(position, StrSearch);
                    mFragment = new ProblemFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
                case 8://产量表现
                    // mFragment = YieldFragment.newInstance(position, StrSearch);
                    mFragment = new YieldFragment(StrSearch);
                    b = new Bundle();
                    b.putInt(ARG_POSITION, position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
                case 9://实时
                    // mFragment = NowFragment.newInstance(position);
                    mFragment = new NowFragment(StrSearch);
                    b = new Bundle();
                    b.putInt("position", position);
                    b.putString(STR_SEARCH,StrSearch);
                    mFragment.setArguments(b);
                    break;
            }
            return mFragment;
        }

        @Override
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            Log.d(TAG, "getItemPosition(" + object.getClass().getSimpleName() + ")");
            if (object instanceof SeedfriendFragment) {
                ((SeedfriendFragment) object).Search(StrSearch);
            } else if (object instanceof CropFragment) {
                ((CropFragment) object).Search(StrSearch);
            } else if (object instanceof DistributorFragment) {
                ((DistributorFragment) object).Search(StrSearch);
            } else if (object instanceof ExpertFragment) {
                ((ExpertFragment) object).Search(StrSearch);
            } else if (object instanceof ExperienceFragment) {
                ((ExperienceFragment) object).Search(StrSearch);
            } else if (object instanceof EnterpriseFragment) {
                ((EnterpriseFragment) object).Search(StrSearch);
            } else if (object instanceof AuthorFragment) {
                ((AuthorFragment) object).Search(StrSearch);
            } else if (object instanceof ProblemFragment) {
                ((ProblemFragment) object).Search(StrSearch);
            } else if (object instanceof YieldFragment) {
                ((YieldFragment) object).Search(StrSearch);
            }
            return super.getItemPosition(object);
        }

    }
}
