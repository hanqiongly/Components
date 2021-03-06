package com.alibaba.android.vlayout.layout;

import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutManagerHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Created by liuyang on 2018/1/25.
 */

public class StickLayoutHelperEX extends FixAreaLayoutHelper{

        private static final String TAG = "StickyLayoutHelper";
        private static final String tag = "test_StickyLayoutHelper";

        protected int mPos = -1;

        protected boolean mStickyStart = true;
        protected int mOffset = 0;


        protected View mFixView = null;
        protected boolean mDoNormalHandle = false;

        protected int mBottom = 0;


    public StickLayoutHelperEX() {
            this(true);
        }

    public StickLayoutHelperEX(boolean stickyStart) {
            this.mStickyStart = stickyStart;
            setItemCount(1);
        }

    public void setStickyStart(boolean stickyStart) {
        this.mStickyStart = stickyStart;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public boolean isStickyNow() {
        return !mDoNormalHandle;
    }

    @Override
    public void setItemCount(int itemCount) {
        if (itemCount > 0) {
            super.setItemCount(1);
        } else {
            super.setItemCount(0);
        }
    }

    @Override
    public void onRangeChange(int start, int end) {
        mPos = start;
    }

    @Override
    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutState,
                            LayoutChunkResult result, LayoutManagerHelper helper) {
        // reach the end of this layout
        if (isOutOfRange(layoutState.getCurrentPosition())) {
            return;
        }

        // find view in currentPosition
        View view = mFixView;
        if (view == null) {
            view = layoutState.next(recycler);
        } else {
            layoutState.skipCurrentPosition();
        }
        if (view == null) {
            result.mFinished = true;
            return;
        }


        doMeasure(view, helper);


        // do layout
        final boolean layoutInVertical = helper.getOrientation() == VERTICAL;
        final OrientationHelper orientationHelper = helper.getMainOrientationHelper();
        result.mConsumed = orientationHelper.getDecoratedMeasurement(view);

        // Do normal measure&layout phase by default
        mDoNormalHandle = true;

        final int remainingSpace = layoutState.getAvailable() - result.mConsumed + layoutState.getExtra();

        int left, top, right, bottom;
        if (helper.getOrientation() == VERTICAL) {
            // not support RTL now
            if (helper.isDoLayoutRTL()) {
                right = helper.getContentWidth() - helper.getPaddingRight() - mMarginRight;
                left = right - orientationHelper.getDecoratedMeasurementInOther(view);
            } else {
                left = helper.getPaddingLeft() + mMarginLeft;
                right = left + orientationHelper.getDecoratedMeasurementInOther(view);
            }

            // whether this layout pass is layout to start or to end
            if (layoutState.getLayoutDirection() == VirtualLayoutManager.LayoutStateWrapper.LAYOUT_START) {
                // fill start, from bottom to top
                bottom = layoutState.getOffset() - mMarginBottom;
                top = layoutState.getOffset() - result.mConsumed;
            } else {
                // fill end, from top to bottom
                if (mStickyStart) {
                    top = layoutState.getOffset() + mMarginTop;
                    bottom = layoutState.getOffset() + result.mConsumed;
                } else {
                    bottom = orientationHelper.getEndAfterPadding() - mMarginBottom - mOffset - mAdjuster.bottom;
                    top = bottom - result.mConsumed;
                }
            }


            if (helper.getReverseLayout() || !mStickyStart) {
                boolean needChangeProcedure = (remainingSpace < (mOffset + mAdjuster.bottom)
                        && layoutState.getItemDirection() == VirtualLayoutManager.LayoutStateWrapper.ITEM_DIRECTION_TAIL)
                        || (bottom > mMarginBottom + mOffset + mAdjuster.bottom);
                if (needChangeProcedure) {
                    mDoNormalHandle = false;
                    mFixView = view;

                    bottom = orientationHelper.getEndAfterPadding() - mMarginBottom - mOffset - mAdjuster.bottom;
                    top = bottom - result.mConsumed;
                }
            } else {
                // should not use 0
                boolean needChangeProcedure = (remainingSpace < (mOffset + mAdjuster.top)
                        && layoutState.getItemDirection() == VirtualLayoutManager.LayoutStateWrapper.ITEM_DIRECTION_HEAD)
                        || (top < mMarginTop + mOffset + mAdjuster.top);
                if (needChangeProcedure) {
                    mDoNormalHandle = false;
                    mFixView = view;
                    top = orientationHelper.getStartAfterPadding() + mMarginTop + mOffset + mAdjuster.top;
                    bottom = top + result.mConsumed;
                } else {
                    // remaining current procedure
                }
            }

        } else {
            top = helper.getPaddingTop();
            bottom = top + orientationHelper.getDecoratedMeasurementInOther(view) + mMarginTop;

            if (layoutState.getLayoutDirection() == VirtualLayoutManager.LayoutStateWrapper.LAYOUT_START) {
                right = layoutState.getOffset() - mMarginRight;
                left = layoutState.getOffset() - result.mConsumed;
            } else {
                left = layoutState.getOffset() + mMarginLeft;
                right = layoutState.getOffset() + result.mConsumed;
            }
            if (helper.getReverseLayout() || !mStickyStart) {
                if (remainingSpace < mOffset + mAdjuster.right) {
                    mDoNormalHandle = false;
                    mFixView = view;

                    right = orientationHelper.getEndAfterPadding() - mOffset - mAdjuster.right;
                    left = right - result.mConsumed;
                }
            } else {
                if (remainingSpace < mOffset + mAdjuster.left) {
                    mDoNormalHandle = false;
                    mFixView = view;
                    left = orientationHelper.getStartAfterPadding() + mOffset + mAdjuster.left;
                    right = result.mConsumed;
                }
            }

        }


        layoutChild(view, left, top, right, bottom, helper);
        mBottom = bottom;
        Log.d(tag, "layoutViews() get bottom value -- left : " + left + " top : " + top +  " right : " + right + " bottom : " + bottom);

        result.mConsumed += (layoutInVertical ? getVerticalMargin() : getHorizontalMargin());

        if (state.isPreLayout()) {
            mDoNormalHandle = true;
        }

        if (mDoNormalHandle) {
            helper.addChildView(layoutState, view);
            handleStateOnResult(result, view);
            mFixView = null;
        } else {
            // result.mConsumed += mOffset;
        }


    }

