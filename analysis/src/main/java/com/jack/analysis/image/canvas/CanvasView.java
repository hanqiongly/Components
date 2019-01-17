package com.jack.analysis.image.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CanvasView extends View{
    private CanvasAPI canvasAPI;
    private Paint paint;
    private @CanvasAPI.CanvasDrawType int mDrawType;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidget(context);
    }

    private void initWidget(Context context) {
        canvasAPI = CanvasAPI.getInstance(context);
        paint = new Paint();
        mDrawType = CanvasAPI.CanvasDrawType.DRAW_TEXT;
    }

    public void setContentStyle(@CanvasAPI.CanvasDrawType int type) {
        mDrawType = type;
        invalidate();
    }

    @Override
    public void onMeasure(int width, int height) {
        super.onMeasure(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!canvasAPI.drawContent(canvas, paint, mDrawType)) {
            super.onDraw(canvas);
        }
    }
}
