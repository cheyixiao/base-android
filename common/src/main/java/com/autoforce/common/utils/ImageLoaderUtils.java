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

    //todo: 将Callback使用自定义类型，否则还是强依赖与Picasso的实现

    /**
     * @param url              图片url
     * @param imageView        显示图片的ImageView
     * @param errorResId       加载错误占位符    没有传-1
     * @param placeHolderResId 默认显示占位符 没有传-1
     * @param callback         加载成功或失败回调   没有传null
     */
    public static void loadImage(String url, ImageView imageView, int errorResId, int placeHolderResId, Callback callback) {

        if (TextUtils.isEmpty(url) || imageView == null) {
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


}
