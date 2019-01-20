package com.jack.animator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jack.animator.animhelper.ValueAnimatorHelper;
import com.jack.util.CollectionUtils;
import com.jack.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyang
 * @date 18/01/2019
 * 仿蜘蛛网效果的控件，蜘蛛网状效果
 * */
public class SpiderEffectView extends View {
    private static final String TAG = SpiderEffectView.class.getSimpleName();
    private final static float MAX_VALUE = 100f;

    private List<SpiderPoint> dataList;

    private String mWeakestCode;

    private Paint webPaint;
    private Paint valuePaint;
    private Paint textPaint;

    /**
     * 背景颜色
     */
    private int bgColor;
    /**
     * 雷达线条颜色
     */
    private int webColor;
    /**
     * 百分比值颜色
     */
    private int valueColor;
    /**
     * 文案颜色
     */
    private int textColor;
    /**
     * 最少得分项特殊的颜色
     */
    private int minScoreTextColor;

    /**
     * 雷达网线条宽度
     */
    private float webLineWidth;
    /**
     * 百分比值圆点半径大小
     */
    private float valuePointRadius;
    /**
     * 字体大小
     */
    private float textSize;
    /**
     * 雷达网每个分割区域扫过的弧度
     */
    private float deltaAngle;
    /**
     * 雷达网半径
     */
    private float radius;
    /**
     * 雷达网层数
     */
    private int webCount;

    private int mWidth;
    private int mHeight;

    private int mMinScoreIndex = -1;

    public SpiderEffectView(Context context) {
        this(context, null);
    }

    public SpiderEffectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
            bgColor = ContextCompat.getColor(getContext(), R.color.color_e6e9ff_16);
            webColor = ContextCompat.getColor(getContext(), R.color.color_9491ff_30);
            valueColor = ContextCompat.getColor(getContext(), R.color.color_8640ff_40);
            textColor = ContextCompat.getColor(getContext(), R.color.color_3c3c3c_100);
            minScoreTextColor = ContextCompat.getColor(getContext(), R.color.reddishPink);

            webLineWidth = DisplayUtils.dp2px(context, 0.5f);
            valuePointRadius = DisplayUtils.dp2px(context, 2.5f);
            textSize = DisplayUtils.dp2px(getContext(), 14.0f);

            webPaint = new Paint();
            webPaint.setAntiAlias(true);
            webPaint.setColor(webColor);
            webPaint.setStyle(Paint.Style.STROKE);

