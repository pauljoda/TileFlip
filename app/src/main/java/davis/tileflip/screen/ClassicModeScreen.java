package davis.tileflip.screen;

import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import davis.tileflip.GameScreen;
import davis.tileflip.R;
import davis.tileflip.tile.GameBoard;

public class ClassicModeScreen extends Screen {
    private int flipCount;

    public ClassicModeScreen(GameScreen activity) {
        super(activity);
    }

    @Override
    public void onCreate() {
        game.setContentView(R.layout.classic_mode);

        final TextView flipCountText = (TextView) game.findViewById(R.id.classicModeFlipCounter);
        flipCountText.setVisibility(View.INVISIBLE);

        final GameBoard board = (GameBoard)game.findViewById(R.id.classicModeBoard);
        board.setSize(3);
        board.setColorCount(4);
        board.setGameEvents(new GameBoard.IGameEventListener() {
            @Override
            public void initComplete() {
                board.randomize(6);
                Animation dropIn = AnimationUtils.loadAnimation(game, R.anim.drop_in);
                dropIn.setInterpolator(new OvershootInterpolator());
                flipCountText.setVisibility(View.VISIBLE);
                flipCountText.startAnimation(dropIn);
            }

            @Override
            public void onFlip() {
                board.checkForCompletion();
                flipCount++;
                updateCounter();
            }

            @Override
            public void onSingleFlip() {
                board.checkForCompletion();
            }

            @Override
            public void onCompletion() {
                System.out.println("HELLO");
            }
        });


    }

    public void updateCounter() {
        TextView counter = (TextView)game.findViewById(R.id.classicModeFlipCounter);
        counter.setText("Flips: " + flipCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public View getRootLayout() {
        return (View)game.findViewById(R.id.classicMode);
    }
}
