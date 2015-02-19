package com.socialmap.yy.travelbox.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.socialmap.yy.travelbox.utils.DimensionUtils;
import com.socialmap.yy.travelbox.utils.ImageHelper;

/**
 * 自定义的圆形ImageView
 */
public class MessageAvatar extends ImageView {

    public MessageAvatar(Context context) {
        super(context);
        init();
    }

    public MessageAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageAvatar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private Bitmap buffer;
    private Paint paint;
    private int paintStrokeWidth = DimensionUtils.dp2px(2);

    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(paintStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (buffer == null) {
            Bitmap b = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            buffer = ImageHelper.getCroppedBitmap(b, getWidth() - paintStrokeWidth * 2);
        }
        canvas.drawBitmap(buffer, paintStrokeWidth, paintStrokeWidth, null);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2 - paintStrokeWidth / 2, paint);
    }

}
