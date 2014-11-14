package davis.tileflip.tile;

public class TileStates {

    public enum FlipDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UNKNOWN
    }

    public static FlipDirection[] validDirections = { FlipDirection.DOWN, FlipDirection.UP, FlipDirection.LEFT, FlipDirection.RIGHT };
}
