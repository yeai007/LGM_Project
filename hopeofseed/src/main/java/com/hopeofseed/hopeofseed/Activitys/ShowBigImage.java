package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.R;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/12 14:26
 * 修改人：whisper
 * 修改时间：2016/10/12 14:26
 * 修改备注：
 */
public class ShowBigImage extends AppCompatActivity {
    private static final String TAG = "ShowImage";
    String ImgUrl;
    ImageView img_show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_img_activity);
        Intent intent = getIntent();
        ImgUrl = intent.getStringExtra("IMG_URL");
        Log.e(TAG, "onCreate: " + ImgUrl);
        initView();
    }

    private void initView() {
        img_show = (ImageView) findViewById(R.id.img_show);
        Glide.with(this)
                .load(ImgUrl)
                .fitCenter()
                .into(img_show);
        img_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
