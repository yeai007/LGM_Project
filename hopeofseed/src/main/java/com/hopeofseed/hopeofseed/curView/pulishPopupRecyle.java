package com.hopeofseed.hopeofseed.curView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.UserMenu;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.util.ListUserMenu;


import java.util.ArrayList;

/**
 * @FileName:com.mgkj.smamootwo.curView
 * @Desc:
 * @Author:liguangming
 * @Date:2016/5/16
 * @Copyright:2014-2016 Moogeek
 */
public class pulishPopupRecyle extends PopupWindow implements PagingScrollHelper.onPageChangeListener {
    private View mMenuView;
    RecyclerView recyclerView;
    TextView tv_title;
    MyAdapter myAdapter;
    PagingScrollHelper scrollHelper = new PagingScrollHelper();
    private RecyclerView.ItemDecoration lastItemDecoration = null;
    private HorizontalPageLayoutManager horizontalPageLayoutManager = null;
    private PagingItemDecoration pagingItemDecoration = null;
    Context mContext;
    ArrayList<UserMenu> arrUserMenu = new ArrayList<>();
    Button btn_cancle, btn_back;

    public pulishPopupRecyle(Activity context, OnClickListener itemsOnClick) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_alert_dialog_recyle, null);
        btn_cancle = (Button) mMenuView.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_back = (Button) mMenuView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_back.setVisibility(View.GONE);
                scrollHelper.setPage(0);
            }
        });
        initUserData();
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        init();
        tv_title = (TextView) mMenuView.findViewById(R.id.tv_title);
        myAdapter = new MyAdapter(mContext, arrUserMenu, pulishPopupRecyle.this);
        recyclerView = (RecyclerView) mMenuView.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(myAdapter);
        scrollHelper.setUpRecycleView(recyclerView);
        scrollHelper.setOnPageChangeListener(this);
        RecyclerView.LayoutManager layoutManager = null;
        RecyclerView.ItemDecoration itemDecoration = null;
        layoutManager = horizontalPageLayoutManager;
        itemDecoration = pagingItemDecoration;
        if (layoutManager != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.removeItemDecoration(lastItemDecoration);
            recyclerView.addItemDecoration(itemDecoration);
            scrollHelper.updateLayoutManger();
            lastItemDecoration = itemDecoration;
        }


    }


    private void init() {
        horizontalPageLayoutManager = new HorizontalPageLayoutManager(2, 3);
        pagingItemDecoration = new PagingItemDecoration(mContext, horizontalPageLayoutManager);
    }

    @Override
    public void onPageChange(int index) {

    }

    private void initUserData() {
        int[] fregments = null;
        switch (Integer.parseInt(Const.currentUser.user_role)) {
            /**
             * 农友
             * */
            case 0:
                fregments = new int[]{0, 4, 2, 3};
                break;
            /**
             * 经销商
             * */
            case 1:
               //fregments = new int[]{0, 4, 2, 3, 1};
                fregments = new int[]{0, 4, 2, 3, 1, 5};
                break;
            /**
             * 企业
             * */
            case 2:
                //fregments = new int[]{0, 4, 2, 3, 1, 8, 6, 7};
                fregments = new int[]{0, 4, 2, 3, 1, 8, 5, 6, 7,9};
                break;
            /**
             * 机构
             * */
            case 4:
                fregments = new int[]{0, 4, 2, 3, 1, 6};
                break;
            /**
             * 专家
             * */
            case 3:
                fregments = new int[]{0, 4, 2, 3};
                break;
            /**
             * 管理员
             * */
            case 5:
                fregments = new int[]{0,1, 2, 3, 4,8, 5,  6, 7};
                break;
            /**
             * 媒体
             * */
            case 6:
                fregments = new int[]{0, 4, 2, 3, 1};
                break;
            default:
                fregments = new int[]{0, 4, 2, 3};
                break;
        }
        ListUserMenu lfc = new ListUserMenu();
        arrUserMenu.addAll(lfc.getCommUser(fregments));
    }

    public void setNextPage() {
        btn_back.setVisibility(View.VISIBLE);
        scrollHelper.setPage(1);
    }
}
