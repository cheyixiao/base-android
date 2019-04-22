package com.autoforce.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.autoforce.common.data.layout.ILayoutDataFetcher;
import com.autoforce.common.data.layout.LayoutDataFetcherImpl;
import com.autoforce.common.view.dynamic.DynamicView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xlh on 2019/3/19.
 * description:
 */
public class NativeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ILayoutDataFetcher fetcher = new LayoutDataFetcherImpl(getContext()) {

            @Override
            public String filenameInAssets() {
                return "item_layout.json";
            }
        };

        JSONObject obj = null;

        try {
            obj = new JSONObject(fetcher.getLayoutData(getContext(), "http://192.168.3.245:8080/item_layout.json"));

        } catch (JSONException e) {

        }

        return DynamicView.createView(getContext(), obj, container);
    }


}
