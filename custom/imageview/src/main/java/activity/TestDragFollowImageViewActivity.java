package activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jack.imageview.DragFollowImageView;
import com.jack.imageview.R;

/**
 * Created by liuyang on 2017/11/7.
 */

public class TestDragFollowImageViewActivity extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_drag_flow_layout);
        initView();
    }

    private void initView() {
        LinearLayout mRootlayer = (LinearLayout) findViewById(R.id.ll_root_for_drag);
        DragFollowImageView dragFollowImageView = (DragFollowImageView) findViewById(R.id.dv_drag_follow_image);

        int paddingLeft = mRootlayer.getPaddingLeft();
        int paddingRight = mRootlayer.getPaddingRight();
        int paddingTop = mRootlayer.getPaddingTop();
        int paddingBottom = mRootlayer.getPaddingBottom();

        int marginLeft = 0;
        int marginRight = 0;
        int marginTop = 0;
        int marginBottom = 0;

        ViewGroup.LayoutParams params = mRootlayer.getLayoutParams();
        if (params != null) {
           if (params instanceof LinearLayout.LayoutParams) {
               LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) params;
               marginLeft = linearParams.leftMargin;
               marginRight = linearParams.rightMargin;
               marginTop = linearParams.topMargin;
               marginBottom = linearParams.bottomMargin;
           } else if (params instanceof RelativeLayout.LayoutParams) {
               RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) params;
               marginLeft = relativeParams.leftMargin;
               marginRight = relativeParams.rightMargin;
               marginTop = relativeParams.topMargin;
               marginBottom = relativeParams.bottomMargin;
           }
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int windowWidth = displayMetrics.widthPixels;
        int windowHeight = displayMetrics.heightPixels;

        int rectTop = paddingTop + marginTop;
        int rectLeft = paddingLeft + marginLeft;
        int rectRight = windowWidth - marginRight - paddingRight;
        int rectBottom = windowHeight - marginBottom - paddingBottom;

        Rect area = new Rect(rectLeft, rectTop, rectRight, rectBottom);
        dragFollowImageView.setDragableArea(area);
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
