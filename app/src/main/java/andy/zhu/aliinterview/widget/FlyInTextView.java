package andy.zhu.aliinterview.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import andy.zhu.aliinterview.Utils;

/**
 * Created by Andy.Zhu on 2017/8/24.
 */

public class FlyInTextView extends View {

    private static final long ONE_CHAR_FLY_DURATION = 233;

    private String mText;
    private float mFlyProgress;
    //private int mCurrentFlyIndex;

    private Interpolator mRealInterpolator;

    private Paint mPaint;
    private float mTextSizePix;

    public FlyInTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlyInTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFF333333);
        mTextSizePix = Utils.sp2pix(context, 15);

        mRealInterpolator = new DecelerateInterpolator();

        mPaint.setTextSize(mTextSizePix);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setText(String text) {
        mText = text;
    }

    @Keep
    public void setFlyProgress(float flyProgress) {
        mFlyProgress = flyProgress;
        invalidate();
    }

    private float charFlyInterval() {
        return 1f / (mText.length() / 2);
    }

    public Animator buildAnimator() {
        float maxProgress = 1 + (mText.length() - 1) * charFlyInterval();
        Animator animator = ObjectAnimator
                .ofFloat(this, "flyProgress", 0, maxProgress)
                .setDuration((long) (maxProgress * ONE_CHAR_FLY_DURATION));
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(mText)) {
            return;
        }

        float startX = 0;
        for (int i = 0; i < mText.length(); i++) {
            String charStr = String.valueOf(mText.charAt(i));
            float progress = mFlyProgress - charFlyInterval() * i;
            if (progress < 0) {
                progress = 0;
            }
            if (progress > 1) {
                progress = 1;
            }
            progress = mRealInterpolator.getInterpolation(progress);
            float x = startX + (1 - progress) * (2 * mTextSizePix);
            float y = mTextSizePix + (canvas.getHeight() - mTextSizePix) * (1 - progress);
            mPaint.setAlpha((int) (progress * 255));
            canvas.drawText(charStr, x, y, mPaint);
            startX += mPaint.measureText(charStr);
        }
    }
}
