package com.autoforce.common.utils;

import android.text.TextUtils;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by xialihao on 2018/11/16.
 * 图片加载二次封装类
 */
public class ImageLoaderUtils {

    /**
     * @param url              图片url
     * @param imageView        显示图片的ImageView
     * @param errorResId       加载错误占位符    没有传-1
     * @param placeHolderResId 默认显示占位符 没有传-1
     * @param callback         加载成功或失败回调   没有传null
     */
    public static void loadImage(String url, ImageView imageView, int errorResId, int placeHolderResId, Callback callback) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        RequestCreator creator = Picasso.get().load(url);

        if (errorResId != -1) {
            creator.error(errorResId);
        }

        if (placeHolderResId != -1) {
            creator.placeholder(placeHolderResId);
        }

        creator.into(imageView, callback);
    }


    /**
     * @param url              图片id
     * @param imageView        显示图片的ImageView
     * @param errorResId       加载错误占位符    没有传-1
     * @param placeHolderResId 默认显示占位符 没有传-1
     * @param callback         加载成功或失败回调   没有传null
     */
    public static void loadImage(int url, ImageView imageView, int errorResId, int placeHolderResId, Callback callback) {

        try {
            RequestCreator creator = Picasso.get().load(url);

            if (errorResId != -1) {
                creator.error(errorResId);
            }

            if (placeHolderResId != -1) {
                creator.placeholder(placeHolderResId);
            }

            creator.into(imageView, callback);
        } catch (Exception e) {

        }
    }


}
