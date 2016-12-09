package com.hopeofseed.hopeofseed.ui.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.GroupMemberGridAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;

import citypickerview.widget.CityPicker;

public class ChatDetailView extends LinearLayout {
    private LinearLayout mAllGroupMemberLL;
    private View mSplitLine1;
    private View mSplitLine2;
    private LinearLayout mGroupNameLL;
    private LinearLayout mMyNameLL;
    private LinearLayout mGroupNumLL;
    private LinearLayout mGroupChatRecordLL;
    private LinearLayout mGroupChatDelLL;
    private ImageButton mReturnBtn;
    private TextView mTitle;
    private TextView mMembersNum;
    private ImageButton mMenuBtn;
    private Button mDelGroupBtn;
    private TextView mGroupName;
    private TextView mGroupNum;
    private TextView mMyName;
    private GroupGridView mGridView;
    private SlipButton mNoDisturbBtn;
    private Context mContext;
    /**
     * 非SDK提供
     */
    ImageView img_user_avatar;
    Button chat_detail_group_address;
    CityPicker cityPicker;
    public String StrProvince = "", StrCity = "", StrZone = "";
    private TextView chat_detail_group_desc;
    TextView AppTitle;
    Button btn_topright, btn_topleft;
    RelativeLayout rel_desc;
    public ChatDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        // TODO Auto-generated constructor stub
    }

    public void initModule() {
        mAllGroupMemberLL = (LinearLayout) findViewById(R.id.all_member_ll);
        mSplitLine1 = findViewById(R.id.all_member_split_line1);
        mSplitLine2 = findViewById(R.id.all_member_split_line2);
        mGroupNameLL = (LinearLayout) findViewById(R.id.group_name_ll);
        mMyNameLL = (LinearLayout) findViewById(R.id.group_my_name_ll);
        mGroupNumLL = (LinearLayout) findViewById(R.id.group_num_ll);
        mGroupChatRecordLL = (LinearLayout) findViewById(R.id.group_chat_record_ll);
        mGroupChatDelLL = (LinearLayout) findViewById(R.id.group_chat_del_ll);
        mReturnBtn = (ImageButton) findViewById(R.id.return_btn);
        mTitle = (TextView) findViewById(R.id.title);
        mMembersNum = (TextView) findViewById(R.id.members_num);
        mMenuBtn = (ImageButton) findViewById(R.id.right_btn);
        mDelGroupBtn = (Button) findViewById(R.id.chat_detail_del_group);
        mGroupName = (TextView) findViewById(R.id.chat_detail_group_name);
        mGroupNum = (TextView) findViewById(R.id.chat_detail_group_num);
        mMyName = (TextView) findViewById(R.id.chat_detail_my_name);
        mGridView = (GroupGridView) findViewById(R.id.chat_detail_group_gv);
        mNoDisturbBtn = (SlipButton) findViewById(R.id.no_disturb_slip_btn);

        mTitle.setText(mContext.getString(R.string.chat_detail_title));
        mMenuBtn.setVisibility(View.GONE);
        //自定义GridView点击背景为透明色


        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        img_user_avatar = (ImageView) findViewById(R.id.img_user_avatar);
        chat_detail_group_desc = (TextView) findViewById(R.id.chat_detail_group_desc);
        AppTitle = (TextView) findViewById(R.id.apptitle);
        AppTitle.setText("聊天详情");
        btn_topright = (Button) findViewById(R.id.btn_topright);
        rel_desc=(RelativeLayout)findViewById(R.id.rel_desc);
        btn_topright.setText("更新");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topleft = (Button) findViewById(R.id.btn_topleft);
        chat_detail_group_address = (Button) findViewById(R.id.chat_detail_group_address);
        chat_detail_group_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityPicker = new CityPicker.Builder(mContext).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province("山东省")
                        .city("济南市")
                        .district("全部")
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
                        chat_detail_group_address.setText("" + citySelected[0] + "  " + citySelected[1] + "  " + citySelected[2]);
                        StrProvince = citySelected[0];
                        StrCity = citySelected[1];
                        StrZone = citySelected[2];
                    }
                });
            }
        });
    }

    public void setListeners(OnClickListener onClickListener) {
        mAllGroupMemberLL.setOnClickListener(onClickListener);
        mGroupNameLL.setOnClickListener(onClickListener);
        mMyNameLL.setOnClickListener(onClickListener);
        mGroupNumLL.setOnClickListener(onClickListener);
        mGroupChatRecordLL.setOnClickListener(onClickListener);
        mGroupChatDelLL.setOnClickListener(onClickListener);
        mReturnBtn.setOnClickListener(onClickListener);
        mDelGroupBtn.setOnClickListener(onClickListener);
        img_user_avatar.setOnClickListener(onClickListener);
        AppTitle.setOnClickListener(onClickListener);
        btn_topright.setOnClickListener(onClickListener);
        btn_topleft.setOnClickListener(onClickListener);
        rel_desc.setOnClickListener(onClickListener);
    }

    public void setOnChangeListener(SlipButton.OnChangedListener listener) {
        mNoDisturbBtn.setOnChangedListener(R.id.no_disturb_slip_btn, listener);
    }

    public void setItemListener(OnItemClickListener listener) {
        mGridView.setOnItemClickListener(listener);
    }

