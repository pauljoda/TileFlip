package davis.tileflip.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import davis.tileflip.R;
import davis.tileflip.tile.GameBoard;

public class ClassicModeActivity extends Activity {

    private int flipCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_mode);

        final TextView flipCountText = (TextView) findViewById(R.id.classicModeFlipCounter);
        flipCountText.setVisibility(View.INVISIBLE);

        final GameBoard board = (GameBoard)findViewById(R.id.classicModeBoard);
        board.setSize(3);
        board.setColorCount(4);
        board.setGameEvents(new GameBoard.IGameEventListener() {
            @Override
            public void initComplete() {
                board.randomize(6);
                Animation dropIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.drop_in);
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
        TextView counter = (TextView)findViewById(R.id.classicModeFlipCounter);
        counter.setText("Flips: " + flipCount);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
