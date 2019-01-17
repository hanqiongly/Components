package com.jack.viewgroup.floating;

import android.view.View;

import com.jack.viewgroup.floating.transition.FloatingTransition;

public class FloatingElement {
    /**
     * Floating effect, the default is ScaleFloatingTransition
     */
    public FloatingTransition floatingTransition;


    /**
     * X direction of offset, unit px
     */
    public int offsetX;

    /**
     * Y direction of offset, unit px
     */
    public int offsetY;


    /**
     * The view which you want to float
     */
    public View targetView;


    /**
     * The layout resources id of the view which you want to float
     */
    public int targetViewLayoutResId;

    /**
     * The view that you want to float above
     */
    public View anchorView;
}
