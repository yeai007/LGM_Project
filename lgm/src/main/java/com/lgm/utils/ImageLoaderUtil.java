package com.lgm.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * @FileName:com.mgkj.Utils
 * @Desc:ImageLoader
 * @Author:liguangming
 * @Date:2016/3/23
 * @Copyright:2014-2016 Moogeek
 */
public class ImageLoaderUtil {
    static String TAG = "ImageLoaderUtil";

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                Const.ImaCathe);// 获取到缓存的目录地址
        Log.e("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .memoryCacheExtraOptions(480, 800)
                // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
                //.discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
                // 线程池内加载的数量
                .threadPoolSize(3)
                // 线程优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                //               .denyCacheImageMultipleSizesInMemory()

                // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
                // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // .memoryCacheSize(2 * 1024 * 1024)
                //硬盘缓存50MB
                .diskCacheSize(50 * 1024 * 1024)
                //将保存的时候的URI名称用MD5
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5对UIL进行加密命名
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的File数量
                // .diskCache(new LimitedAgeDiscCache(cacheDir))// 自定义缓存路径
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
                // 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    /**
     * 加载地址图片，默认加载、等待、错误图片
     *
     * @param imageView
     * @param url
     * @param option    0:正常图片、1：圆形图片
     */
    public static void loadImage(ImageView imageView, String url, int option) {
        Log.e(TAG, "loadImage: " + ImageDownloader.Scheme.FILE.wrap(url));
        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(url),
                imageView, Option.getListOptions(option, 0, 0, 0));
    }

    /**
     * 加载地址图片，默认加载、等待、错误图片
     *
     * @param imageView
     * @param url
     */
    public static void loadImage(ImageView imageView, String url) {
        Log.e(TAG, "loadImage: " + ImageDownloader.Scheme.FILE.wrap(url));

        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(url),
                imageView, Option.getListOptions(0, 0, 0, 0));
    }

    /**
     * 加载地址图片，默认加载、等待、错误图片
     *
     * @param imageView
     * @param url
     * @param option    0:正常图片、1：圆形图片
     */
    public static void loadImageHttp(ImageView imageView, String url, int option) {
        Log.e(TAG + "Http", "loadImage: " + url);
        if (!(url == null || url.equals("null"))) {
            ImageLoader.getInstance().displayImage(url,
                    imageView, Option.getListOptions(option, 0, 0, 0));
        } else {
            Log.e(TAG + "Http", "url is null: ");
        }
    }

    /**
     * 加载本地res图片，默认加载、等待、错误图片
     *
     * @param imageView
     * @param resId
     */
    public static void loadImage(ImageView imageView, int resId) {
        String imageUri = "drawable://" + resId;
        ImageLoader.getInstance().displayImage(imageUri, imageView, Option.getListOptions(0, 0, 0, 0));
    }

    /**
     * 加载本地res图片，默认加载、等待、错误图片
     *
     * @param imageView
     * @param resId
     * @param option    0:正常图片、1：圆形图片
     */
    public static void loadImage(ImageView imageView, int resId, int option) {
        String imageUri = "drawable://" + resId;
        ImageLoader.getInstance().displayImage(imageUri, imageView, Option.getListOptions(option, 0, 0, 0));
    }

    /**
     * 清楚缓存
     */
    public static void cleanCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

}
