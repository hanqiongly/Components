/*
 * MIT License
 *
 * Copyright (c) 2016 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.alibaba.android.vlayout.layout;

import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutManagerHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.VirtualLayoutManager.LayoutStateWrapper;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


/**
 * Layout which allow item sticky to start/end
 *
 * @author villadora
 * @since 1.0.0
 */
public class StickyLayoutHelper extends FixAreaLayoutHelper {

    private static final String tag = "test_StickyLayoutHelper";

    protected int mPos = -1;

    protected int mOffset = 0;


    protected View mFixView = null;
    protected boolean mDoNormalHandle = false;

    public StickyLayoutHelper() {
        this(true);
    }

    public StickyLayoutHelper(boolean stickyStart) {
        setItemCount(1);
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
    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutStateWrapper layoutState, LayoutChunkResult result, LayoutManagerHelper helper) {
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
        final OrientationHelper orientationHelper = helper.getMainOrientationHelper();
        result.mConsumed = orientationHelper.getDecoratedMeasurement(view);

        // Do normal measure&layout phase by default
        mDoNormalHandle = true;

        final int remainingSpace = layoutState.getAvailable() - result.mConsumed + layoutState.getExtra();

        int left, top, right, bottom;
            // not support RTL now
            if (helper.isDoLayoutRTL()) {
                right = helper.getContentWidth() - helper.getPaddingRight() - mMarginRight;
                left = right - orientationHelper.getDecoratedMeasurementInOther(view);
            } else {
                left = helper.getPaddingLeft() + mMarginLeft;
                right = left + orientationHelper.getDecoratedMeasurementInOther(view);
            }

            // whether this layout pass is layout to start or to end
            if (layoutState.getLayoutDirection() == LayoutStateWrapper.LAYOUT_START) {
                // fill start, from bottom to top
                bottom = layoutState.getOffset() - mMarginBottom;
                top = layoutState.getOffset() - result.mConsumed;
            } else {
                // fill end, from top to bottom
                    top = layoutState.getOffset() + mMarginTop;
                    bottom = layoutState.getOffset() + result.mConsumed;
            }


            if (helper.getReverseLayout()) {
                if ((remainingSpace < (mOffset + mAdjuster.bottom) && layoutState.getItemDirection() == LayoutStateWrapper.ITEM_DIRECTION_TAIL)
                        || (bottom > mMarginBottom + mOffset + mAdjuster.bottom)) {
                    mDoNormalHandle = false;
                    mFixView = view;

                    bottom = orientationHelper.getEndAfterPadding() - mMarginBottom - mOffset - mAdjuster.bottom;
                    top = bottom - result.mConsumed;
                }
            } else {
                // should not use 0
                if ((remainingSpace < (mOffset + mAdjuster.top) && layoutState.getItemDirection() == LayoutStateWrapper.ITEM_DIRECTION_HEAD)
                        || (top < mMarginTop + mOffset + mAdjuster.top)) {
                    mDoNormalHandle = false;
                    mFixView = view;
                    top = orientationHelper.getStartAfterPadding() + mMarginTop + mOffset + mAdjuster.top;
                    bottom = top + result.mConsumed;
                } else {
                    Log.i("Sticky", "remainingSpace: " + remainingSpace + "    offset: " + mOffset);
                }
            }


        layoutChild(view, left, top, right, bottom, helper);
        Log.d(tag, "layoutViews() get bottom value -- left : " + left + " top : " + top +  " right : " + right + " bottom : " + bottom);

        result.mConsumed += getVerticalMargin();

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
        Log.d(tag, "abnormal pos: " + mPos + " start: " + startPosition + " end: " + endPosition);
        if (mFixView != null) {
            int top, bottom;
            View refer = null;
                for (int i = helper.getChildCount() - 1; i >= 0; i--) {
                    refer = helper.getChildAt(i);
                    int anchorPos = helper.getPosition(refer);
                    if (anchorPos < mPos) { // TODO: when view size is larger than totalSpace!
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


        }
    }

    protected void fixLayoutStateInCase1(OrientationHelper orientationHelper, RecyclerView.Recycler recycler, int startPosition, int endPosition,
        LayoutManagerHelper helper) {
        // considering the case when last layoutHelper has margin bottom
        // 1. normal flow to abnormal flow; 2. abnormal flow to normal flow
        if ( endPosition >= mPos) {
            int consumed = orientationHelper.getDecoratedMeasurement(mFixView);
            boolean layoutInVertical = helper.getOrientation() == VERTICAL;
            final int startAdjust = layoutInVertical ? mAdjuster.top : mAdjuster.left;
            final int endAdjust = layoutInVertical ? mAdjuster.bottom : mAdjuster.right;

            int left = 0, top = 0, right = 0, bottom = 0;
            int index = -1;
                // not support RTL now
                if (helper.isDoLayoutRTL()) {
                    right = helper.getContentWidth() - helper.getPaddingRight();
                    left = right - orientationHelper.getDecoratedMeasurementInOther(mFixView);
                } else {
                    left = helper.getPaddingLeft();
                    right = left + orientationHelper.getDecoratedMeasurementInOther(mFixView);
                }

                View refer = null;
                    for (int i = 0; i < helper.getChildCount(); i++) {
                        refer = helper.getChildAt(i);
                        int anchorPos = helper.getPosition(refer);
                        if (anchorPos > mPos) { // TODO: when view size is larger than totalSpace!
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


                if (refer == null || index < 0) {
                    // can not find normal view for insert
                    mDoNormalHandle = false;
                }

                if (helper.getReverseLayout()) {
                    if (bottom > orientationHelper.getEndAfterPadding() - mOffset - endAdjust) {
                        mDoNormalHandle = false;
                    }
                } else {
                    if (top < orientationHelper.getStartAfterPadding() + mOffset + startAdjust) {
                        mDoNormalHandle = false;
                    }
                }

                if (!mDoNormalHandle) {
                    if (helper.getReverseLayout() ) {
                        bottom = orientationHelper.getEndAfterPadding() - mOffset - endAdjust;
                        top = bottom - consumed;
                    } else {
                        top = orientationHelper.getStartAfterPadding() + mOffset + startAdjust;
                        bottom = top + consumed;
                    }
                }


            layoutChild(mFixView, left, top, right, bottom, helper);
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
        final int startAdjust = mAdjuster.top ;
        final int endAdjust =  mAdjuster.bottom ;
        if (endPosition >= mPos) {

            if (eView == null) {
                // TODO? why do condition here?
                if (mOffset + startAdjust > 0) {
                    normalHandle = true;
                }
                mFixView = recycler.getViewForPosition(mPos);
                doMeasure(mFixView, helper);
            } else if (orientationHelper.getDecoratedStart(eView) >= orientationHelper.getStartAfterPadding() + mOffset + startAdjust) {
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
                        for (int i = helper.getChildCount() - 1; i >= 0; i--) {
                            refer = helper.getChildAt(i);
                            int anchorPos = helper.getPosition(refer);
                            if (anchorPos < mPos) { // TODO: when view size is larger than totalSpace!
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

                    if (refer == null || index < 0) {
                        // can not find normal view for insert
                        normalHandle = false;
                    }

                    if (helper.getReverseLayout()) {
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
                    if (helper.getReverseLayout()) {
                        bottom = orientationHelper.getEndAfterPadding() - mOffset - endAdjust;
                        top = bottom - consumed;
                    } else {
                        top = orientationHelper.getStartAfterPadding() + mOffset + startAdjust;
                        bottom = top + consumed;
                    }
                }


            layoutChild(mFixView, left, top, right, bottom, helper);
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

        int widthSize = helper.getContentWidth() - helper.getPaddingLeft() - helper.getPaddingRight() - getHorizontalMargin();
        int heightSize = helper.getContentHeight() - helper.getPaddingTop() - helper.getPaddingBottom() - getVerticalMargin();
        float viewAspectRatio = params.mAspectRatio;

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

    }

}

