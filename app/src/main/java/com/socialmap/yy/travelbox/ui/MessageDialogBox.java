package com.socialmap.yy.travelbox.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.socialmap.yy.travelbox.utils.DimensionUtils;

/**
 * 自定义的圆形ImageView
 * Created by yy on 7/24/14.
 */
public class MessageDialogBox extends View {

    public MessageDialogBox(Context context) {
        super(context);
        init();
    }

    public MessageDialogBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageDialogBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private Paint paint;
    private Path path;
    private float paintStrokeWidth = DimensionUtils.dp2px(1);
    private float pointerHeight = DimensionUtils.dp2px(10);
    private float pointerWidth = DimensionUtils.dp2px(15);
    private float pointerMarginTop = DimensionUtils.dp2px(24);


    private void init() {
        paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(paintStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = getWidth()-paintStrokeWidth;
        float h = getHeight()-paintStrokeWidth;
        if (w <= pointerWidth || h < pointerHeight) return;
        if (path == null) {
            path = new Path();
            path.moveTo(0,pointerMarginTop);
            path.lineTo(pointerWidth,pointerMarginTop);
            path.lineTo(pointerWidth,0);
            path.lineTo(w, 0);
            path.lineTo(w, h);
            path.lineTo(pointerWidth, h);
            path.lineTo(pointerWidth, pointerHeight+pointerMarginTop);
            path.close();
        }
        //canvas.save();
        canvas.translate(paintStrokeWidth/2,paintStrokeWidth/2);
        canvas.drawPath(path, paint);
        //canvas.restore();
    }

}
