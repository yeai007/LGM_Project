package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.controller.CreateGroupController;
import com.hopeofseed.hopeofseed.ui.chatting.BaseActivity;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.hopeofseed.hopeofseed.ui.view.CreateGroupView;

import citypickerview.widget.CityPicker;

/*
创建群聊
 */
public class CreateGroupActivity extends BaseActivity {
    private CreateGroupView mCreateGroupView;
    private CreateGroupController mCreateGroupController;
    Button chat_detail_group_address;
    CityPicker cityPicker;
    public String StrProvince = "", StrCity = "", StrZone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initAddress();
        mCreateGroupView = (CreateGroupView) findViewById(R.id.create_group_view);
        mCreateGroupView.initModule();
        mCreateGroupController = new CreateGroupController(mCreateGroupView, this);
        mCreateGroupView.setListeners(mCreateGroupController);
    }

    private void initAddress() {
        TextView appTitle=(TextView)findViewById(R.id.apptitle);
        appTitle.setText("创建群组");
        chat_detail_group_address = (Button) findViewById(R.id.chat_detail_group_address);
        chat_detail_group_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreateGroupController.hideInput();
                cityPicker = new CityPicker.Builder(CreateGroupActivity.this).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province(Const.Province)
                        .city(Const.City)
                        .district(Const.Zone)
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

                        //
                        //mChatDetailController.updateInfoNoJpush();
                    }
                });
            }
        });
    }


    public void startChatActivity(long groupId, String groupName) {
        Intent intent = new Intent();
        //设置跳转标志
        intent.putExtra("fromGroup", true);
        intent.putExtra(Application.GROUP_ID, groupId);
        intent.putExtra(Application.GROUP_NAME, groupName);
        intent.putExtra(Application.MEMBERS_COUNT, 1);
        intent.setClass(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

}
