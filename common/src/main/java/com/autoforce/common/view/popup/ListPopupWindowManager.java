package com.autoforce.common.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoforce.common.R;
import com.autoforce.common.utils.DeviceUtil;

import java.util.List;

/**
 * Created by xialihao on 2018/12/11.
 */
public class ListPopupWindowManager {

    private PopupWindow mWindow;

    private ListPopupWindowManager(Context context, Param param) {
        init(context, param);
    }

    private void init(Context context, Param param) {

        mWindow = new PopupWindow(context);
        View contentView = View.inflate(context, R.layout.pop_drop_list, null);

        ListView listView = contentView.findViewById(R.id.list_view);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return param.data.size();
            }

            @Override
            public String getItem(int position) {
                return param.data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @SuppressLint("ViewHolder")
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                Context ctx = parent.getContext();
                LinearLayout vp = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.pop_drop_list_item_layout, parent, false);
                TextView textView = vp.findViewById(R.id.text);
                textView.setText(getItem(position));

                ImageView ivCheck = vp.findViewById(R.id.iv_check);
                ivCheck.setVisibility(getItem(position).equals(param.selectedData) ? View.VISIBLE : View.GONE);
//                ivCheck.setVisibility(position == param.position ? View.VISIBLE : View.GONE);

                return vp;
            }
        });

        listView.setOnItemClickListener(param.listener);

        mWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
        mWindow.setFocusable(true);
        int width = DeviceUtil.getScreenWidth(context) - DeviceUtil.dip2px(context, 16) * 2;
        mWindow.setWidth(width);
        mWindow.setContentView(contentView);

        mWindow.setOnDismissListener(() -> {
            if (param.dismissListener != null) {
                param.dismissListener.onDismiss();
            }
        });
    }

    private void show(View anchorView) {
        if (mWindow != null) {
            mWindow.showAsDropDown(anchorView);
        }
    }

    public void dismiss() {
        if (mWindow != null && mWindow.isShowing()) {
            mWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return mWindow != null && mWindow.isShowing();
    }


    public static class Builder {

        Param param;
        Context context;

        public Builder(Context context) {
            this.context = context;
            param = new Param();
        }

        public Builder setData(List<String> data) {
            param.data = data;
            return this;
        }

//        public Builder setSelectedPos(int position) {
//            param.position = position;
//            return this;
//        }

        public Builder setSelectedData(String data) {
            param.selectedData = data;
            return this;
        }

        public Builder setOnItemClickListener(ListView.OnItemClickListener listener) {
            param.listener = listener;
            return this;
        }

        public ListPopupWindowManager show(View anchorView) {
            ListPopupWindowManager manager = new ListPopupWindowManager(context, param);
            manager.show(anchorView);

            return manager;
        }

        public Builder setOnDismissListener(PopupWindow.OnDismissListener listener) {
            param.dismissListener = listener;
            return this;
        }
    }

    private static class Param {

        PopupWindow.OnDismissListener dismissListener;
        String selectedData;
        List<String> data;
        //        int position = -1;
        ListView.OnItemClickListener listener;

    }
}
