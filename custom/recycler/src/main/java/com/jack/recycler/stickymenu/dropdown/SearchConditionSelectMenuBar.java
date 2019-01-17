package com.jack.recycler.stickymenu.dropdown;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.recycler.R;
import com.jack.recycler.stickymenu.dropdown.adapter.SecondaryMenuContainerAdapter;
import com.jack.util.CollectionUtils;
import com.jack.util.DisplayUtils;

import java.util.List;

/**
 * @author liuyang
 * @date 2017/12/12
 */

public class SearchConditionSelectMenuBar extends RelativeLayout implements SecondaryMenuLabelContainer.OnRefreshClassifyMenuListener {
    /**
     * 一级菜单栏标题字体大小
     */
    private int firstLevelMenuTitleSize;
    /**
     * 二级菜单栏筛选项内部item字体大小
     */
    private int secondaryMenuItemTextSize;

    /**
     * 未选中菜单栏的颜色
     */
    private int labelTextNormalColor;
    /**
     * 被选中的菜单栏的颜色
     */
    private int labelTextSelectedColor;

    /**
     * 一级菜单栏每个label的高度
     */
    private int firstLevelMenuLabelHeight;
    /**
     * 二级菜单菜单栏item项的高度
     */
    private int secondaryMenuItemHeight;

    private SecondaryMenuLabelContainer mSecondaryMenuLabelContainer;
    private SecondaryMenuContainerAdapter mSecondaryMenuContainerAdapter;

    private OnMenuSelectedListener mOnMenuSelectedListener;

    private List<MenuData> mFirstLevelMenuData;
    private ArrayMap<String, List<MenuData>> mSecondaryMenuData;

    public SearchConditionSelectMenuBar(Context context) {
        super(context);
        firstLevelMenuTitleSize = DisplayUtils.dp2px(context, 14);
        secondaryMenuItemTextSize = DisplayUtils.dp2px(context, 12);
        firstLevelMenuLabelHeight = DisplayUtils.dp2px(context, 40);
        secondaryMenuItemHeight = DisplayUtils.dp2px(context, 30);
        labelTextNormalColor = Color.BLACK;
        labelTextSelectedColor = Color.RED;
        setBackgroundColor(Color.parseColor("#00000000"));

        initMenuContainers();
    }

    private void initMenuContainers() {
        LinearLayout layoutFirstLevelMenu = new LinearLayout(getContext());
        layoutFirstLevelMenu.setOrientation(LinearLayout.HORIZONTAL);
        layoutFirstLevelMenu.setId(R.id.drop_down_first_level_menu_layout);
        LayoutParams firstLevelLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(layoutFirstLevelMenu, firstLevelLayoutParams);

        initSecondaryMenuContainer();
    }

    public void setOnMenuSelectedListener(OnMenuSelectedListener listener) {
        mOnMenuSelectedListener = listener;
        if (mSecondaryMenuLabelContainer != null) {
            mSecondaryMenuLabelContainer.setOnMenuSelectedListener(listener);
        }
    }

    /**
     * 如果当前的数据显示机制中的key是非integer或long的数据，则SparseArray要换成ArrayMap结构
     *
     * @param firstMenuData 一级菜单数据集合
     * @param secondaryData 二级菜单数据集合，与一级菜单数据一一对应：
     *                      i位置上的一级菜单所对应的二级菜单也在i位置上
     */
    public void constructMenu(List<MenuData> firstMenuData, ArrayMap<String, List<MenuData>> secondaryData, boolean needManageSecondary) {
        if (CollectionUtils.isEmpty(firstMenuData)) {
            notifyHideSecondaryMenu();
            setVisibility(View.GONE);
            return;
        }

        LinearLayout layoutFirstLevelMenu = (LinearLayout) findViewById(R.id.drop_down_first_level_menu_layout);
        int paddingHornal = DisplayUtils.dp2px(getContext(), 5);

        for (int i = 0; i < firstMenuData.size(); i++) {
            List<MenuData> categoryListData = null;
            MenuData targetCategoryData = firstMenuData.get(i);
            if (targetCategoryData == null || !targetCategoryData.isValidate()) {
                continue;
            }
            if (secondaryData != null) {
                String key = targetCategoryData.getCode();
                categoryListData = secondaryData.get(key);
            }
            TextView menuLabel = generateFirstLevelMenuLabel(targetCategoryData, categoryListData, i, needManageSecondary);
            menuLabel.setPadding(paddingHornal, 0, paddingHornal, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, firstLevelMenuLabelHeight);
            params.weight = 0.25f;
            menuLabel.setLayoutParams(params);
            menuLabel.setGravity(Gravity.CENTER);
            layoutFirstLevelMenu.addView(menuLabel, params);
        }

        mFirstLevelMenuData = firstMenuData;
        mSecondaryMenuData = secondaryData;

    }

