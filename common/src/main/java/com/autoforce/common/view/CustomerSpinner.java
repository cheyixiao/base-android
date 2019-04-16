package com.autoforce.common.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.autoforce.common.R;
import com.autoforce.common.utils.DeviceUtil;

/**
 * Created by xialihao on 2018/11/24.
 */
public class CustomerSpinner extends LinearLayout implements View.OnClickListener {

    private static final int REMOTE = 0;
    private static final int ADJACENT = 1;
    private static final int DEFAULT_COLOR = R.color.black3;
    private static final float DEFAULT_HEIGHT_DP = 44f;
    private static final int DEFAULT_DURATION = 300;
    private static final float DEFAULT_SPACING = 3;
    private CharSequence text;
    private float textSize;
    private int textColor;
    private Drawable drawable;
    private ImageView ivImage;
    private long duration;
    private int spacing;
    private int style;
    private TextView tvText;
    private int textStyle;

    private int TEXT_STYLE_BOLD = 1;
    private int TEXT_STYLE_FAKE_BOLD = 2;


    public CustomerSpinner(@NonNull Context context) {
        this(context, null);
    }

    public CustomerSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomerSpinner);

            try {
                style = a.getInt(R.styleable.CustomerSpinner_spinner_style, 0);

                if (style == ADJACENT) {
                    LayoutInflater.from(context).inflate(R.layout.spinner_child_adjacent, this);
                } else {
                    LayoutInflater.from(context).inflate(R.layout.spinner_child_remote, this);
                }

                //text
                text = a.getText(R.styleable.CustomerSpinner_spinner_text);

                //textSize
                textSize = a.getDimensionPixelSize(R.styleable.CustomerSpinner_spinner_textSize, 12);

                //textColor
                textColor = a.getColor(R.styleable.CustomerSpinner_spinner_texColor, context.getResources().getColor(DEFAULT_COLOR));

                //src
                drawable = a.getDrawable(R.styleable.CustomerSpinner_spinner_src);

                //textStyle
                textStyle = a.getInt(R.styleable.CustomerSpinner_spinner_textStyle, 0);
                //高度
//                contentHeight = a.getDimensionPixelOffset(R.styleable.CustomerSpinner_content_height, DeviceUtil.dip2px(context, DEFAULT_HEIGHT_DP));

                //箭头旋转的动画时长
                duration = a.getInt(R.styleable.CustomerSpinner_spinner_duration, DEFAULT_DURATION);

                //文字和箭头之间的间隔
                spacing = a.getDimensionPixelOffset(R.styleable.CustomerSpinner_spacing, DeviceUtil.dip2px(context, DEFAULT_SPACING));

            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth() + spacing, getMeasuredHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvText = findViewById(R.id.tv_title);
        ivImage = findViewById(R.id.iv_image);

        tvText.setText(text);
        tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvText.setTextColor(textColor);

        if (drawable == null) {
            ivImage.setImageResource(R.drawable.icon_pull);
        } else {
            ivImage.setImageDrawable(drawable);
        }

        if (style == ADJACENT) {
            LayoutParams p = (LayoutParams) ivImage.getLayoutParams();
            p.leftMargin = spacing;
            ivImage.setLayoutParams(p);
        }

        if (textStyle == TEXT_STYLE_FAKE_BOLD) {
            tvText.getPaint().setFakeBoldText(true);
        } else if (textStyle == TEXT_STYLE_BOLD) {
            tvText.setTypeface(Typeface.DEFAULT_BOLD);
        }

        setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        saveTag(v);
        Boolean b = (Boolean) v.getTag();
        changeStatus(b);
    }

    private void changeStatus(Boolean b) {

        rotateImage(ivImage, b);
        if (b) {
            if (mClickChangeListener != null) {
                mClickChangeListener.onShow();
            }
        } else {
            if (mClickChangeListener != null) {
                mClickChangeListener.onClose();
            }
        }

        if (!b.equals(getTag())) {
            setTag(b);
        }
    }

    private void rotateImage(ImageView imageView, Boolean b) {

        ObjectAnimator animator;

        if (b) {
            animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f);
        } else {
            animator = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0f);
        }

        animator.setDuration(duration);
        animator.start();
    }

    private void saveTag(View v) {

        Object tag = v.getTag();

        if (tag == null) {
            v.setTag(true);
        } else {
            Boolean b = (Boolean) tag;
            v.setTag(!b);
        }
    }

    private OnClickChangeListener mClickChangeListener;

    public void setTextColor(int color) {
        tvText.setTextColor(color);
    }

    public interface OnClickChangeListener {

        /**
         * 清空数据，收起列表回调
         */
        void onClose();

        /**
         * 获取数据，打开下拉列表
         */
        void onShow();
    }

    /*---------------public api-------------------*/
    public void setOnClickChangeListener(OnClickChangeListener listener) {
        this.mClickChangeListener = listener;
    }

    public void setText(CharSequence sequence) {
        tvText.setText(sequence);
        changeStatus(false);
    }

    public void setDefaultText(String text) {
        tvText.setText(text);
        ivImage.setImageDrawable(drawable);
    }

    public String getText() {
        return tvText.getText().toString();
    }

    /**
     * 关闭下拉列表
     */
    public void closeList() {
        changeStatus(false);
    }
}
