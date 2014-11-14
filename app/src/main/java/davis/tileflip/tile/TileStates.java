package davis.tileflip.tile;

import android.graphics.drawable.Drawable;
import android.view.View;
import davis.tileflip.R;

public class TileStates {

    public enum FlipDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UNKNOWN
    }

    public enum TileColor {
        RED,
        BLUE,
        GREEN,
        PURPLE,
        ORANGE;

        public TileColor getNext() {
            return this.ordinal() < TileColor.values().length - 1 ? TileColor.values()[this.ordinal() + 1] : RED;
        }

        public Drawable getDrawable(View v) {
            switch(this) {
            case RED :
                return v.getResources().getDrawable(R.drawable.red_tile);
            case BLUE :
                return v.getResources().getDrawable(R.drawable.blue_tile);
            case GREEN :
                return v.getResources().getDrawable(R.drawable.green_tile);
            case PURPLE :
                return v.getResources().getDrawable(R.drawable.purple_tile);
            case ORANGE :
                return v.getResources().getDrawable(R.drawable.orange_tile);
            default :
                return v.getResources().getDrawable(R.drawable.grey_tile);
            }
        }
    }

    public static FlipDirection[] validDirections = { FlipDirection.DOWN, FlipDirection.UP, FlipDirection.LEFT, FlipDirection.RIGHT };
}