            valuePaint = new Paint();
            valuePaint.setAntiAlias(true);
            valuePaint.setColor(valueColor);
            valuePaint.setStyle(Paint.Style.FILL);

            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(textColor);
    }

    public void onDraw(Canvas canvas) {
        mWidth = getWidth();
        mHeight = getHeight();
        radius = Math.min(mWidth,mHeight) * 0.5f;
        canvas.translate(mWidth/2, mHeight/2);
        if (isPointsAvailable()) {
            drawSpiderFrameLines(canvas);
            drawRegion(canvas);
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas) {
        float textPaintLocationRadius = radius * 0.8f;
        textPaint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        for (int index = 0; index < dataList.size(); index++) {
            if (index == mMinScoreIndex) {
                textPaint.setColor(minScoreTextColor);
            }
            SpiderPoint radarDataModel = dataList.get(index);
            double currentAngel = calculateCurrentAngel(index);
            float x = (float) ((textPaintLocationRadius + fontHeight) * Math.sin(currentAngel));

            float y = (float) ((textPaintLocationRadius + fontHeight * 1.5f) * Math.cos(currentAngel));
            String title = radarDataModel.getTitle();
            float titleWidth = textPaint.measureText(title);
            float titleX = x;
            if (index != 2) {
                if (index < 2) {
                    titleX = x + titleWidth / 2;
                } else {
                    titleX = x - titleWidth / 2;
                }
            }
            canvas.drawText(title, titleX - titleWidth / 2, y, textPaint);

            String scoreLabel = radarDataModel.getDispInfo();
            textPaint.setTextSize(textSize + 4);
            canvas.drawText(scoreLabel, titleX - titleWidth / 4, y + textSize + 4, textPaint);

            //reset
            textPaint.setTextSize(textSize);
            textPaint.setColor(textColor);
        }
    }

    private void drawRegion(Canvas canvas) {
        Context context = getContext();
        Path path = new Path();
        path.reset();
        float minScoreX = 0;
        float minScoreY = 0;
        List<PointModel> pointList = new ArrayList<>();
        float animProgress = progressAnimatorHelper != null ? progressAnimatorHelper.getAnimatorProgress() : 1.0f;
        /*确定好一整块的窗体绘制区域，完成当前窗体整体区域的path连接，以便于区域的背景阴影的绘制*/
        for (int index = 0; index < dataList.size(); index++) {
            SpiderPoint radarDataModel = dataList.get(index);
            double percent = (radarDataModel.getValue() * animProgress) / MAX_VALUE;
            double currentAngel = calculateCurrentAngel(index);
            float x = (float) (radius * Math.sin(currentAngel) * percent);
            float y = (float) (radius * Math.cos(currentAngel) * percent);
            PointModel pointModel = new PointModel(index);
            pointModel.x = x;
            pointModel.y = y;
            pointList.add(pointModel);
            if (index == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            if (index == mMinScoreIndex) {
                minScoreX = x;
                minScoreY = y;
            }
        }

        //绘制填充区域
        int[] colors = new int[]{
                Color.parseColor("#9d4540ff"),
                Color.parseColor("#9a4540ff"),
                Color.parseColor("#98fd6eb8")
        };

        float[] positions = new float[]{
                0, 0.5f, 1.0f
        };
        setLayerType(View.LAYER_TYPE_NONE, null);

        /*绘制蜘蛛网区域的背景*/
        Shader shader = new LinearGradient(0, -mHeight / 2, 0, mHeight / 2, colors, positions, Shader.TileMode.CLAMP);
        valuePaint.setShader(shader);
        canvas.save();
        canvas.clipPath(path);
        valuePaint.setShadowLayer(DisplayUtils.dp2px(context, 10.5f), 0, DisplayUtils.dp2px(context, 1.0f), Color.parseColor("#579a62e7"));
        canvas.drawPaint(valuePaint);

        valuePaint.clearShadowLayer();
        canvas.restore();
        valuePaint.reset();

        /**绘制相邻两个顶点组成的圆弧，扇形*/
        for (int start = 0, count = pointList.size(); start < count; start++) {
            int end = (start + 1) % count;
            if (start == end) {
                //只有一个点
                break;
            }
            PointModel startPoint = pointList.get(start);
            PointModel endPoint = pointList.get(end);
            path.reset();
            path.moveTo(0, 0);
            path.lineTo(startPoint.x, startPoint.y);
            path.lineTo(endPoint.x, endPoint.y);
            path.close();
            valuePaint.setStyle(Paint.Style.FILL);
            valuePaint.setColor(startPoint.color);
            valuePaint.setShadowLayer(DisplayUtils.dp2px(context, 10.5f), 0, DisplayUtils.dp2px(context, 1.0f), startPoint.shadowColor);
            canvas.drawPath(path, valuePaint);
            valuePaint.clearShadowLayer();
        }

        //绘制小圆点
        if (mMinScoreIndex >= 0 && mMinScoreIndex < CollectionUtils.size(dataList)) {
            drawMinScoreCircle(canvas, minScoreX, minScoreY);
        }
    }

    private void drawMinScoreCircle(Canvas canvas, float x, float y) {
        valuePaint.setColor(valueColor);
        valuePaint.setStyle(Paint.Style.STROKE);
        valuePaint.setStrokeWidth(DisplayUtils.dp2px(getContext(), 1f));
        canvas.drawCircle(x, y, valuePointRadius, valuePaint);
        valuePaint.setColor(Color.WHITE);
        valuePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, valuePointRadius - DisplayUtils.dp2px(getContext(), 1f), valuePaint);

        textPaint.setTextSize(textSize - DisplayUtils.dp2px(getContext(), 1f));
        canvas.drawText("弱", x + textSize / 2, y - textSize / 2, textPaint);
    }

    private double calculateCurrentAngel(int i) {
        double currentAngel = INITIAL_ANGEL + i * deltaAngle;
        return currentAngel;
    }

    /**绘制蜘蛛网框架线条*/
    private void drawSpiderFrameLines(Canvas canvas) {
        webPaint.setStrokeWidth(webLineWidth);
        Path webPath = new Path();
        float r = radius / webCount;
        for (int i = 0; i < webCount; i++) {
            float curR = radius - r * i;
            webPath.reset();
            /**计算每个半径上的顶点集合，完成每一圈顶点的计算*/
            for (int j = 0; j < dataList.size(); j++) {
                float x = (float) (curR * Math.sin(deltaAngle / 2 + deltaAngle * j));
                float y = (float) (curR * Math.cos(deltaAngle / 2 + deltaAngle * j));
                if (j == 0) {
                    webPath.moveTo(x, y);
                } else {
                    webPath.lineTo(x, y);
                }
            }
            webPath.close();
            //绘制最外层网前绘制上背景色,整个蜘蛛网范围内填充满背景色
            if (i == 0) {
                valuePaint.setColor(bgColor);
                valuePaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(webPath, valuePaint);
            }
            /**绘制好每一层的蜘蛛网的网线*/
            canvas.drawPath(webPath, webPaint);
        }
    }

    private boolean isPointsAvailable() {
        return CollectionUtils.size(dataList) >= 3;
    }

    ValueAnimatorHelper progressAnimatorHelper;
    private float INITIAL_ANGEL = -1;
    public void setDataList(List<SpiderPoint> dataList, String weakestCode) {
        if (dataList == null || dataList.size() < 3) {
            Log.d(TAG, "the size of data should not be less than 3");
            return;
        }
        this.dataList = dataList;
        this.mWeakestCode = weakestCode;
        if (webCount <= 0) {
            webCount = dataList.size();
        }
        deltaAngle = (float) (Math.PI * 2 / dataList.size());
        INITIAL_ANGEL = deltaAngle / 2;

        findMinScoreIndex();

        progressAnimatorHelper = ValueAnimatorHelper.startAnim(this, 1000);
    }

    /**
     * 找出最小分值索引
     */
    private void findMinScoreIndex() {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (int index = 0; index < dataList.size(); index++) {
            String code = dataList.get(index).getCode();
            if (TextUtils.equals(mWeakestCode, code)) {
                mMinScoreIndex = index;
                break;
            }
        }
    }

    /**蜘蛛网状图每个顶点要显示的信息*/
    public static class SpiderPoint {
        private String title;
        private int value;
        private boolean isDefault;
        private String code;

        public SpiderPoint(){}

        public SpiderPoint(String title,int value, String code) {
            this(title, value, code, false);
        }

        public SpiderPoint(String title,int value, String code, boolean isDefault) {
            this.code = code;
            this.isDefault = isDefault;
            this.value = value;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDispInfo() {
            return isDefault ? "?" : "" + value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    private static class PointModel {
        float x;
        float y;
        int color;
        int shadowColor;

        PointModel(int position) {
            init(position);
        }

        void init(int position) {
            switch (position) {
                case 0:
                    color = Color.parseColor("#7f6d40ff");
                    shadowColor = Color.parseColor("#576462e7");
                    break;
                case 1:
                    color = Color.parseColor("#318640ff");
                    shadowColor = Color.parseColor("#576462e7");
                    break;
                case 2:
                    color = Color.parseColor("#618640ff");
                    shadowColor = Color.parseColor("#576462e7");
                    break;
                case 3:
                    color = Color.parseColor("#00000000");
                    shadowColor = Color.parseColor("#00000000");
                    break;
                case 4:
                    color = Color.parseColor("#418640ff");
                    shadowColor = Color.parseColor("#576462e7");
                    break;
                default:
                    color = Color.parseColor("#00000000");
                    shadowColor = Color.parseColor("#00000000");
            }

        }

    }
}
