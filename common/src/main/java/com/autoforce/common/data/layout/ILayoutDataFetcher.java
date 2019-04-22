package com.autoforce.common.data.layout;


import android.content.Context;

/**
 * Created by xlh on 2019/4/22.<br/>
 * description:
 */
public interface ILayoutDataFetcher {

    String getLayoutData(Context context, String url);

    String filenameInAssets();

}
