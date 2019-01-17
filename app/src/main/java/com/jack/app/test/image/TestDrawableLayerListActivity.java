package com.jack.app.test.image;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import com.jack.app.R;
import com.jack.imageview.MultiImageDisplayWidget;

/**
 * Created by liuyang on 2018/9/7.
 */

public class TestDrawableLayerListActivity extends Activity implements ImageLoadController.OnDrawableLoadedListener{
    private ImageLoadController imageLoadController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_drawable_layer_list_activity_layout);
        MultiImageDisplayWidget multiImageDisplayWidget = findViewById(R.id.miv_display_widget);

        imageLoadController = new ImageLoadController(this, this);
        multiImageDisplayWidget.setImageUrls(imageLoadController.getUrls());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDrawableLoaded(int count, Drawable[] drawables) {
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
//        ivDisplayLayerDrawable.setImageDrawable(layerDrawable);
    }
}