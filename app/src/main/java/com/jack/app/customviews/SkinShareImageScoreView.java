package com.jack.app.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.app.R;

/**
 * Created by liuyang on 2017/10/24.
 */

public class SkinShareImageScoreView extends RelativeLayout {
    private TextView mScoreInfo;
    private TextView mScoreMarketInfo;
    private Drawable mBackGroundDrawable;
    private OnBackGroundUpdatedListener mOnBackGroundUpdatedListener;

    public SkinShareImageScoreView(Context context) {
        this(context, null);
    }

    public SkinShareImageScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinShareImageScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.share_image_score_view, this);
        mScoreInfo = (TextView) rootView.findViewById(R.id.tv_skin_score);
        mScoreMarketInfo = (TextView) rootView.findViewById(R.id.tv_skin_score_market_info);
//        mScoreEmoji = (ImageView) rootView.findViewById(R.id.iv_skin_score_emoji);
        mBackGroundDrawable = getResources().getDrawable(R.drawable.score_view_back_ground);
        updateScoreInfo(98, "", "");
    }

    public void updateScoreInfo(int score, String marketInfo, String backGroudUrl) {
        if (score < 0) {
            score = 0;
        } else if (score > 100) {
            score = 100;
        }

        String scoreStr = "" + score;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(scoreStr);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(51);
        spannableStringBuilder.setSpan(sizeSpan, 0, scoreStr.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mScoreInfo.setText(spannableStringBuilder);
        updateScoreEmoji(score, mScoreInfo);

        if (!TextUtils.isEmpty(marketInfo))
            mScoreMarketInfo.setText(marketInfo);

        refreshBackGround(backGroudUrl);
    }

    private void updateScoreEmoji(int score, TextView textView) {
        int resId = R.drawable.skin_score_emoji_icon;
        if (score>= 0 && score <= 60) {

        } else if (score > 60 && score <= 80) {

        } else if (score > 80 && score <= 90) {

        } else {

        }

        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, 45,45);
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    private void refreshBackGround(String url) {
        if (TextUtils.isEmpty(url)) {
            setBackgroundResource(R.drawable.score_view_back_ground);
            return ;
        }

        //todo update background
        mOnBackGroundUpdatedListener.onBackGroundUpdated(true);
    }

    public void setOnBackGroundUpdatedListener(OnBackGroundUpdatedListener listener) {
        mOnBackGroundUpdatedListener = listener;
    }

    public interface OnBackGroundUpdatedListener {
        public void onBackGroundUpdated(boolean success);
    }
}
