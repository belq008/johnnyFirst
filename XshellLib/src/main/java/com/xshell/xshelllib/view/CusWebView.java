package com.xshell.xshelllib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.xshell.xshelllib.utils.DensityUtil;

/**
 * Created by zzy on 2016/11/29.
 */

public class CusWebView extends WebView {


    private Paint paint1;
    private Paint paint2;
    private float m_radius;
    private int width = 0;
    private int height = 0;
    private int x;
    private int y;

    private int defaultSize;

    public CusWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        defaultSize = DensityUtil.dip2px(context, 300);
    }

    public CusWebView(Context context) {
        super(context);
// TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context){
        paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setAntiAlias(true);
        paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    public void setRadius(float radius){
        m_radius = radius;
//        width = w;
//        height = h;
    }
    public void setRadius(float radius,int w, int h){
        m_radius = radius;
        width = w;
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        width = widthSize;
        height = heightSize;


       setMeasuredDimension(widthMeasureSpec, measureHanlder(heightMeasureSpec));


    }


    private int measureHanlder(int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
//            Log.i("zzy","--------EXACTLY-------:"+result);
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
//            Log.i("zzy","--------AT_MOST-------:"+result);
//            Log.i("zzy","--------AT_MOST specSize-------:"+specSize);

        } else {
            result = defaultSize;
//            Log.i("zzy","--------other-------:"+result);
        }
        height = result;
        return result;
    }


    @Override
    public void draw(Canvas canvas) {
        x = this.getScrollX();
        y = this.getScrollY();
        Bitmap bitmap = Bitmap.createBitmap(x + width, y + height, Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLeftUp(canvas2);
        drawRightUp(canvas2);
        //drawLeftDown(canvas2);
        //drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x, m_radius);
        path.lineTo(x, y);
        path.lineTo(m_radius, y);
        path.arcTo(new RectF(x, y, x + m_radius * 2, y + m_radius * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint1);
    }


    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x, y + height - m_radius);
        path.lineTo(x, y + height);
        path.lineTo(x + m_radius, y + height);
        path.arcTo(new RectF(x, y + height - m_radius * 2,
                x + m_radius * 2, y + height), 90, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }


    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x + width - m_radius, y + height);
        path.lineTo(x + width, y + height);
        path.lineTo(x + width, y + height - m_radius);
        path.arcTo(new RectF(x + width - m_radius * 2, y + height
                - m_radius * 2, x + width, y + height), 0, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }


    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x + width, y + m_radius);
        path.lineTo(x + width, y);
        path.lineTo(x + width - m_radius, y);
        path.arcTo(new RectF(x + width - m_radius * 2, y, x + width,
                y + m_radius * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }
}