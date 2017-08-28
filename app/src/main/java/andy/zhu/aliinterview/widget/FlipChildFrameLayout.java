package andy.zhu.aliinterview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import andy.zhu.aliinterview.Line;

/**
 * Created by Andy.Zhu on 2017/8/25.
 */

public class FlipChildFrameLayout extends FrameLayout {

    private Line mLine;
    private View mFlipView;

    private Path mClipPath;
    private Path mFlipPath;

    private Paint mFlipPaint;

    public FlipChildFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipChildFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mClipPath = new Path();
        mFlipPath = new Path();

        mFlipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFlipPaint.setColor(0xE0FFFFFF);
        mFlipPaint.setStyle(Paint.Style.FILL);
    }

    public void setLine(Line line) {
        mLine = line;
        invalidate();
    }

    private Line mCacheForEval = new Line(0, 0);

    public void flipChildOut(View child) {
        if (child.getParent() != this) {
            return;
        }

        mFlipView = child;
        Line startLine = new Line(1, new PointF(child.getLeft(), child.getBottom()));
        Line endLine = new Line(2, new PointF(getWidth(), child.getTop()));
        Animator animator = ObjectAnimator
                .ofObject(this, new Property<FlipChildFrameLayout, Line>(Line.class, "line") {
                    @Override
                    public Line get(FlipChildFrameLayout object) {
                        return object.mLine;
                    }

                    @Override
                    public void set(FlipChildFrameLayout object, Line value) {
                        object.setLine(value);
                    }
                }, new TypeEvaluator<Line>() {
                    @Override
                    public Line evaluate(float fraction, Line startValue, Line endValue) {
                        mCacheForEval.set(blend(startValue.k, endValue.k, fraction), blend(startValue.b, endValue.b, fraction));
                        return mCacheForEval;
                    }
                }, startLine, endLine);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator(1.5f));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(mFlipView);
                mFlipView = null;
            }
        });
        animator.start();
    }

    private static float blend(float start, float end, float fraction) {
        return start * (1 - fraction) + end * fraction;
    }

    // Avoid new objects in every drawChild
    private PointF mLeftTopPoint = new PointF();
    private PointF mRightTopPoint = new PointF();
    private PointF mRightBottomPoint = new PointF();
    private PointF mLeftBottomPoint = new PointF();

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (child != mFlipView || mLine == null) {
            return super.drawChild(canvas, child, drawingTime);
        }

        PointF pointOnX = mLine.findHLineInterPoint(0);
        PointF pointOnRight = mLine.findVLineInterPoint(canvas.getWidth());
        mClipPath.reset();
        mClipPath.moveTo(pointOnX.x, pointOnX.y);
        mClipPath.lineTo(pointOnRight.x, pointOnRight.y);
        mClipPath.lineTo(pointOnRight.x, 0);
        mClipPath.close();
        canvas.save();
        canvas.clipPath(mClipPath);
        boolean result = super.drawChild(canvas, child, drawingTime);

        mFlipPath.reset();
        mLine.otherSizePoint(child.getLeft(), child.getTop(), mLeftTopPoint);
        mFlipPath.moveTo(mLeftTopPoint.x, mLeftTopPoint.y);
        mLine.otherSizePoint(child.getRight(), child.getTop(), mRightTopPoint);
        mFlipPath.lineTo(mRightTopPoint.x, mRightTopPoint.y);
        mLine.otherSizePoint(child.getRight(), child.getBottom(), mRightBottomPoint);
        mFlipPath.lineTo(mRightBottomPoint.x, mRightBottomPoint.y);
        mLine.otherSizePoint(child.getLeft(), child.getBottom(), mLeftBottomPoint);
        mFlipPath.lineTo(mLeftBottomPoint.x, mLeftBottomPoint.y);
        mFlipPath.close();
        canvas.drawPath(mFlipPath, mFlipPaint);

        canvas.restore();
        return result;

//        Matrix matrix = new Matrix();
//        matrix.postRotate(45, canvas.getWidth() / 2, canvas.getHeight() / 2);
//        matrix.postScale(-1, 1, canvas.getWidth() / 2, canvas.getHeight() / 2);
//        matrix.postRotate(-45, canvas.getWidth() / 2, canvas.getHeight() / 2);
//        float translate = (child.getWidth() + child.getHeight()) / 2f;
//        matrix.postTranslate(-translate + 100, translate - 100);
//        canvas.save();
//        canvas.setMatrix(matrix);
////        canvas.clipRect(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight());
//        boolean result = super.drawChild(canvas, child, drawingTime);
//        canvas.restore();
//        return result || super.drawChild(canvas, child, drawingTime);
    }
}
