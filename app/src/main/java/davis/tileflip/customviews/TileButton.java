package davis.tileflip.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import davis.tileflip.R;
import davis.tileflip.animation.FlipAnimation;
import davis.tileflip.tile.TileStates;

public class TileButton extends Button implements View.OnClickListener {
    private TileStates.FlipDirection flipDirection;
    private TileStates.TileColor color;
    private Drawable reverse;
    private TileButtonFlip mAnimation;
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
        color = TileStates.TileColor.BLUE;
        if(reverse == null)
            reverse = getResources().getDrawable(R.drawable.red_button);
        flipDirection = TileStates.FlipDirection.DOWN;
        duration = a.getInt(R.styleable.TileButton_duration, 300);
        mAnimation = new TileButtonFlip(TileStates.FlipDirection.DOWN);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimation.setDuration(duration);
        setOnClickListener(this);
    }

    public void performFlip() {
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

    public class TileButtonFlip extends FlipAnimation {

        public TileButtonFlip(TileStates.FlipDirection direction) {
            super(direction);
        }

        @Override
        public void onHalfWay() {
            mAnimation = new TileButtonFlip(TileStates.FlipDirection.DOWN);
            mAnimation.setInterpolator(new DecelerateInterpolator());
            mAnimation.setDuration(duration);
            Drawable back = reverse;
            reverse = getBackground();
            setBackground(back);
        }
    }
}