//	public void setLongClickListener(OnItemLongClickListener listener) {
//		mGridView.setOnItemLongClickListener(listener);
//	}

    public void setAdapter(GroupMemberGridAdapter adapter) {
        mGridView.setAdapter(adapter);
    }

    public void setGroupName(String str) {
        mGroupName.setText(str);
    }

    public void setMyName(String str) {
        mMyName.setText(str);
    }

    public void setSingleView() {
        mGroupNameLL.setVisibility(View.GONE);
        mGroupNumLL.setVisibility(View.GONE);
        mMyNameLL.setVisibility(View.GONE);
        mDelGroupBtn.setVisibility(View.GONE);
    }

    public void updateGroupName(String newName) {
        mGroupName.setText(newName);
    }

    public void setTitle(int size) {
        String title = mContext.getString(R.string.chat_detail_title)
                + mContext.getString(R.string.combine_title);
        mTitle.setText(String.format(title, size));
    }

    public GroupGridView getGridView() {
        return mGridView;
    }

    public void setMembersNum(int size) {
        String text = mContext.getString(R.string.all_group_members)
                + mContext.getString(R.string.combine_title);
        mMembersNum.setText(String.format(text, size));
    }

    public void dismissAllMembersBtn() {
        mSplitLine1.setVisibility(View.GONE);
        mSplitLine2.setVisibility(View.GONE);
        mAllGroupMemberLL.setVisibility(View.GONE);
    }

    public void initNoDisturb(int status) {
        mNoDisturbBtn.setChecked(status == 1);
    }

    public void setNoDisturbChecked(boolean flag) {
        mNoDisturbBtn.setChecked(flag);
    }

    public void setAvatar(String imgURL) {
        Glide.with(mContext)
                .load(Const.IMG_URL + imgURL)
                .centerCrop()
                .into(img_user_avatar);
    }

    public void setAddress(String Province, String City, String Zone) {
        if (!TextUtils.isEmpty(Province)) {
            StrProvince = Province;
            StrCity = City;
            StrZone = Zone;
            chat_detail_group_address.setText("" + Province + "  " + City + "  " + Zone);
        }
        //  chat_detail_group_address.setText("" + citySelected[0] + "  " + citySelected[1] + "  "+ citySelected[2]);
    }

    public void setDesc(String desc) {
        if (!TextUtils.isEmpty(desc)) {
            chat_detail_group_desc.setText(desc);
        }
    }

    public String getDesc() {
        return chat_detail_group_desc.getText().toString();
    }
}
