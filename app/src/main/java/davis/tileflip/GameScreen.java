package davis.tileflip;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import davis.tileflip.screen.IScreen;
import davis.tileflip.screen.LandingScreen;

public class GameScreen extends Activity {
    private IScreen currentScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentScreen = new LandingScreen(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return currentScreen.onCreateOptionsMenu(menu);
    }
}
