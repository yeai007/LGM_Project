package com.lgm.utils;

import android.graphics.Bitmap;

import com.lgm.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


/**
 * @FileName:com.mgkj.Utils
 * @Desc:ImageLoader图片配置
 * @Author:liguangming
 * @Date:2016/3/23
 * @Copyright:2014-2016 Moogeek
 */
public class Option {


    public static DisplayImageOptions getListOptions(int op, int errorRes, int failRes, int loadRes) {
        DisplayImageOptions options = null;
        switch (op) {
            case 0:
                options = new DisplayImageOptions.Builder()
                        // 设置图片在下载期间显示的图片
                        // .showImageOnLoading(R.drawable.ic_stub)
                        // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageForEmptyUri(getErrorRes(errorRes))
                        // 设置图片加载/解码过程中错误时候显示的图片
                        .showImageOnFail(getFailRes(failRes))
                        // 设置下载的图片是否缓存在内存中
                        .cacheInMemory(false)
                        // 设置下载的图片是否缓存在SD卡中
                        .cacheOnDisc(true)
                        // 保留Exif信息
                        .considerExifParams(true)
                        // 设置图片以如何的编码方式显示
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        // 设置图片的解码类型
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        // .decodingOptions(android.graphics.BitmapFactory.Options
                        // decodingOptions)//设置图片的解码配置
                        .considerExifParams(true)
                        // 设置图片下载前的延迟
                        //.delayBeforeLoading(100)// int
                        // delayInMillis为你设置的延迟时间
                        // 设置图片加入缓存前，对bitmap进行设置
                        // .preProcessor(BitmapProcessor preProcessor)
                        // .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                        // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                        // .displayer(new FadeInBitmapDisplayer(100))// 淡入
                        .build();
                break;
            case 1:
                //圆形图片
                options = new DisplayImageOptions.Builder()
                        .showStubImage(getLoadRes(loadRes))
                        //  .showImageForEmptyUri(R.mipmap.ic_launcher)
                        .showImageOnFail(getFailRes(failRes))
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                        .displayer(new DisplayerRound(0))
                        .build();
                break;
        }
        return options;
    }

    private static int getErrorRes(int errorRes) {

        if (errorRes == 0) {
            return R.drawable.imageloader_loading;
        } else {
            return errorRes;
        }
    }

    private static int getFailRes(int failRes) {

        if (failRes == 0) {
            return R.drawable.imageloader_loading;
        } else {
            return failRes;
        }
    }

    private static int getLoadRes(int loadRes) {

        if (loadRes == 0) {
            return R.drawable.imageloader_loading;
        } else {
            return loadRes;
        }
    }
}
