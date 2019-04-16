package com.autoforce.common.view.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.autoforce.common.utils.DeviceUtil;
import com.autoforce.common.utils.DrawableUtils;
import com.autoforce.common.view.tab.config.ConfigUtils;
import com.autoforce.common.view.tab.config.DefaultConfigs;
import com.autoforce.framework.config.MainConfigResult;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by xlh on 2019/3/19.
 * description:
 */
public class MainTabGroup extends RadioGroup {

    private MainTabInterface mTabInterface;

    public MainTabGroup(Context context) {
        super(context);
    }

    public MainTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(MainConfigResult config, MainTabInterface tabInterface) {

        this.mTabInterface = tabInterface;

        // 移除掉之前已经存在的所有子View，防止干扰
        removeAllViews();

        setLayoutHeight(config);

        List<MainConfigResult.TabInfoBean> tabInfoList = config.getTabs();

        if (tabInfoList != null) {

            for (int i = 0; i < tabInfoList.size(); i++) {
                MainConfigResult.TabInfoBean info = tabInfoList.get(i);

                RadioButton radioButton = generateRadioButton(i, info);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

                addView(radioButton, params);
            }
        }


        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            final RadioButton childView = (RadioButton) getChildAt(i);
            resizeDrawable(childView);
            setRadioPadding(childView);
        }

    }

    @NonNull
    private RadioButton generateRadioButton(int i, MainConfigResult.TabInfoBean info) {

        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setText(info.getTabName());
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setButtonDrawable(null);
        radioButton.setId(ConfigUtils.getIdByIndex(i));

        // 图标缩放比例
        Float imageRatio = info.getImageRatio();

        // icon的状态设置
        String iconUnchecked = info.getIconUnchecked();
        String iconChecked = info.getIconChecked();

        Drawable uncheckedDrawable = null;
        Drawable checkedDrawable = null;

        if (mTabInterface != null) {
            uncheckedDrawable = mTabInterface.getIconDrawable(iconUnchecked);
            checkedDrawable = mTabInterface.getIconDrawable(iconChecked);
        }

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);
        stateListDrawable.addState(new int[]{}, uncheckedDrawable);

        String iconPosition = info.getIconPosition();

        if (DefaultConfigs.POSITION_TOP.equalsIgnoreCase(iconPosition)) {
            radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, stateListDrawable, null, null);
        } else if (DefaultConfigs.POSITION_LEFT.equalsIgnoreCase(iconPosition)) {
            radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(stateListDrawable, null, null, null);
        } else if (DefaultConfigs.POSITION_RIGHT.equalsIgnoreCase(iconPosition)) {
            radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, stateListDrawable, null);
        } else if (DefaultConfigs.POSITION_BOTTOM.equalsIgnoreCase(iconPosition)) {
            radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, stateListDrawable);
        }

        // 文字和icon的间距
        Float drawablePadding = info.getDrawablePadding();
        radioButton.setCompoundDrawablePadding(DeviceUtil.dip2px(getContext(), ConfigUtils.getValue(drawablePadding, DefaultConfigs.DEFAULT_DRAWABLE_PADDING) / 2));

        //字体大小
        Float textSize = info.getTextSize();
        radioButton.setTextSize(ConfigUtils.getValue(textSize, DefaultConfigs.DEFAULT_TEXT_SIZE) / 2);

        // 字体颜色
        String textColorUnCheckedStr = info.getTextColorUnchecked();
        String textColorCheckedStr = info.getTextColorChecked();

        int checkedColor = Color.parseColor(textColorCheckedStr);
        int uncheckedColor = Color.parseColor(textColorUnCheckedStr);

        ColorStateList colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_checked}, {}}, new int[]{checkedColor, uncheckedColor});
        radioButton.setTextColor(colorStateList);

        radioButton.setTag(info);
        return radioButton;
    }

    /**
     * 设置底部导航栏的高度
     *
     * @param config
     */
    private void setLayoutHeight(MainConfigResult config) {
        int height = DeviceUtil.dip2px(getContext(),
                ConfigUtils.getValue(config.getLayoutHeight(), DefaultConfigs.DEFAULT_HEIGHT) / 2);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    /**
     * 改变icon的显示比
     *
     * @param childView
     */
    private void resizeDrawable(RadioButton childView) {
        Float imageRatio = ((MainConfigResult.TabInfoBean) childView.getTag()).getImageRatio();
        if (imageRatio != null) {
            DrawableUtils.resizeDrawable(childView, DrawableUtils.DRAWABLE_TOP, imageRatio);
        }
    }

    /**
     * 设置RadioButton的padding，扩大点击响应区域
     *
     * @param childView
     */
    private void setRadioPadding(RadioButton childView) {
        childView.post(() -> {
            int buttonHeight = childView.getMeasuredHeight();
            int groupHeight = getMeasuredHeight();
            int padding = (groupHeight - buttonHeight) / 2;
            childView.setPadding(0, padding, 0, padding);
        });
    }

}
