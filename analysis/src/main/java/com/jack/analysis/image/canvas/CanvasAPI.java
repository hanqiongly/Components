package com.jack.analysis.image.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.jack.util.DisplayUtils;

public class CanvasAPI {
    private static CanvasAPI mINSTANCE ;
    private Paint clearPaint;
    private int mStrokeWidth;
    private int mDefaultTextHeight = 0;

    public static CanvasAPI getInstance(Context context) {
        if (mINSTANCE == null) {
            mINSTANCE = new CanvasAPI(context);
        }
        return mINSTANCE;
    }

    private CanvasAPI(Context context){
        clearPaint = new Paint();
        mStrokeWidth = DisplayUtils.dp2px(context, 6);
        mDefaultTextHeight = 35;
    }

    boolean drawContent(Canvas canvas, Paint paint, @CanvasDrawType int drawContentType) {
        paint.reset();
        boolean result = true;
        switch(drawContentType) {
            case CanvasDrawType.DRAW_AXIS:
                drawAxis(canvas, paint);
                break;
            case CanvasDrawType.DRAW_COLORS:
                drawColors(canvas);
                break;
            case CanvasDrawType.DRAW_TEXT:
                drawText(canvas, paint);
                break;
            case CanvasDrawType.DRAW_POINT:
                drawPoint(canvas, paint);
                break;
            case CanvasDrawType.DRAW_LINE:
                drawLine(canvas, paint);
                break;
            case CanvasDrawType.DRAW_RECT:
                break;
            case CanvasDrawType.DRAW_CIRCLE:
                break;
            case CanvasDrawType.DRAW_OVAL:
                break;
            case CanvasDrawType.DRAW_ARC:
                break;
            case CanvasDrawType.DRAW_PATH:
                break;
            case CanvasDrawType.DRAW_BITMAP:
                break;
            default:
                result = false;
                break;
        }

        return result;
    }

    /**绘制坐标系事例：
     * 第一次绘制绘图坐标系时，绘图坐标系默认情况下和Canvas坐标系重合，所以绘制出的坐标系紧贴View的上侧和左侧；
     * 第二次首先将坐标轴向右下角平移了一段距离，然后绘制出的坐标系也就整体向右下角平移了；
     * 第三次再次向右下角平移，并旋转了30度，图上倾斜的坐标系即最后的绘图坐标系
     */
    private void drawAxis(Canvas canvas, Paint paint) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        /*设置为只是绘制边框，线条*/
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setStrokeJoin(Paint.Join.BEVEL);

        /**绘制最初始的坐标轴*/
        paint.setColor(0xff00ff00);
        canvas.drawLine(0,0, canvasWidth, 0, paint);
        canvas.drawLine(0,0,0, canvasHeight, paint);

        /**坐标系向右下角平移四分之一*/
         paint.setColor(0xff0000ff);
         canvas.translate(canvasWidth / 4, canvasHeight / 4);
         canvas.drawLine(0, 0, canvasWidth, 0, paint);
         canvas.drawLine(0, 0, 0, canvasHeight, paint);

