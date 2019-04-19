package com.autoforce.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.autoforce.common.utils.AssetFileUtils;
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
//        TextView textView = new TextView(getContext());
//        textView.setText("我是原生页面");
        JSONObject obj = null;
        try {
            obj = new JSONObject(AssetFileUtils.getFile(getContext(), "item_layout.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return DynamicView.createView(getContext(), obj, container);
    }
}
