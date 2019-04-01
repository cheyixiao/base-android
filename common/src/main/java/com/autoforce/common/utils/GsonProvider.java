package com.autoforce.common.utils;

import com.google.gson.Gson;

/**
 * Created by xialihao on 2018/11/16.
 */
public class GsonProvider {

    public static Gson gson(){
        return GsonHolder.singleton;
    }

    private static class GsonHolder {
        private static final Gson singleton = new Gson();
    }
}
