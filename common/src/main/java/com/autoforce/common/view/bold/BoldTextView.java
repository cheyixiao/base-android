package com.autoforce.common.view.bold;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.binaryfork.spanny.Spanny;

/**
 * @author xlh
 * @date 2018/6/21.
 */

public class BoldTextView extends AppCompatTextView {
    public BoldTextView(Context context) {
        super(context);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(text == null) {
            text = "";
        }
        super.setText(new Spanny(text, new FakeBoldSpan()), type);
    }
}