         /**坐标系继续平移四分之一，并且基于最后一次的原点旋转30度*/
        canvas.translate(canvasWidth / 4, canvasHeight / 4);
        canvas.rotate(30);
        paint.setColor(0xffff0000);
        canvas.drawLine(0, 0, canvasWidth, 0, paint);
        canvas.drawLine(0, 0, 0, canvasHeight, paint);
    }

    /**
     * Canvas中的drawARGB可以用来对整个Canvas以某种统一的颜色整体绘制，四个参数分别是Alpha、Red、Green、Blue，取值都是0-255。
     * */
    private void drawColors(Canvas canvas) {
        canvas.drawARGB(255, 68,56,240);
    }

    // TODO: 2018/10/16 分析各种坐标所采用的度量单位
    /**
     * Android中的画笔有两种Paint和TextPaint，我们可以Paint来画其他的图形：点、线、矩形、椭圆等。TextPaint继承自Paint，是专门用来画文本的，由于TextPaint继承自Paint，
     * 所以也可以用TextPaint画点、线、面、矩形、椭圆等图形。我们在上面的代码中将canvas.translate()和canvas.rotate()放到了canvas.save()和canvas.restore()之间，
     * 这样做的好处是，在canvas.save()调用时，将当前坐标系保存下来，将当前坐标系的矩阵Matrix入栈保存，然后通过translate或rotate等对坐标系进行变换，然后进行绘图，绘图完
     * 成后，我们通过调用canvas.restore()将之前保存的Matrix出栈，这样就将当前绘图坐标系恢复到了canvas.save()执行的时候状态。如果熟悉OpenGL开发，对这种模式应该很了解。
     * 通过调用paint.setColor(0xff00ff00)将画笔设置为绿色，paint的setColor方法需要传入一个int值，通常情况下我们写成16进制0x的形式，第一个字节存储Alpha通道，第二个字节
     * 存储Red通道，第三个字节存储Green通道，第四个字节存储Blue通道，每个字节的取值都是从00到ff。如果对这种设置颜色的方式不熟悉，也可以调用
     * paint.setARGB(int a, int r, int g, int b)方法设置画笔的颜色，不过paint.setColor(int color)的方式更简洁。
     * 通过调用paint.setTextAlign()设置文本的对齐方式，该对齐方式是相对于绘制文本时的画笔的坐标来说的，在本例中，我们绘制文本时画笔在Canvas宽度的中间。在drawText()方法执
     * 行时，需要传入一个x和y坐标，假设该点为P点，P点表示我们从P点绘制文本。当对齐方式为Paint.Align.LEFT时，绘制的文本以P点为基准向左对齐，这是默认的对齐方式；当对齐方式为
     * Paint.Align.CENTER时，绘制的文本以P点为基准居中对齐；当对齐方式为Paint.Align.RIGHT时，绘制的文本以P点为基准向右对齐。
     * 通过调用paint.setUnderlineText(true)绘制带有下划线的文本。
     * 通过调用paint.setFakeBoldText(true)绘制粗体文本。
     * 通过rotate旋转坐标系，我们可以绘制倾斜文本。
     * */
    private void drawText(Canvas canvas, Paint paint) {
        int textHeight = mDefaultTextHeight;
        int canvasWidth = canvas.getWidth();
        int halfCanvasWidth = canvasWidth / 2;
        float translateY = textHeight;

        //绘制正常文本
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("正常绘制文本", 0, 0, paint);
        canvas.restore();
        translateY += textHeight * 2;

        //绘制绿色文本
        paint.setColor(0xff00ff00);//设置字体为绿色
        canvas.save();
        canvas.translate(0, translateY);//将画笔向下移动
        canvas.drawText("绘制绿色文本", 0, 0, paint);
        canvas.restore();
        paint.setColor(0xff000000);//重新设置为黑色
        translateY += textHeight * 2;

        //设置左对齐
        paint.setTextAlign(Paint.Align.LEFT);//设置左对齐
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("左对齐文本", 0, 0, paint);
        canvas.restore();
        translateY += textHeight * 2;

        //设置居中对齐
        paint.setTextAlign(Paint.Align.CENTER);//设置居中对齐
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("as居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本居中对齐文本as", 0, 0, paint);
        canvas.restore();
        translateY += textHeight * 2;

        //设置右对齐
        paint.setTextAlign(Paint.Align.RIGHT);//设置右对齐
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("右对齐文本", 0, 0, paint);
        canvas.restore();
        paint.setTextAlign(Paint.Align.LEFT);//重新设置为左对齐
        translateY += textHeight * 2;

        //设置下划线
        paint.setUnderlineText(true);//设置具有下划线
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("下划线文本", 0, 0, paint);
        canvas.restore();
        paint.setUnderlineText(false);//重新设置为没有下划线
        translateY += textHeight * 2;

        //绘制加粗文字
        paint.setFakeBoldText(true);//将画笔设置为粗体
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("粗体文本", 0, 0, paint);
        canvas.restore();
        paint.setFakeBoldText(false);//重新将画笔设置为非粗体状态
        translateY += textHeight * 2;

        //文本绕绘制起点顺时针旋转
        canvas.save();
        canvas.translate(0, translateY);
        canvas.rotate(20);
        canvas.drawText("文本绕绘制起点旋转20度", 0, 0, paint);
        canvas.restore();
    }

    /**绘制点
     * Paint的setStrokeWidth方法可以控制所画线的宽度，通过Paint的getStrokeWidth方法可以得到所画线的宽度，默认情况下，
     * 线宽是0。其实strokeWidth不仅对画线有影响，对画点也有影响，由于默认的线宽是0，所以默认情况下调用drawPoint方法无法在Canvas
     * 上画出点，为了让大家清楚地看到所画的点，我用Paint的setStrokeWidth设置了一个比较大的线宽，这样我们看到的点也就比较大。
     * Paint有个setStrokeCap方法可以设置所画线段的时候两个端点的形状，即所画线段的帽端的形状，在下面讲到drawLine方法时会详细说明，
     * 其实setStrokeCap方法也会影响所画点的形状。Paint的setStrokeCap方法可以有三个取值：Paint.Cap.BUTT、Paint.Cap.ROUND和Paint.Cap.SQUARE。
     * 默认情况下Paint的getStrokeCap的返回值是Paint.Cap.BUTT，默认画出来的点就是一个正方形，上图第一个点即是用BUTT作为帽端画的。
     * 我们可以调用setStrokeCap方法设置Paint的strokeCap为Paint.Cap.ROUND时，画笔画出来的点就是一个圆形，上图第二个点即是用ROUND作为帽端画的。
     * 调用调用setStrokeCap方法设置Paint的strokeCap为Paint.Cap.SQUARE时，画笔画出来的电也是一个正方形，与用BUTT画出来的效果在外观上相同，上图最
     * 后一个点即时用SQUARE作为帽端画的。
     */
    private void drawPoint(Canvas canvas, Paint paint) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int x = canvasWidth / 2;
        int deltaY = canvasHeight / 3;
        int y = deltaY / 2;
        paint.setColor(0xff8bc5ba);//设置颜色
        paint.setStrokeWidth(mStrokeWidth * 5);//设置线宽，如果不设置线宽，无法绘制点

        //绘制Cap为BUTT的点
        paint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawPoint(x, y, paint);

        //绘制Cap为ROUND的点
        canvas.translate(0, deltaY);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(x, y, paint);

        //绘制Cap为SQUARE的点
        canvas.translate(0, deltaY);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPoint(x, y, paint);
    }

    /**Canvas绘制线条：
     * drawLine方法接收四个数值，即起点的x和y以及终点的x和y，绘制一条线段。
     * drawLines方法接收一个float数组pts，需要注意的是在用drawLines绘图时，其每次从pts数组中取出四个点绘制一条线段，然后再取出后面四个点绘制一条线段，
     * 所以要求pts的长度需要是4的倍数。假设我们有四个点，分别是p1、p2、p3、p4，我们依次将其坐标放到pts数组中，即pts =
     * {p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y}，那么用drawLines绘制pts时，你会发现p1和p2之间画了一条线段，p3和p4之间画了一条线段，但是p2和p3之间没有画线段，
     * 这样大家就应该能明白drawLines每次都需要从pts数组中取出4个值绘制一条线段的意思了。
     * 通过调用Paint的setStrokeWidth方法设置线的宽度。
     * 上面在讲drawPoint时提到了strokeCap对所绘制点的形状的影响，通过drawLine绘制的线段也受其影响，体现在绘制的线段的两个端点的形状上。
     * Paint.Cap.BUTT
     * 当用BUTT作为帽端时，所绘制的线段恰好在起点终点位置处戛然而止，两端是方形，上图中第一条加粗的线段就是用BUTT作为帽端绘制的。
     * Paint.Cap.ROUND
     * 当用ROUND作为帽端时，所绘制的线段的两端端点会超出起点和终点一点距离，并且两端是圆形状，上图中第二条加粗的线段就是用ROUND作为帽端绘制的。
     * Paint.Cap.SQUARE
     * 当用SQUARE作为帽端时，所绘制的线段的两端端点也会超出起点和终点一点距离，两端点的形状是方形，上图中最后一条加粗的线段就是用SQUARE作为帽端绘制的。
     * */
    private void drawLine(Canvas canvas, Paint paint) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int x = canvasWidth / 2;
        int deltaY = canvasHeight / 3;
        int y = deltaY / 2;

        paint.setColor(0xff8bc5ba);//设置颜色
        paint.setStrokeWidth(mStrokeWidth);//设置线宽，如果不设置线宽，无法绘制点

        paint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawLine(0, 0, x, y, paint);

        canvas.translate(15, y);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(0, 0, x, y, paint);

        canvas.translate(15, y);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawLine(0, 0, x, y, paint);

        canvas.translate(10, y);
        paint.setStrokeWidth(1);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
        canvas.drawLine(5, 0, 5, y, paint);
        canvas.drawLine(5, y, x, 2 * y, paint);

        float[] pointPairs = { 0, 0, x, y, x/2, y/3, x, y};
        canvas.drawLines(pointPairs, paint);
    }

    private void drawRect(Canvas canvas, Paint paint) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int x = canvasWidth / 2;
        int deltaY = canvasHeight / 3;
        int y = deltaY / 2;
    }

    private void clearCanvas(Canvas canvas) {
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(clearPaint);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    public @interface CanvasDrawType {
        int DRAW_AXIS = 0;
        int DRAW_COLORS = 1;
        int DRAW_TEXT = 2;
        int DRAW_POINT = 3;
        int DRAW_LINE = 4;
        int DRAW_RECT = 5;
        int DRAW_CIRCLE = 6;
        int DRAW_OVAL = 7;
        int DRAW_ARC = 8;
        int DRAW_PATH = 9;
        int DRAW_BITMAP = 10;
    }
}
