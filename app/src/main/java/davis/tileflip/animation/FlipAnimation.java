package davis.tileflip.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import davis.tileflip.tile.TileStates;

public abstract class FlipAnimation extends Animation {

    private Camera camera;
    private float centerX;
    private float centerY;
    private TileStates.FlipDirection dir;
    private boolean visibilitySwapped = false;
    private boolean mIsRotationReversed;
    private boolean mIsRotationXEnabled = false;
    private boolean mIsRotationYEnabled = false;
    private boolean mIsRotationZEnabled = false;

    public abstract void onHalfWay();

    public FlipAnimation(TileStates.FlipDirection direction) {
        dir = direction;
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
                onHalfWay();
                visibilitySwapped = true;
            }
        }

        switch(dir) {
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