    private void initSecondaryMenuContainer() {
        mSecondaryMenuLabelContainer = new SecondaryMenuLabelContainer(getContext());
        mSecondaryMenuLabelContainer.setId(R.id.drop_down_secondary_menu_layout);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(BELOW, R.id.drop_down_first_level_menu_layout);
        mSecondaryMenuLabelContainer.setLayoutParams(params);
        addView(mSecondaryMenuLabelContainer, params);
        mSecondaryMenuLabelContainer.setVisibility(View.GONE);
        mSecondaryMenuLabelContainer.setOnRefreshClassifyTabListener(this);
    }

    private int mLastSelectedPosition = -1;

    /**
     * 构建第i个位置上的一级菜单菜单栏, 返回构建好的菜单栏目组件
     *
     * @param menuData      一级菜单的显示标题与对应code
     * @param secondaryData 当前一级菜单所对应的二级菜单的数据
     * @param position      当前需要添加的菜单数据的位置，作为该控件的id
     * @param neeaManageSecondary 当前接口是否需要管理二级菜单，true：需要内部构建，
     *                                                      false：外部构建，通过listener进行外部构建
     */
    private TextView generateFirstLevelMenuLabel(final MenuData menuData, final List<MenuData> secondaryData, final int position, final boolean neeaManageSecondary) {
        final TextView menuLabel = new TextView(getContext());
        menuLabel.setMaxLines(1);

        /**重要！！！*/
        menuLabel.setId(position);
        menuLabel.setEllipsize(TextUtils.TruncateAt.END);
        menuLabel.setTag(menuData);
        final boolean needSecondaryMenu = !CollectionUtils.isEmpty(secondaryData);

        String labelTitle = null;
        int labelTextColor = labelTextNormalColor;
        if (needSecondaryMenu) {
            String label = generateSelectedMenuItemLabel(secondaryData);
            if (!TextUtils.isEmpty(label)) {
                labelTitle = label;
                labelTextColor = labelTextSelectedColor;
//                menuLabel.setText(label);
//                menuLabel.setTextColor(labelTextSelectedColor);
            } else {
                labelTitle = menuData.getLabel();
//                menuLabel.setText(menuData.getLabel());
//                menuLabel.setTextColor(labelTextNormalColor);
            }
        } else {
            labelTitle = menuData.getLabel();
            if (menuData.isSelected) {

                labelTextColor = labelTextSelectedColor;
//                menuLabel.setText(menuData.getLabel());
//                menuLabel.setTextColor(labelTextSelectedColor);
            } else {
//                menuLabel.setText(menuData.getLabel());
//                menuLabel.setTextColor(labelTextNormalColor);
                labelTextColor = labelTextNormalColor;
            }
        }

        menuLabel.setText(labelTitle);
        menuLabel.setTextColor(labelTextColor);
//        final int secondaryMarginTop = menuLabel.getBottom();

        menuLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                menuData.setSelected(!menuData.isSelected);
//                if (menuData.isSelected) {
//                    if (mLastSelectedPosition != position) {
//                        resetPositionView(mLastSelectedPosition);
//                        mLastSelectedPosition = position;
//
//                    }
//                }
//                switchMenuTab(menuLabel , menuData.isSelected);

                Toast.makeText(getContext(), "clicked ", Toast.LENGTH_SHORT).show();
                mSecondaryMenuLabelContainer.reverseSelectStatus();

                if (CollectionUtils.isEmpty(secondaryData)) {
                    menuData.setSelected(!menuData.isSelected);

                    switchMenuTab(menuLabel, menuData.isSelected, position);
                    mSecondaryMenuLabelContainer.setVisibility(View.GONE);
                    mOnMenuSelectedListener.onHideSecondaryMenuContainer();

                    if (mOnMenuSelectedListener != null) {
                        mOnMenuSelectedListener.onCategoryMenuSelected();
                    }
                    return;
                }
                if (!neeaManageSecondary) {
                    mOnMenuSelectedListener.onConstructSecondaryMenuContainer(position, menuData.code, mCurrentBottom);
                    return;
                }

                if (!TextUtils.equals(mSecondaryMenuLabelContainer.getClassifyCode(), menuData.getCode())) {
//                    mSecondaryMenuLabelContainer.reverseSelectStatus();
//                    mSecondaryMenuLabelContainer.setVisibility(View.GONE);

                    if (needSecondaryMenu) {
                        constructSecondaryMenu(menuData.getCode(), secondaryData);
                    }
                } else {
                    int visibility = mSecondaryMenuLabelContainer.getVisibility() == VISIBLE ? GONE : VISIBLE;
                    if (visibility != View.VISIBLE) {
//                        mSecondaryMenuLabelContainer.reverseSelectStatus();
                        mSecondaryMenuLabelContainer.setVisibility(View.GONE);
                    } else {
                        mSecondaryMenuLabelContainer.display();
                    }
                }
            }
        });

        return menuLabel;
    }

