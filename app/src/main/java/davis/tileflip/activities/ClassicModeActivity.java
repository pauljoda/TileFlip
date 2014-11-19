package davis.tileflip.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import davis.tileflip.R;
import davis.tileflip.customviews.TileButton;
import davis.tileflip.tile.GameBoard;
import davis.tileflip.tile.TileStates;

public class ClassicModeActivity extends Activity {

    private int flipCount;
    private int singleFlips;
    private int maxSingleFlips;

    private int colorCount;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_mode);

        final TileButton flipCountDisplay = (TileButton) findViewById(R.id.classicModeFlipCount);
        flipCountDisplay.setOnClickListener(null);
        flipCountDisplay.setVisibility(View.INVISIBLE);

        final TileButton singleFlipCounter = (TileButton) findViewById(R.id.classicModeSingleFlipCounter);
        singleFlipCounter.setOnClickListener(null);
        singleFlipCounter.setVisibility(View.INVISIBLE);
        singleFlips = maxSingleFlips = 3;
        singleFlipCounter.setText("Single:\n" + singleFlips);

        final GameBoard board = (GameBoard)findViewById(R.id.classicModeBoard);
        size = 3;
        colorCount = 2;
        board.setSize(size);
        board.setColorCount(colorCount);
        board.setGameEvents(new GameBoard.IGameEventListener() {
            @Override
            public void initComplete() {
                board.randomize(6);
                final Animation dropIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.drop_in);
                dropIn.setInterpolator(new OvershootInterpolator());
                dropIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        singleFlipCounter.setVisibility(View.VISIBLE);
                        dropIn.setAnimationListener(null);
                        singleFlipCounter.startAnimation(dropIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                flipCountDisplay.setVisibility(View.VISIBLE);
                flipCountDisplay.startAnimation(dropIn);
            }

            @Override
            public void onFlip() {
                flipCount++;
                updateCounter();
                if(isComplete())
                    onCompletion();
            }

            @Override
            public void onSingleFlip() {
                if(singleFlips > 0)
                    updateSingleFlipCounter();
                if(singleFlips <= 1)
                    board.setCanSingleFlip(false);
                if(isComplete())
                    onCompletion();
            }

            public boolean isComplete() {
                TileStates.TileColor check = board.tiles.get(0).getTileColor();
                for(int i = 0; i < board.tiles.size(); i++) {
                    if(board.tiles.get(i).getTileColor() != check)
                        return false;
                }
                return true;
            }

            @Override
            public void onCompletion() {
                displayWinningScreen();
            }
        });
    }

    public void updateCounter() {
        TileButton counter = (TileButton)findViewById(R.id.classicModeFlipCount);
        counter.performFlip();
        counter.setText("Flips:\n" + flipCount);
    }

    public void updateSingleFlipCounter() {
        singleFlips--;
        TileButton counter = (TileButton)findViewById(R.id.classicModeSingleFlipCounter);
        counter.performFlip();
        counter.setText("Single:\n" + singleFlips);
    }

    public void displayWinningScreen() {
        final GameBoard board = (GameBoard)findViewById(R.id.classicModeBoard);

        Intent win = new Intent(this, WinActivity.class);
        int maxScore = (2000 * colorCount * size);
        int score = maxScore - (flipCount * (maxScore / 1000));
        score -= (maxSingleFlips - singleFlips) * (maxScore / 10);
        win.putExtra("Score", score);
        startActivity(win);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
