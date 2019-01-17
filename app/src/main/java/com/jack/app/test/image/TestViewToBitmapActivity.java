package com.jack.app.test.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.jack.app.R;

/**
 * Created by liuyang on 2017/11/17.
 */

public class TestViewToBitmapActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_image_score_view);
        viewToBitmap();
    }

    private void viewToBitmap() {

        new Thread() {
            @Override
            public void run() {
                RelativeLayout root = (RelativeLayout) findViewById(R.id.rl_draw_screen_root);
                Bitmap bitmap = Bitmap.createBitmap(480, 584, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                root.draw(canvas);
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