//    private void reverseMenuListStatus(List<MenuData> rawData) {
//        if (CollectionUtils.isEmpty(rawData)) {
//            return;
//        }
//
//        for (int i = 0; i < rawData.size(); i++) {
//            MenuData data = rawData.get(i);
//            if (data == null) {
//                continue;
//            }
//            data.reverseStatus();
//        }
//    }

    /**
     * 判断当前id的子 View是否包含二级菜单
     */
    private boolean isCurrentViewContainsSubMenu(View childView) {
        if (mSecondaryMenuData == null || mSecondaryMenuData.size() <= 0) {
            return false;
        }
        if (childView != null && childView.getTag() != null && childView.getTag() instanceof MenuData) {
            MenuData menuData = (MenuData) childView.getTag();
            return !CollectionUtils.isEmpty(mSecondaryMenuData.get(menuData.getCode()));
        }

        return false;
    }

    @Override
    public void onMeasure(int width, int height) {
        super.onMeasure(width, height);
    }

    int mCurrentBottom = 0;

    @Override
    public void onLayout(boolean isChanged, int left, int top, int right, int bottom) {
        super.onLayout(isChanged, left, top, right, bottom);
        mCurrentBottom = getBottom();
    }

    private void resetPositionView(int positoin) {
        View childview = findViewById(positoin);
        if (childview != null && childview instanceof TextView) {
            ((TextView) childview).setTextColor(labelTextNormalColor);
            if (childview.getTag() != null && childview.getTag() instanceof MenuData) {
                ((MenuData) childview.getTag()).isSelected = false;
            }
        }
    }

    private int preSelectedMenuWithoutSubMenuPosition = -1;

    private void switchMenuTab(TextView view, boolean isSelected, int position) {
        view.setTextColor(isSelected ? labelTextSelectedColor : labelTextNormalColor);

        //// TODO: 2017/12/14 确认没有二级筛选项的菜单之间是否互斥
        /**如果单列的筛选项也需要互斥，则打开以下的代码;不是互斥的话，
         *则需要用户自己点击筛选项才能执行筛选项的选中和取消选中状态*/
        if (isSelected) {
            resetPositionView(preSelectedMenuWithoutSubMenuPosition);
            preSelectedMenuWithoutSubMenuPosition = position;
        } else {
            preSelectedMenuWithoutSubMenuPosition = -1;
        }

    }

    private String generateSelectedMenuItemLabel(List<MenuData> secondaryData) {
        if (CollectionUtils.isEmpty(secondaryData)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < secondaryData.size(); i++) {
            MenuData menuData = secondaryData.get(i);
            if (menuData == null || !menuData.isValidate()) {
                continue;
            }
            if (menuData.isSelected) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(menuData.getLabel());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 添加二级菜单控件
     */
    private void constructSecondaryMenu(String code, List<MenuData> data) {
        if (mSecondaryMenuLabelContainer != null) {
            mSecondaryMenuLabelContainer.setLabelData(code, data);
        }
    }

    public void notifyHideSecondaryMenu() {
        if (mOnMenuSelectedListener != null) {
            mOnMenuSelectedListener.onHideSecondaryMenuContainer();
        }
    }

    @Override
    public void onUpdateClassifyTab(String code, String title) {
        View view = findViewById(R.id.drop_down_first_level_menu_layout);
        if (view == null || !(view instanceof LinearLayout)) {
            return;
        }

        LinearLayout firstMenuParentLayout = (LinearLayout) view;
        for (int i = 0; i < firstMenuParentLayout.getChildCount(); i++) {
            View childView = firstMenuParentLayout.getChildAt(i);
            if (!(childView.getTag() != null && childView.getTag() instanceof MenuData)) {
                continue;
            }
            MenuData data = (MenuData) childView.getTag();
            if (TextUtils.equals(data.getCode(), code)) {
                boolean isSelected = !TextUtils.isEmpty(title);
                childView.setSelected(isSelected);
                ((TextView)childView).setTextColor(isSelected ? labelTextSelectedColor : labelTextNormalColor);
                ((TextView) childView).setText(isSelected ? title : data.getLabel());
            }
        }

    }

    /**
     * 一级菜单点击回调事件
     */
    interface OnFirstLevelMenuCellClicked {
        /**
         * 一级菜单点击回调事件,点击构建当前菜单下的二级菜单
         *
         * @param position 当前菜单所在的位置
         * @param code     当前菜单对应的code ，在参数列表中以code=value1，value2存在
         */
        public void onFirstMenuClicked(int position, String code);
    }

    public static class MenuData {
        String code;
        String label;

        /**
         * 用户是否最终选中状态
         */
        boolean isSelected = false;
        /**
         * 用户是否暂时选中，用户点击选中，但是没有点击确认的中间状态
         */
        boolean needDisplay = false;

        public MenuData() {
            isSelected = false;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isNeedDisplay() {
            return needDisplay;
        }

        public void setNeedDisplay(boolean needDisplay) {
            this.needDisplay = needDisplay;
        }

        public boolean isValidate() {
            return (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(label));
        }

        public void reverseStatus() {
            needDisplay = isSelected;
        }
    }
}
