package andy.zhu.aliinterview.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import andy.zhu.aliinterview.R;
import andy.zhu.aliinterview.Utils;

/**
 * Created by Andy.Zhu on 2017/8/26.
 */

public class InputPage extends RelativeLayout {

    private TextView mTitle;
    private TextView mDesc;
    private TextView mHint;
    private FlyInTextView mFlyInTextView;

    public InputPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int paddingLR = (int) Utils.dp2pix(context, 30);
        setPadding(paddingLR, 0, paddingLR, 0);

        LayoutInflater.from(context).inflate(R.layout.layout_input_page, this);
        mTitle = (TextView) findViewById(R.id.title);
        mDesc = (TextView) findViewById(R.id.desc);
        mHint = (TextView) findViewById(R.id.hint);
        mFlyInTextView = (FlyInTextView) findViewById(R.id.fly_in_text);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputPage);
        mTitle.setText(a.getString(R.styleable.InputPage_title));
        mDesc.setText(a.getString(R.styleable.InputPage_desc));
        mHint.setText(a.getString(R.styleable.InputPage_hint));
        mFlyInTextView.setText(a.getString(R.styleable.InputPage_input_text));
        a.recycle();
    }

    public void startFlyAnimation(long delay) {
        Animator hintDisappearAnim = ObjectAnimator
                .ofFloat(mHint, "alpha", 1, 0)
                .setDuration(300);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(hintDisappearAnim, mFlyInTextView.buildAnimator());
        animatorSet.setStartDelay(delay);
        animatorSet.start();
    }
}
