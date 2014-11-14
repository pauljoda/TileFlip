package davis.tileflip;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import davis.tileflip.tile.Tile;
import davis.tileflip.tile.TileStates;

import java.util.Calendar;
import java.util.Random;

public class GameScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        View view = findViewById(R.id.landingLayout);
        ValueAnimator colorChange = ObjectAnimator.ofInt(view, "backgroundColor", view.getResources().getColor(R.color.DayBackground), getColorForTime());
        colorChange.setDuration(3000);
        colorChange.setEvaluator(new ArgbEvaluator());
        colorChange.start();

        final Random r = new Random();
        final Tile tile = (Tile)findViewById(R.id.loadingTile);
        tile.setFlipDirection(TileStates.FlipDirection.DOWN);
        tile.setOnFlipListener(new Tile.OnFlipListener() {
            @Override
            public void onClick(Tile view) {

            }

            @Override
            public void onFlipStart(Tile view) {

            }

            @Override
            public void onFlipEnd(Tile view) {
                tile.setFlipDirection(TileStates.validDirections[r.nextInt(4)]);
                tile.toggleFlip();
            }
        });
        tile.setOnClickListener(null);
        tile.toggleFlip();
    }

    public int getColorForTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour > 8 && hour < 18) {
            return getResources().getColor(R.color.DayBackground);
        }
        else if(hour > 6 && hour < 20) {
            return getResources().getColor(R.color.EveningBackground);
        }
        else {
            return getResources().getColor(R.color.NightBackground);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }
}
