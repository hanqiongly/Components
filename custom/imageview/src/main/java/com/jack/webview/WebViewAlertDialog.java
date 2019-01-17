package com.jack.webview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.jack.imageview.R;

/**
 * Created by liuyang on 2018/1/4.
 */

public class WebViewAlertDialog extends Dialog{

    private View mRootView;
    private OnCloseDialogListener mOnCloseDialogListener;

    public WebViewAlertDialog(@NonNull Context context, int themeStyle) {
        super(context, themeStyle);
    }

    public void setOnCloseDialogListener(OnCloseDialogListener listener) {
        mOnCloseDialogListener = listener;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAlphaAnimation();
    }

    private void startAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mRootView.setAlpha(1.0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mRootView.startAnimation(alphaAnimation);
    }

    private void stopAlphaAnimation() {
        if (mRootView != null) {
            Animation animation = mRootView.getAnimation();
            if (animation != null) {
                animation.cancel();
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAlphaAnimation();
        if (mOnCloseDialogListener != null) {
            mOnCloseDialogListener.onCloseDialog();
        }
    }

    public static class Builder {
        private Context mContext;
        private String mUrl;
        private OnCloseDialogListener mOnCloseDialogListener;

        public Builder(Context context){
            mContext = context;
        }

        public Builder setUrl(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder setOnCloseDialogListener(OnCloseDialogListener listener) {
            mOnCloseDialogListener = listener;
            return this;
        }

        public WebViewAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final WebViewAlertDialog dialog = new WebViewAlertDialog(mContext, R.style.updateDialog);
            View contentview = inflater.inflate(R.layout.web_view_alert_dialog,null);
//            contentview.findViewById(R.id.iv_alert_close).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    if (mOnCloseDialogListener != null) {
//                        mOnCloseDialogListener.onCloseDialog();
//                    }
//                }
//            });
            dialog.mRootView = contentview;
            dialog.mOnCloseDialogListener = mOnCloseDialogListener;
            WebViewWithCorner webView = (WebViewWithCorner) contentview.findViewById(R.id.wv_prize_content_holder);
            webView.loadUrl(mUrl);
            dialog.setContentView(contentview, new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)));
            return dialog;
        }
    }

    public interface OnCloseDialogListener{
        void onCloseDialog();
    }

}