    @Override
    public void beforeLayout(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutManagerHelper helper) {
        super.beforeLayout(recycler, state, helper);


        if (mFixView != null && helper.isViewHolderUpdated(mFixView)) {
            // recycle view for later usage
            helper.removeChildView(mFixView);
            recycler.recycleView(mFixView);
            mFixView = null;
        }

        mDoNormalHandle = false;
    }


    @Override
    public boolean requireLayoutView() {
        return false;
    }

    @Override
    public void afterLayout(RecyclerView.Recycler recycler, RecyclerView.State state, int startPosition, int endPosition, int scrolled,
                            LayoutManagerHelper helper) {
        super.afterLayout(recycler, state, startPosition, endPosition, scrolled, helper);

        // disabled if mPos is negative number
        if (mPos < 0) {
            return;
        }

        final OrientationHelper orientationHelper = helper.getMainOrientationHelper();

        // not normal flow,
        if (!mDoNormalHandle && mPos >= startPosition && mPos <= endPosition) {
            fixLayoutStateFromAbnormal2Normal(orientationHelper, recycler, startPosition, endPosition, helper);
        }

        if (mDoNormalHandle || state.isPreLayout()) {
            if (!state.isPreLayout()) {
                // TODO: sticky only support one item now

            }
            if (mFixView != null) {
                helper.removeChildView(mFixView);
            } else {
                // mDoNormalHandle == true && mFixView == null
                return;
            }
        }


        // Not in normal flow
        if (!mDoNormalHandle && mFixView != null) {
            // already capture in layoutViews phase
            // if it's not shown on screen
            if (mFixView.getParent() == null) {
                helper.addFixedView(mFixView);
            } else {
                fixLayoutStateInCase1(orientationHelper, recycler, startPosition, endPosition, helper);
            }
        } else {
            fixLayoutStateInCase2(orientationHelper, recycler, startPosition, endPosition, helper);
        }
    }

