package com.jack.recycler.stickymenu.dropdown;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.recycler.R;
import com.jack.recycler.stickymenu.dropdown.adapter.SecondaryMenuContainerAdapter;
import com.jack.util.CollectionUtils;

import java.util.List;

/**
 * @author liuyang
 * @date 2017/12/13
 */

public class SecondaryMenuLabelContainer extends LinearLayout implements View.OnClickListener {
    private RecyclerView mRvSelectLabelContainer;

    /**
     * 当前分类筛选框所对应的一级分类类型码
     */
    private String mClassifyCode;
    private SecondaryMenuContainerAdapter mAdapter;
    private TextView mBtnEnsure;
    private TextView mBtnReset;

    private OnMenuSelectedListener mOnMenuSelectedListener;
    private OnRefreshClassifyMenuListener mOnRefreshClassifyMenuListener;

    public SecondaryMenuLabelContainer(Context context) {
        this(context, null);
    }

    public SecondaryMenuLabelContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SecondaryMenuLabelContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.secondary_menu_container_layout, this);
        mRvSelectLabelContainer = (RecyclerView) rootView.findViewById(R.id.rv_select_label_content_container);
        mRvSelectLabelContainer.setBackgroundColor(Color.parseColor("#00000000"));
        mBtnEnsure = (TextView) rootView.findViewById(R.id.tv_category_menu_ensure);
        mBtnReset = (TextView) rootView.findViewById(R.id.tv_category_menu_reset);
        findViewById(R.id.ll_layer_btn).setOnClickListener(this);
        mBtnEnsure.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
        mAdapter = new SecondaryMenuContainerAdapter();
        mRvSelectLabelContainer.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRvSelectLabelContainer.setLayoutManager(gridLayoutManager);
    }

    public void setOnMenuSelectedListener(OnMenuSelectedListener menuSelectedListener) {
        mOnMenuSelectedListener = menuSelectedListener;
    }

    public void setOnRefreshClassifyTabListener(OnRefreshClassifyMenuListener listener) {
        mOnRefreshClassifyMenuListener = listener;
    }

    private void resetSelection() {
        mAdapter.resetSelectedCollection();
    }

    public void display() {
        mRvSelectLabelContainer.scrollToPosition(0);
        setVisibility(View.VISIBLE);
    }

    public void setLabelData(String classifyCode, List<SearchConditionSelectMenuBar.MenuData> data) {
        if (TextUtils.isEmpty(classifyCode) || CollectionUtils.isEmpty(data)) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        mClassifyCode = classifyCode;
        mAdapter.setData(data);
        mRvSelectLabelContainer.scrollToPosition(0);
    }

    public String getClassifyCode() {
        return mClassifyCode;
    }

    private String setCurrentMenuStatus() {
        String title = null;
        if (mAdapter != null) {
            title = mAdapter.checkCurrentListStatus();
        }

        return title;
    }

    public void reverseSelectStatus() {
        if (mAdapter != null) {
            mAdapter.reverseMenuListStatus();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tv_category_menu_ensure) {
            String title = setCurrentMenuStatus();
            //// TODO: 2017/12/14 更新一级菜单的显示数据
            if (mOnRefreshClassifyMenuListener != null) {
                mOnRefreshClassifyMenuListener.onUpdateClassifyTab(mClassifyCode, title);
            }

            if (mOnMenuSelectedListener != null) {
                mOnMenuSelectedListener.onCategoryMenuSelected();
            }
            setVisibility(GONE);

        } else if (viewId == R.id.tv_category_menu_reset) {
            resetSelection();
        } else if (viewId == R.id.ll_layer_btn) {
            reverseSelectStatus();
            setVisibility(GONE);
        }
//        switch (viewId) {
//            case R.id.tv_category_menu_ensure:
//
//                break;
//            case R.id.tv_category_menu_reset:
//
//                break;
//            default:
//                break;
//        }
    }

    public interface OnRefreshClassifyMenuListener {
        /**
         * 根据当前二级面板的选中状态来更新一级菜单的数据状态
         *
         * @param code  当前对应的tab属于的code
         * @param title 更新状态所对应的文案（空则显示分类的title）
         */
        public void onUpdateClassifyTab(String code, String title);
    }
}
