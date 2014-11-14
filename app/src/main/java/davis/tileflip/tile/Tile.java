package davis.tileflip.tile;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;
import davis.tileflip.R;

public class Tile extends ImageView implements View.OnClickListener, View.OnLongClickListener,
        Animation.AnimationListener {

    private Drawable back;
    private Drawable front;
    private int maxColors = 2;

    private static final Interpolator fDefaultInterpolator = new OvershootInterpolator();
    private static int sDefaultDuration;
    private static boolean sDefaultAnimated;
    private static boolean sDefaultFlipped;
    private static boolean sDefaultIsRotationReversed;
    private TileStates.FlipDirection flipDirection;
    private TileStates.TileColor tileColor;

    public interface OnFlipListener {

        public void onClick(Tile view);

        public boolean onLongClick(Tile view);

        public void onFlipStart(Tile view);

        public void onFlipEnd(Tile view);
    }

    private FlipAnimator mAnimation;
    private boolean mIsRotationXEnabled;
    private boolean mIsRotationYEnabled;
    private boolean mIsRotationZEnabled;
    private boolean mIsFlipping;
    private boolean mIsRotationReversed;
    private OnFlipListener onFlipListener;
    private OnLongClickListener onLongClickListener;
    private boolean mIsFlipped;
    private boolean mIsDefaultAnimated;


    public Tile(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Tile(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Tile(Context context, AttributeSet attrs, int i) {
        super(context, attrs);
        init(context, attrs, i);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        sDefaultDuration = 1000;
        sDefaultAnimated = true;
        sDefaultFlipped = false;
        sDefaultIsRotationReversed = false;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Tile, defStyle, 0);
        mIsDefaultAnimated = sDefaultAnimated;
        mIsFlipped = sDefaultFlipped;
        back = a.getDrawable(R.styleable.Tile_backSide);
        int duration = a.getInt(R.styleable.Tile_flipDuration, sDefaultDuration);
        int interpolatorResId = a.getResourceId(R.styleable.Tile_flipInterpolator, 0);
        Interpolator interpolator = interpolatorResId > 0 ? AnimationUtils
                .loadInterpolator(context, interpolatorResId) : fDefaultInterpolator;

        flipDirection = TileStates.FlipDirection.UNKNOWN;

        front = getDrawable();
        mIsRotationReversed = sDefaultIsRotationReversed;

        mAnimation = new FlipAnimator();
        mAnimation.setAnimationListener(this);
        mAnimation.setInterpolator(interpolator);
        mAnimation.setDuration(duration);

        setOnClickListener(this);
        setOnLongClickListener(this);

        setImageDrawable(mIsFlipped ? back : front);
        setMaxColors(1);
        mIsFlipping = false;
        tileColor = TileStates.TileColor.RED;

        a.recycle();
    }

    public void setMaxColors(int i) {
        maxColors = i;
    }

    public void setFlipDirection(TileStates.FlipDirection dir) {
        flipDirection = dir;
    }

    public void setFlippedDrawable(Drawable flippedDrawable){
        back = flippedDrawable;
    }

    public void setDrawable(Drawable drawable){
        front = drawable;
        if(!mIsFlipped) setImageDrawable(front);
    }

    public TileStates.TileColor getTileColor() {
        return tileColor;
    }

    public boolean isRotationXEnabled() {
        return mIsRotationXEnabled;
    }

    public void setRotationXEnabled(boolean enabled) {
        mIsRotationXEnabled = enabled;
    }

    public boolean isRotationYEnabled() {
        return mIsRotationYEnabled;
    }

    public void setRotationYEnabled(boolean enabled) {
        mIsRotationYEnabled = enabled;
    }

    public boolean isRotationZEnabled() {
        return mIsRotationZEnabled;
    }

    public void setRotationZEnabled(boolean enabled) {
        mIsRotationZEnabled = enabled;
    }

    public FlipAnimator getFlipAnimation() {
        return mAnimation;
    }

    public void setAnimator() {
        mAnimation = new FlipAnimator();
    }

    public void setInterpolator(Interpolator interpolator) {
        mAnimation.setInterpolator(interpolator);
    }

    public void setDuration(int duration) {
        mAnimation.setDuration(duration);
    }

    public boolean isFlipped() {
        return mIsFlipped;
    }

    public boolean isFlipping() {
        return mIsFlipping;
    }

    public boolean isRotationReversed(){
        return mIsRotationReversed;
    }

    public void setRotationReversed(boolean rotationReversed){
        mIsRotationReversed = rotationReversed;
    }

    public boolean isAnimated() {
        return mIsDefaultAnimated;
    }

    public void setAnimated(boolean animated) {
        mIsDefaultAnimated = animated;
    }

    public void setFlipped(boolean flipped) {
        setFlipped(flipped, mIsDefaultAnimated);
    }

    public void setFlipped(boolean flipped, boolean animated) {
        if (flipped != mIsFlipped) {
            toggleFlip(animated);
        }
    }

    public void flipTile() {
        toggleFlip(mIsDefaultAnimated);
    }

    public void toggleFlip(boolean animated) {
        updateDrawables();
        if (animated) {
            mAnimation.setToDrawable(back);
            startAnimation(mAnimation);
        } else {
            setImageDrawable(mIsFlipped ? front : back);
        }
        mIsFlipped = !mIsFlipped;
    }

    public void updateDrawables() {
        tileColor = tileColor.getNext();
        switch(maxColors) {
        case 5 :
            back = tileColor.getDrawable(this);
            break;
        case 4 :
            if(tileColor == TileStates.TileColor.ORANGE) {
                tileColor = TileStates.TileColor.RED;
            }
            back = tileColor.getDrawable(this);
            break;
        case 3 :
            if(tileColor == TileStates.TileColor.PURPLE) {
                tileColor = TileStates.TileColor.RED;
            }
            back = tileColor.getDrawable(this);
            break;
        case 2 :
            if(tileColor == TileStates.TileColor.GREEN) {
                tileColor = TileStates.TileColor.RED;
            }
            back = tileColor.getDrawable(this);
            break;
        default :
            if(tileColor == TileStates.TileColor.BLUE) {
                tileColor = TileStates.TileColor.RED;
            }
            back = tileColor.getDrawable(this);
            break;
        }
    }

    public void setOnFlipListener(OnFlipListener listener) {
        onFlipListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (onFlipListener != null) {
            onFlipListener.onClick(this);
        }
        else
            flipTile();
    }

    @Override
    public boolean onLongClick(View v) {
        if(onFlipListener != null) {
            onFlipListener.onLongClick(this);
        }
        else {
            TileStates.FlipDirection temp = flipDirection;
            setFlipDirection(TileStates.FlipDirection.UNKNOWN);
            flipTile();
        }
        return true;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (onFlipListener != null) {
            onFlipListener.onFlipStart(this);
        }
        mIsFlipping = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (onFlipListener != null) {
            onFlipListener.onFlipEnd(this);
        }
        mIsFlipping = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    public class FlipAnimator extends Animation {

        private Camera camera;
        private Drawable toDrawable;
        private float centerX;
        private float centerY;
        private boolean visibilitySwapped;

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
                    setImageDrawable(toDrawable);
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