    protected void fixLayoutStateFromAbnormal2Normal(OrientationHelper orientationHelper, RecyclerView.Recycler recycler, int startPosition, int endPosition,
                                                     LayoutManagerHelper helper) {
        //fix status, from abnormal to normal
        if (mFixView != null) {
            int top, bottom;
            View refer = null;
            if (mStickyStart) {
                for (int i = helper.getChildCount() - 1; i >= 0; i--) {
                    refer = helper.getChildAt(i);
                    int anchorPos = helper.getPosition(refer);

                    // TODO: when view size is larger than totalSpace!
                    if (anchorPos < mPos) {
                        top = orientationHelper.getDecoratedEnd(refer);
                        LayoutHelper layoutHelper = helper.findLayoutHelperByPosition(anchorPos);
                        if (layoutHelper instanceof MarginLayoutHelper) {
                            top = top + ((MarginLayoutHelper) layoutHelper).mMarginBottom + ((MarginLayoutHelper) layoutHelper).mPaddingBottom;
                        }
                        if (top >= mOffset + mAdjuster.top) {
                            mDoNormalHandle = true;
                        }

                        break;
                    }
                }


            } else {
                for (int i = 0; i < helper.getChildCount(); i++) {
                    refer = helper.getChildAt(i);
                    int anchorPos = helper.getPosition(refer);
                    if (anchorPos > mPos) {
                        bottom = orientationHelper.getDecoratedStart(refer);
                        LayoutHelper layoutHelper = helper.findLayoutHelperByPosition(anchorPos);
                        if (layoutHelper instanceof MarginLayoutHelper) {
                            bottom = bottom - ((MarginLayoutHelper) layoutHelper).mMarginTop - ((MarginLayoutHelper) layoutHelper).mPaddingTop;
                        }
                        if (bottom >= mOffset + mAdjuster.bottom) {
                            mDoNormalHandle = true;
                        }
                        break;
                    }
                }
            }
        }
    }

