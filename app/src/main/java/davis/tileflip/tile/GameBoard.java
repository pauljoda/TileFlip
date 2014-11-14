package davis.tileflip.tile;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import davis.tileflip.R;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends GridLayout implements View.OnLongClickListener, View.OnClickListener{
    private int colorCount;
    private int size;
    public List<Tile> tiles;

    private boolean canClick = true;

    public interface IGameEventListener {
        public void onFlip();
        public void onCompletion();
        public void checkForCompletion();
    }

    private IGameEventListener gameEvents;

    public GameBoard(Context context) {
        this(context, null, 0);
    }

    public GameBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameBoard(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(isInEditMode()) {
            setBackground(getResources().getDrawable(R.drawable.grey_tile));
            return;
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                init(context, attrs, defStyleAttr);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameBoard, defStyleAttr, 0);
        setBackground(getResources().getDrawable(R.drawable.grey_tile));
        tiles = new ArrayList<Tile>();
        gameEvents = new GameEventListener();

        setPadding(30, 30, 30, 30);
        setRowCount(size);
        setColumnCount(size);
        int i = (getWidth() - getPaddingLeft() - getPaddingRight()) / getRowCount();
        for(int j = 0; j < size * size; j++) {
            Tile tile = new Tile(context);
            tile.setImageDrawable(getResources().getDrawable(R.drawable.red_tile));
            tile.setFlippedDrawable(getResources().getDrawable(R.drawable.blue_tile));
            tile.setScaleType(ImageView.ScaleType.FIT_CENTER);
            tile.setFlipDirection(TileStates.FlipDirection.UNKNOWN);
            tile.setLayoutParams(new android.widget.LinearLayout.LayoutParams(i, i));
            tile.setMaxColors(colorCount);
            tile.setId(j);
            tile.setInterpolator(new DecelerateInterpolator());
            tile.setDuration(300);
            tile.setOnClickListener(this);
            tile.setOnLongClickListener(this);
            addView(tile, j);
            tiles.add(tile);
            Animation pop = AnimationUtils.loadAnimation(context, R.anim.pop);
            pop.setStartOffset((j * 100) + 500);
            pop.setInterpolator(new OvershootInterpolator());
            tile.startAnimation(pop);
        }
    }

    public void setColorCount(int i) {
        colorCount = i;
    }

    public void setSize(int i) {
        size = i;
    }

    public void setLockState(boolean state) {
        canClick = state;
    }

    @Override
    public void onMeasure(int i, int j)
    {
        super.onMeasure(i, i);
        int k = android.view.View.MeasureSpec.getSize(i);
        int l = android.view.View.MeasureSpec.getSize(j);
        int i1;
        if (k > l)
        {
            i1 = l;
        } else
        {
            i1 = k;
        }
        setMeasuredDimension(i1, i1);
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Tile && canClick) {
            int id = v.getId();
            Tile tile = (Tile)v;
            tile.setFlipDirection(TileStates.FlipDirection.UNKNOWN);
            tile.flipTile();
            //Flip above
            if(id - size >= 0) {
                Tile above = tiles.get(id - size);
                above.setFlipDirection(TileStates.FlipDirection.UP);
                above.flipTile();
            }
            //Flip Below
            if(id + size < (size * size)) {
                Tile below = tiles.get(id + size);
                below.setFlipDirection(TileStates.FlipDirection.DOWN);
                below.flipTile();
            }
            //Flip Right
            if((id + 1) % size != 0) {
                Tile right = tiles.get(id + 1);
                right.setFlipDirection(TileStates.FlipDirection.RIGHT);
                right.flipTile();
            }
            //Flip left
            if(id % size != 0) {
                Tile left = tiles.get(id - 1);
                left.setFlipDirection(TileStates.FlipDirection.LEFT);
                left.flipTile();
            }
            gameEvents.onFlip();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    public class GameEventListener implements IGameEventListener {

        @Override
        public void onFlip() {
            checkForCompletion();
        }

        @Override
        public void onCompletion() {
            System.out.println("HELLO");
        }

        @Override
        public void checkForCompletion() {
            TileStates.TileColor check = tiles.get(0).getTileColor();
            for(int i = 0; i < tiles.size(); i++) {
                if(tiles.get(i).getTileColor() != check)
                    return;
            }
            onCompletion();
        }
    }
}
