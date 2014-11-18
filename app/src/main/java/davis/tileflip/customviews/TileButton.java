package davis.tileflip.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import davis.tileflip.R;
import davis.tileflip.tile.TileStates;

public class TileButton extends Button implements View.OnClickListener {
    private TileStates.FlipDirection flipDirection;
    private Drawable reverse;
    private FlipAnimator mAnimation;
    private int duration;
    private OnClickListener clickListener;
    
    public TileButton(Context context) {
        super(context);
        init(null);
    }

    public TileButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TileButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TileButton, 0, 0);
        reverse = a.getDrawable(R.styleable.TileButton_backgroundDrawable);
        if(reverse == null)
            reverse = getResources().getDrawable(R.drawable.red_button);
        flipDirection = TileStates.FlipDirection.DOWN;
        duration = a.getInt(R.styleable.TileButton_duration, 300);
        mAnimation = new FlipAnimator();
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimation.setDuration(duration);
        setOnClickListener(this);
    }

    public void performFlip() {
        mAnimation.setToDrawable(reverse);
        startAnimation(mAnimation);
    }

    public void setClick(OnClickListener listener) {
        clickListener = listener;
    }

    @Override
    public void onClick(final View v) {
        if(clickListener != null) {
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clickListener.onClick(v);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        performFlip();
    }

    public class FlipAnimator extends Animation {

        private Camera camera;
        private Drawable toDrawable;
        private float centerX;
        private float centerY;
        private boolean visibilitySwapped;
        private boolean mIsRotationReversed;
        private boolean mIsRotationXEnabled = false;
        private boolean mIsRotationYEnabled = false;
        private boolean mIsRotationZEnabled = false;

        public void setToDrawable(Drawable to) {
            toDrawable = to;
            visibilitySwapped = false;
        }

        public FlipAnimator() {
            setFillAfter(true);
        }


        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            camera = new Camera();
            this.centerX = width / 2;
            this.centerY = height / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            final double radians = Math.PI * interpolatedTime;
            float degrees = (float) (180.0 * radians / Math.PI);

            if(mIsRotationReversed){
                degrees = -degrees;
            }

            if (interpolatedTime >= 0.5f) {
                if(mIsRotationReversed){ degrees += 180.f; } else{ degrees -= 180.f; }

                if (!visibilitySwapped) {
                    setBackground(toDrawable);
                    visibilitySwapped = true;
                }
            }

            switch(flipDirection) {
            case UP :
                mIsRotationXEnabled = true;
                mIsRotationYEnabled = false;
                mIsRotationZEnabled = false;
                mIsRotationReversed = false;
                break;
            case DOWN :
                mIsRotationXEnabled = true;
                mIsRotationYEnabled = false;
                mIsRotationZEnabled = false;
                mIsRotationReversed = true;
                break;
            case LEFT :
                mIsRotationXEnabled = false;
                mIsRotationYEnabled = true;
                mIsRotationZEnabled = false;
                mIsRotationReversed = true;
                break;
            case RIGHT :
                mIsRotationXEnabled = false;
                mIsRotationYEnabled = true;
                mIsRotationZEnabled = false;
                mIsRotationReversed = false;
                break;
            case UNKNOWN :
                mIsRotationXEnabled = false;
                mIsRotationYEnabled = false;
                mIsRotationZEnabled = false;
                mIsRotationReversed = false;
                break;
            }

            final Matrix matrix = t.getMatrix();

            camera.save();
            camera.translate(0.0f, 0.0f, (float) (150.0 * Math.sin(radians)));
            camera.rotateX(mIsRotationXEnabled ? degrees : 0);
            camera.rotateY(mIsRotationYEnabled ? degrees : 0);
            camera.rotateZ(mIsRotationZEnabled ? degrees : 0);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
