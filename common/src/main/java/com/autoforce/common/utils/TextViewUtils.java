package com.autoforce.common.utils;

import android.content.Context;
import android.support.annotation.StyleRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;

/**
 * Created by xialihao on 2018/11/22.
 * 基于SpannableString以及TextView的工具类
 */
public class TextViewUtils {

    public static CharSequence stringSelectInt(String src, String unit, Context context, @StyleRes int RStyle) {
        return stringSelectInt("", src, unit, context, RStyle);
    }

    public static CharSequence stringSelectInt(String header, String src, String unit, Context context, @StyleRes int RStyle) {
        SpannableString spannableString = new SpannableString(header + src + unit);
        spannableString.setSpan(new TextAppearanceSpan(context, RStyle), header.length(), header.length() + src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static CharSequence stringOnClick(String header, String src, String unit, Context context, @StyleRes int RStyle, View.OnClickListener onClickListener) {
        SpannableString spannableString = new SpannableString(header + src + unit);
        spannableString.setSpan(new TextAppearanceSpan(context, RStyle), header.length(), header.length() + src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(onClickListener, header.length(), header.length() + src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static CharSequence highlightContent(Context context, String src, HighlightData... array) {

        SpannableString spannableString = new SpannableString(src);

        for (HighlightData highlightData : array) {
            int start = src.indexOf(highlightData.getHightContent());
            int end = start + highlightData.getHightContent().length();
            spannableString.setSpan(new TextAppearanceSpan(context, highlightData.getResStyle()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }

    public static CharSequence stringSelectHeader(String header, String src, int color) {

        SpannableString spannableString = new SpannableString(header + src);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, header.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


}
