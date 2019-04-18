package com.autoforce.common.view.input;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import com.autoforce.common.R;

/**
 * Created by xlh on 2019/4/18. <br>
 * description:
 * An EditText that can be defined the length of decimal.
 */
public class DecimalEditText extends AppCompatEditText {

    private static final int DEFAULT_DECIMAL_NUMBER = 2;
    private int length;

    public DecimalEditText(Context context) {
        this(context, null);
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DecimalEditText);
        length = ta.getInt(R.styleable.DecimalEditText_decimalLength, DEFAULT_DECIMAL_NUMBER);
        ta.recycle();

        init();
    }

    public void init() {

        setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                if (!TextUtils.isEmpty(s.toString())) {
//                    if (Double.parseDouble(s.toString()) < 0) {
//                        setText(emptyString());
//                    }
//                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                String temp = s.toString();

                int dotPos = temp.indexOf(".");

                if (dotPos <= 0) {
                    return;
                }

                if (temp.length() - dotPos - 1 > length) {
                    s.delete(dotPos + length + 1, dotPos + length + 2);
                }
            }
        });

    }

//    private String emptyString() {
//
//        StringBuilder sb = new StringBuilder("0.");
//        for (int i = 0; i < length; i++) {
//            sb.append("0");
//        }
//
//        return sb.toString();
//    }


}
