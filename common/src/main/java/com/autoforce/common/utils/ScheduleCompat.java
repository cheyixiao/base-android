package com.autoforce.common.utils;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xialihao on 2018/11/27.
 */
public class ScheduleCompat {

    public static <T> FlowableTransformer<T,T> apply(){
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
