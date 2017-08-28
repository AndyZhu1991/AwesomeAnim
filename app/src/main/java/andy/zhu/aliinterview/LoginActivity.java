package andy.zhu.aliinterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

import andy.zhu.aliinterview.widget.FlipChildFrameLayout;
import andy.zhu.aliinterview.widget.InputPage;
import andy.zhu.aliinterview.widget.WhiteCircleView;

public class LoginActivity extends AppCompatActivity {

    private static final long UP_FLOAT_DURATION = 500;
    private static final long CONTENT_TRANSLATE_DURATION = 800;

    private FlipChildFrameLayout mFlipChildLayout;

    private InputPage mWelcomeInputPage;
    private InputPage mEmailInputPage;
    private InputPage mPasswordInputPage;

    private WhiteCircleView mWhiteCircleView;

    private View mContent;
    private View mGridLayout;
    private View mListLayout;
    private View mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFlipChildLayout = (FlipChildFrameLayout) findViewById(R.id.flip_child_layout);
        mWhiteCircleView = (WhiteCircleView) findViewById(R.id.white_circle_view);

        mWelcomeInputPage = (InputPage) findViewById(R.id.welcome);
        mWelcomeInputPage.findViewById(R.id.next).setOnClickListener(mWelcomeNextListener);
        mEmailInputPage = (InputPage) findViewById(R.id.email);
        mEmailInputPage.findViewById(R.id.next).setOnClickListener(mEmailNextListener);
        mEmailInputPage.findViewById(R.id.next).setEnabled(false);
        mPasswordInputPage = (InputPage) findViewById(R.id.password);
        mPasswordInputPage.findViewById(R.id.next).setOnClickListener(mPasswordNextListener);
        mPasswordInputPage.findViewById(R.id.next).setEnabled(false);

        mContent = findViewById(R.id.content);
        mGridLayout = findViewById(R.id.grids);
        mListLayout = findViewById(R.id.list);
        mBottomBar = findViewById(R.id.bottom_bar);
        mContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mContent.setTranslationY(mContent.getHeight());
                float childTransY = mContent.getHeight() - mGridLayout.getTop();
                mGridLayout.setTranslationY(childTransY);
                mListLayout.setTranslationY(childTransY);
                mBottomBar.setTranslationY(childTransY);
                mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mWelcomeInputPage.startFlyAnimation(1234);
    }

    private View.OnClickListener mWelcomeNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFlipChildLayout.flipChildOut(mWelcomeInputPage);
            Animator emailAnim = buildUpFloatAnimator(mEmailInputPage, 1, 1, 0);
            Animator passwordAnim = buildUpFloatAnimator(mPasswordInputPage, 0.9f, 0.8f,
                    Utils.dp2pix(LoginActivity.this, 35));
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(emailAnim, passwordAnim);
            animatorSet.setStartDelay(500);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmailInputPage.startFlyAnimation(233);
                    mEmailInputPage.findViewById(R.id.next).setEnabled(true);
                }
            });
            animatorSet.start();
        }
    };

    private View.OnClickListener mEmailNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFlipChildLayout.flipChildOut(mEmailInputPage);
            Animator passwordAnim = buildUpFloatAnimator(mPasswordInputPage, 1, 1, 0);
            passwordAnim.setStartDelay(500);
            passwordAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPasswordInputPage.startFlyAnimation(233);
                    mPasswordInputPage.findViewById(R.id.next).setEnabled(true);
                }
            });
            passwordAnim.start();
        }
    };

    private View.OnClickListener mPasswordNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFlipChildLayout.flipChildOut(mPasswordInputPage);

            Animator whiteCircleAnimator = ObjectAnimator
                    .ofFloat(mWhiteCircleView, "progress", 0, 1)
                    .setDuration(800);
            whiteCircleAnimator.setInterpolator(new AccelerateInterpolator(1.3f));
            whiteCircleAnimator.setStartDelay(300);
            whiteCircleAnimator.start();

            Animator contentAnimator = buildContentAnimator();
            contentAnimator.setStartDelay(864);
            contentAnimator.start();
        }
    };

    private Animator buildUpFloatAnimator(View v, float scale, float alpha, float transY) {
        Animator scaleXAnim = ObjectAnimator.ofFloat(v, "scaleX", v.getScaleX(), scale);
        Animator scaleYAnim = ObjectAnimator.ofFloat(v, "scaleY", v.getScaleY(), scale);
        Animator alphaAnim = ObjectAnimator.ofFloat(v, "alpha", v.getAlpha(), alpha);
        Animator transYAnim = ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY(), transY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnim, scaleYAnim, alphaAnim, transYAnim);
        animatorSet.setDuration(UP_FLOAT_DURATION);
        return animatorSet;
    }

    private Animator buildContentAnimator() {
        Animator contentAnimator = ObjectAnimator
                .ofFloat(mContent, "translationY", mContent.getTranslationY(), 0)
                .setDuration(CONTENT_TRANSLATE_DURATION);

        Animator gridAnimator = ObjectAnimator
                .ofFloat(mGridLayout, "translationY", mGridLayout.getTranslationY(), 0)
                .setDuration(CONTENT_TRANSLATE_DURATION);
        gridAnimator.setStartDelay(200);

        Animator listAnimator = ObjectAnimator
                .ofFloat(mListLayout, "translationY", mListLayout.getTranslationY(), 0)
                .setDuration(CONTENT_TRANSLATE_DURATION);
        listAnimator.setStartDelay(350);

        Animator bottomAnimator = ObjectAnimator
                .ofFloat(mBottomBar, "translationY", mBottomBar.getTranslationY(), 0)
                .setDuration(CONTENT_TRANSLATE_DURATION);
        bottomAnimator.setStartDelay(450);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(contentAnimator, gridAnimator, listAnimator, bottomAnimator);
        return animatorSet;
    }
}