    protected void fixLayoutStateInCase1(OrientationHelper orientationHelper, RecyclerView.Recycler recycler, int startPosition, int endPosition,
                                         LayoutManagerHelper helper) {
        // considering the case when last layoutHelper has margin bottom
        // 1. normal flow to abnormal flow; 2. abnormal flow to normal flow
        boolean isReachedBorderViews = (mStickyStart && endPosition >= mPos) || (!mStickyStart && startPosition <= mPos);
        if (isReachedBorderViews) {
            int consumed = orientationHelper.getDecoratedMeasurement(mFixView);
            boolean layoutInVertical = helper.getOrientation() == VERTICAL;
            final int startAdjust = layoutInVertical ? mAdjuster.top : mAdjuster.left;
            final int endAdjust = layoutInVertical ? mAdjuster.bottom : mAdjuster.right;

            int left = 0, top = 0, right = 0, bottom = 0;
            int index = -1;
            if (layoutInVertical) {
                // not support RTL now
                if (helper.isDoLayoutRTL()) {
                    right = helper.getContentWidth() - helper.getPaddingRight();
                    left = right - orientationHelper.getDecoratedMeasurementInOther(mFixView);
                } else {
                    left = helper.getPaddingLeft();
                    right = left + orientationHelper.getDecoratedMeasurementInOther(mFixView);
                }

                View refer = null;
                if (mStickyStart) {
                    for (int i = 0; i < helper.getChildCount(); i++) {
                        refer = helper.getChildAt(i);
                        int anchorPos = helper.getPosition(refer);
                        // TODO: when view size is larger than totalSpace!
                        if (anchorPos > mPos) {
                            bottom = orientationHelper.getDecoratedStart(refer);
                            LayoutHelper layoutHelper = helper.findLayoutHelperByPosition(anchorPos);
                            if (layoutHelper instanceof MarginLayoutHelper) {
                                bottom = bottom - ((MarginLayoutHelper) layoutHelper).mMarginTop - ((MarginLayoutHelper) layoutHelper).mPaddingTop;
                            }
                            top = bottom - consumed;
                            index = i + 1;
                            mDoNormalHandle = true;
                            break;
                        }
                    }
                } else {
                    for (int i = helper.getChildCount() - 1; i >= 0; i--) {
                        refer = helper.getChildAt(i);
                        int anchorPos = helper.getPosition(refer);
                        if (anchorPos < mPos) {
                            top = orientationHelper.getDecoratedEnd(refer);
                            LayoutHelper layoutHelper = helper.findLayoutHelperByPosition(anchorPos);
                            if (layoutHelper instanceof MarginLayoutHelper) {
                                top = top + ((MarginLayoutHelper) layoutHelper).mMarginBottom + ((MarginLayoutHelper) layoutHelper).mPaddingBottom;
                            }
                            bottom = top + consumed;

                            index = i;
                            mDoNormalHandle = true;
                            break;
                        }
                    }
                }

                if (refer == null || index < 0) {
                    // can not find normal view for insert
                    mDoNormalHandle = false;
                }

                if (helper.getReverseLayout() || !mStickyStart) {
                    if (bottom > orientationHelper.getEndAfterPadding() - mOffset - endAdjust) {
                        mDoNormalHandle = false;
                    }
                } else {
                    if (top < orientationHelper.getStartAfterPadding() + mOffset + startAdjust) {
                        mDoNormalHandle = false;
                    }
                }

                if (!mDoNormalHandle) {
                    if (helper.getReverseLayout() || !mStickyStart) {
                        bottom = orientationHelper.getEndAfterPadding() - mOffset - endAdjust;
                        top = bottom - consumed;
                    } else {
                        top = orientationHelper.getStartAfterPadding() + mOffset + startAdjust;
                        bottom = top + consumed;
                    }
                }
            } else {
                top = helper.getPaddingTop();
                bottom = top + orientationHelper.getDecoratedMeasurementInOther(mFixView);

                if (mDoNormalHandle) {
                    View refer = null;
                    if (mStickyStart) {
                        for (int i = helper.getChildCount() - 1; i >= 0; i--) {
                            refer = helper.getChildAt(i);
                            int anchorPos = helper.getPosition(refer);
                            // TODO: when view size is larger than totalSpace!
                            if (anchorPos < mPos) {
                                left = orientationHelper.getDecoratedEnd(refer);
                                right = left + consumed;
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < helper.getChildCount(); i++) {
                            refer = helper.getChildAt(i);
                            int anchorPos = helper.getPosition(refer);
                            if (anchorPos > mPos) {
                                right = orientationHelper.getDecoratedStart(refer);
                                left = right - consumed;
                                break;
                            }
                        }
                    }
                } else if (helper.getReverseLayout() || !mStickyStart) {
                    right = orientationHelper.getEndAfterPadding() - mOffset - endAdjust;
                    left = right - consumed;
                } else {
                    left = orientationHelper.getStartAfterPadding() + mOffset + startAdjust;
                    right = left + consumed;
                }

            }

            layoutChild(mFixView, left, top, right, bottom, helper);
            mBottom = bottom;
            Log.d(tag, "fixLayoutStateInCase1() get bottom value -- left : " + left + " top : " + top +  " right : " + right + " bottom : " + bottom);

            if (mDoNormalHandle) {
                // offset
                if (index >= 0) {
                    //// TODO: 2017/12/18 to be studied
                    helper.addChildView(mFixView, index);
                    mFixView = null;
                }
            } else {
                helper.showView(mFixView);
                helper.addFixedView(mFixView);
            }
        } else {
            helper.removeChildView(mFixView);
            helper.recycleView(mFixView);
            mFixView = null;
        }
    }

    protected void fixLayoutStateInCase2(OrientationHelper orientationHelper, RecyclerView.Recycler recycler, int startPosition, int endPosition,
                                         LayoutManagerHelper helper) {
        // 1. normal flow to abnormal flow; 2. abnormal flow to normal flow
        // (mDoNormalHandle && mFixView != null) || (!mDoNormalHandle && mFixView == null)
        View eView = mFixView;
        if (eView == null) {
            // !mDoNormalHandle && mFixView == null, find existing view
            eView = helper.findViewByPosition(mPos);
        }

        boolean normalHandle = false;
        boolean layoutInVertical = helper.getOrientation() == VERTICAL;
        final int startAdjust = layoutInVertical ? mAdjuster.top : mAdjuster.left;
        final int endAdjust = layoutInVertical ? mAdjuster.bottom : mAdjuster.right;

        boolean isReachedBorderView = (mStickyStart && endPosition >= mPos) || (!mStickyStart && startPosition <= mPos);
        if (isReachedBorderView) {
            if (eView == null) {
                // TODO? why do condition here?
                boolean isNormalProcedure = mOffset + (mStickyStart ? startAdjust : endAdjust) > 0;
                if (isNormalProcedure) {
                    normalHandle = true;
                }
                mFixView = recycler.getViewForPosition(mPos);
                doMeasure(mFixView, helper);
            } else if (mStickyStart && orientationHelper.getDecoratedStart(eView) >= orientationHelper.getStartAfterPadding() + mOffset + startAdjust) {
                // normal
                normalHandle = true;
                mFixView = eView;
            } else if (!mStickyStart && orientationHelper.getDecoratedEnd(eView) <= orientationHelper.getEndAfterPadding() - mOffset - endAdjust) {
                // normal
                normalHandle = true;
                mFixView = eView;
            } else {
                // abnormal
                // TODO: reuse views
                // mFixView = recycler.getViewForPosition(mPos);
                mFixView = eView;
            }
        }


        if (mFixView != null) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mFixView.getLayoutParams();

            if (params.isItemRemoved()) {
                // item is removed
                return;
            }

            // when do measure in after layout no need to consider scrolled
            // doMeasure(mFixView, helper);

            // do layout

            int consumed = orientationHelper.getDecoratedMeasurement(mFixView);


            int left = 0, top = 0, right = 0, bottom = 0;
            int index = -1;
            if (layoutInVertical) {
                // not support RTL now
                if (helper.isDoLayoutRTL()) {
                    right = helper.getContentWidth() - helper.getPaddingRight();
                    left = right - orientationHelper.getDecoratedMeasurementInOther(mFixView);
                } else {
                    left = helper.getPaddingLeft();
                    right = left + orientationHelper.getDecoratedMeasurementInOther(mFixView);
                }

                if (normalHandle) {
                    View refer = null;
                    if (mStickyStart) {
                        for (int i = helper.getChildCount() - 1; i >= 0; i--) {
                            refer = helper.getChildAt(i);
                            int anchorPos = helper.getPosition(refer);

                            // TODO: when view size is larger than totalSpace!
                            if (anchorPos < mPos) {
                                top = orientationHelper.getDecoratedEnd(refer);
                                LayoutHelper layoutHelper = helper.findLayoutHelperByPosition(anchorPos);
                                if (layoutHelper instanceof MarginLayoutHelper) {
                                    top = top + ((MarginLayoutHelper) layoutHelper).mMarginBottom + ((MarginLayoutHelper) layoutHelper).mPaddingBottom;
                                }
                                bottom = top + consumed;
                                index = i + 1;
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < helper.getChildCount(); i++) {
                            refer = helper.getChildAt(i);
                            int anchorPos = helper.getPosition(refer);
                            if (anchorPos > mPos) {
                                bottom = orientationHelper.getDecoratedStart(refer);
                                LayoutHelper layoutHelper = helper.findLayoutHelperByPosition(anchorPos);
                                if (layoutHelper instanceof MarginLayoutHelper) {
                                    bottom = bottom - ((MarginLayoutHelper) layoutHelper).mMarginTop - ((MarginLayoutHelper) layoutHelper).mPaddingTop;
                                }
                                top = bottom - consumed;
                                index = i;
                                break;
                            }
                        }
                    }

                    if (refer == null || index < 0) {
                        // can not find normal view for insert
                        normalHandle = false;
                    }

                    if (helper.getReverseLayout() || !mStickyStart) {
                        if (bottom > orientationHelper.getEndAfterPadding() - mOffset - endAdjust) {
                            normalHandle = false;
                        }
                    } else {
                        if (top < orientationHelper.getStartAfterPadding() + mOffset + startAdjust) {
                            normalHandle = false;
                        }
                    }

                }

                if (!normalHandle) {
                    if (helper.getReverseLayout() || !mStickyStart) {
                        bottom = orientationHelper.getEndAfterPadding() - mOffset - endAdjust;
                        top = bottom - consumed;
                    } else {
                        top = orientationHelper.getStartAfterPadding() + mOffset + startAdjust;
                        bottom = top + consumed;
                    }
                }

            } else {
                top = helper.getPaddingTop();
                bottom = top + orientationHelper.getDecoratedMeasurementInOther(mFixView);

                if (normalHandle) {
                    View refer = null;
                    if (mStickyStart) {
                        for (int i = helper.getChildCount() - 1; i >= 0; i--) {
                            refer = helper.getChildAt(i);
                            int anchorPos = helper.getPosition(refer);

                            // TODO: when view size is larger than totalSpace!
                            if (anchorPos < mPos) {
                                left = orientationHelper.getDecoratedEnd(refer);
                                right = left + consumed;
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < helper.getChildCount(); i++) {
                            refer = helper.getChildAt(i);
                            int anchorPos = helper.getPosition(refer);
                            if (anchorPos > mPos) {
                                right = orientationHelper.getDecoratedStart(refer);
                                left = right - consumed;
                                break;
                            }
                        }
                    }
                } else if (helper.getReverseLayout() || !mStickyStart) {
                    right = orientationHelper.getEndAfterPadding() - mOffset - endAdjust;
                    left = right - consumed;
                } else {
                    left = orientationHelper.getStartAfterPadding() + mOffset + startAdjust;
                    right = left + consumed;
                }

            }

            layoutChild(mFixView, left, top, right, bottom, helper);
            mBottom = bottom;
            Log.d(tag, "fixLayoutStateInCase2() get bottom value -- left : " + left + " top : " + top +  " right : " + right + " bottom : " + bottom);

            if (normalHandle) {
                // offset
                if (index >= 0) {
                    helper.addChildView(mFixView, index);
                    mFixView = null;
                }
            } else {
                helper.addFixedView(mFixView);
            }

        }
        mDoNormalHandle = normalHandle;
    }


    @Nullable
    @Override
    public View getFixedView() {
        return mFixView;
    }

    @Override
    public void onClear(LayoutManagerHelper helper) {
        super.onClear(helper);
        if (mFixView != null) {
            helper.removeChildView(mFixView);
            helper.recycleView(mFixView);
            mFixView = null;
        }
    }

    private void doMeasure(View view, LayoutManagerHelper helper) {
        final VirtualLayoutManager.LayoutParams params = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        final boolean layoutInVertical = helper.getOrientation() == VERTICAL;

        int widthSize = helper.getContentWidth() - helper.getPaddingLeft() - helper.getPaddingRight() - getHorizontalMargin();
        int heightSize = helper.getContentHeight() - helper.getPaddingTop() - helper.getPaddingBottom() - getVerticalMargin();
        float viewAspectRatio = params.mAspectRatio;

        if (layoutInVertical) {
            final int widthSpec = helper.getChildMeasureSpec(widthSize, params.width, false);
            int heightSpec;
            if (!Float.isNaN(viewAspectRatio) && viewAspectRatio > 0) {
                heightSpec = View.MeasureSpec.makeMeasureSpec((int) (widthSize / viewAspectRatio + 0.5f), View.MeasureSpec.EXACTLY);
            } else if (!Float.isNaN(mAspectRatio) && mAspectRatio > 0) {
                heightSpec = View.MeasureSpec.makeMeasureSpec((int) (widthSize / mAspectRatio + 0.5), View.MeasureSpec.EXACTLY);
            } else {
                heightSpec = helper.getChildMeasureSpec(heightSize, params.height, true);
            }

            helper.measureChildWithMargins(view, widthSpec, heightSpec);
        } else {
            final int heightSpec = helper.getChildMeasureSpec(heightSize, params.height, false);
            int widthSpec;
            if (!Float.isNaN(viewAspectRatio) && viewAspectRatio > 0) {
                widthSpec = View.MeasureSpec.makeMeasureSpec((int) (heightSize * viewAspectRatio + 0.5), View.MeasureSpec.EXACTLY);
            } else if (!Float.isNaN(mAspectRatio) && mAspectRatio > 0) {
                widthSpec = View.MeasureSpec.makeMeasureSpec((int) (heightSize * mAspectRatio + 0.5), View.MeasureSpec.EXACTLY);
            } else {
                widthSpec = helper.getChildMeasureSpec(widthSize, params.width, true);
            }

            helper.measureChildWithMargins(view, widthSpec, heightSpec);
        }

    }

}


