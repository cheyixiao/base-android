package com.autoforce.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.autoforce.common.R;


/**
 * @author xlh
 * @date 2017/11/16.
 */

public class CustomerDialog extends Dialog {

    private final EditText input;

    CustomerDialog(@NonNull Context context, Params params) {
        super(context, R.style.MyDialog);

        setContentView(R.layout.layout_dialog_general);

        TextView tvTips = findViewById(R.id.tv_tips);
        if (params.tips != null) {
            tvTips.setVisibility(View.VISIBLE);
            tvTips.setText(params.tips);
        }

        TextView textView = findViewById(R.id.tv_content);
        if (params.contentHeight != -1) {
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.height = params.contentHeight;
            textView.setLayoutParams(layoutParams);
        }
        textView.setTextSize(params.textSize);
        textView.setGravity(params.gravity);
        textView.setText(params.content);

        if (params.textColor != -1) {
            textView.setTextColor(params.textColor);
        }

        if (params.bold) {
            textView.getPaint().setFakeBoldText(true);
        }

        input = findViewById(R.id.et_input);
        if (params.showInput) {
            input.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }

        TextView cancel = findViewById(R.id.cancel);

        if (params.mNegativeButtonText != null) {
            cancel.setText(params.mNegativeButtonText);
        }

        cancel.setVisibility(params.isHideNegative ? View.GONE : View.VISIBLE);

        cancel.setOnClickListener(v -> {
            dismiss();

            if (params.mNegativeButtonListener != null) {
                params.mNegativeButtonListener.onClick(cancel);
            }
        });

        TextView confirm = findViewById(R.id.confirm);
        if (params.mPositiveButtonText != null) {
            confirm.setText(params.mPositiveButtonText);
        }

        confirm.setVisibility(params.isHidePositive ? View.GONE : View.VISIBLE);

        confirm.setOnClickListener(v -> {

            dismiss();
            if (params.mPositiveButtonListener != null) {
                params.mPositiveButtonListener.onClick(confirm);
            }

            if (params.mOnInputListener != null) {
                params.mOnInputListener.onDataGot(input.getText().toString().trim());
            }
        });
    }


    public static class Builder {

        private Params params;
        private Activity mContext;

        public Builder(@NonNull Activity context) {
            this.mContext = context;
            params = new Params();
        }

        public Builder setTips(String tips) {
            params.tips = tips;
            return this;
        }

        public Builder setTips(@StringRes int textId) {
            params.tips = mContext.getText(textId);
            return this;
        }

        public Builder setContent(String content) {
            params.content = content;
            return this;
        }

        public Builder setContent(@StringRes int textId) {
            params.content = mContext.getText(textId);
            return this;
        }

        public Builder setContentHeight(int height) {
            params.contentHeight = height;
            return this;
        }

        public Builder setTextSize(float textSize) {

            params.textSize = textSize;
            return this;
        }

        public Builder setTextColor(int color) {
            params.textColor = color;
            return this;
        }

        public Builder setBold() {
            params.bold = true;
            return this;
        }

        public Builder setGravity(int gravity) {

            params.gravity = gravity;
            return this;
        }

        public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
            params.mPositiveButtonText = mContext.getText(textId);
            params.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButtonInInput(int textId, OnInputListener listener) {
            params.mPositiveButtonText = mContext.getText(textId);
            params.mOnInputListener = listener;
            return this;
        }

        public Builder setPositiveButton(final View.OnClickListener listener) {
            params.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, final View.OnClickListener listener) {

            params.mNegativeButtonText = mContext.getText(textId);
            params.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(final View.OnClickListener listener) {

            params.mNegativeButtonListener = listener;
            return this;
        }


        public Builder hideNegativeButton() {

            params.isHideNegative = true;
            return this;
        }

        public Builder hidePositiveButton() {

            params.isHidePositive = true;
            return this;
        }

        public Builder setCancellable(boolean cancellable) {
            params.cancellable = cancellable;
            return this;
        }

        public Builder setContentView(View view) {

            params.contentView = view;
            return this;
        }

        public Builder showInput(boolean isShow) {
            params.showInput = isShow;
            return this;
        }

        public CustomerDialog build() {

            CustomerDialog dialog = new CustomerDialog(mContext, params);
            dialog.setCancelable(params.cancellable);
//            dialog.show();
            return dialog;
        }

    }

    private static class Params {

        CharSequence content;
        CharSequence mPositiveButtonText;
        View.OnClickListener mPositiveButtonListener;
        CharSequence mNegativeButtonText;
        View.OnClickListener mNegativeButtonListener;
        float textSize = 13f;
        int gravity = Gravity.CENTER;
        CharSequence tips;
        int contentHeight = -1;
        boolean isHideNegative = false;
        boolean cancellable = true;
        View contentView;
        boolean isHidePositive;
        boolean showInput = false;
        OnInputListener mOnInputListener;
        int textColor = -1;
        boolean bold = false;
    }

    public interface OnInputListener {

        /**
         * 当Dialog有EditText时，回调用户输入内容
         *
         * @param input
         */
        void onDataGot(String input);
    }


}
