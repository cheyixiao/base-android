package com.autoforce.common.view.bold;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

/**
 * @author xlh
 * @date 2018/6/21.
 */

public class FakeBoldSpan extends CharacterStyle {

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setFakeBoldText(true);
//        tp.setStyle(Paint.Style.FILL_AND_STROKE);
//        tp.setStrokeWidth(1);
    }
}
