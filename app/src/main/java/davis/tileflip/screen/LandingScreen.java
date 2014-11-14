package davis.tileflip.screen;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import davis.tileflip.R;
import davis.tileflip.tile.GameBoard;

import java.util.Calendar;
import java.util.Random;

public class LandingScreen implements IScreen {
    private Activity game;

    public LandingScreen(Activity parent) {
        game = parent;
        onCreate();
    }

    @Override
    public void onCreate() {
        game.setContentView(R.layout.activity_landing);

        View view = game.findViewById(R.id.landingLayout);
        ValueAnimator colorChange = ObjectAnimator.ofInt(view, "backgroundColor", view.getResources().getColor(R.color.DayBackground), getColorForTime());
        colorChange.setDuration(3000);
        colorChange.setEvaluator(new ArgbEvaluator());
        colorChange.start();

        final Random r = new Random();
        GameBoard board = (GameBoard)game.findViewById(R.id.gameBoard);
        board.setSize(3);
        board.setColorCount(2);
        /*final Tile tile = (Tile)game.findViewById(R.id.loadingTile);
        tile.setFlipDirection(TileStates.FlipDirection.DOWN);
        tile.setMaxColors(5);
        tile.setOnFlipListener(new Tile.OnFlipListener() {
            @Override
            public void onClick(Tile view) {

            }

            @Override
            public boolean onLongClick(Tile view) {
                return false;
            }

            @Override
            public void onFlipStart(Tile view) {

            }

            @Override
            public void onFlipEnd(Tile view) {
                tile.setFlipDirection(TileStates.validDirections[r.nextInt(4)]);
                tile.flipTile();
            }
        });
        tile.flipTile();*/
    }


    public int getColorForTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour > 8 && hour < 18) {
            return game.getResources().getColor(R.color.DayBackground);
        }
        else if(hour > 6 && hour < 20) {
            return game.getResources().getColor(R.color.EveningBackground);
        }
        else {
            return game.getResources().getColor(R.color.NightBackground);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
