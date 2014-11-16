package davis.tileflip.screen;

import android.view.Menu;
import android.view.View;
import davis.tileflip.GameScreen;
import davis.tileflip.R;

import java.util.Calendar;

public abstract class Screen implements IScreen {
    protected GameScreen game;
    public abstract View getRootLayout();

    public Screen(GameScreen activity) {
        game = activity;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
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
}
