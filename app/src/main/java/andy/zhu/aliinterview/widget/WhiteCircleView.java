package andy.zhu.aliinterview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Andy.Zhu on 2017/8/26.
 */

public class WhiteCircleView extends View {

    private float mProgress = 0;

    private Paint mPaint;

    public WhiteCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhiteCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Keep
    public void setProgress(float progress) {
        mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress > 0) {
            float width = canvas.getWidth();
            float height = canvas.getHeight();
            float maxRadius = (float) (Math.sqrt(width * width + height * height) / 2);
            float radius = maxRadius * mProgress;
            canvas.drawCircle(width / 2, height / 2, radius, mPaint);
        }
    }
}
