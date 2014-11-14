package davis.tileflip;

import android.os.Bundle;
import android.view.Menu;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.example.games.basegameutils.BaseGameActivity;
import davis.tileflip.screen.IScreen;
import davis.tileflip.screen.LandingScreen;

public class GameScreen extends BaseGameActivity {
    private IScreen currentScreen;
    public GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentScreen = new LandingScreen(this);
    }

    public void setCurrentScreen(IScreen screen) {
        currentScreen = screen;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return currentScreen.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {
        if(currentScreen instanceof LandingScreen) {
            LandingScreen screen = (LandingScreen)currentScreen;
            screen.loadMainMenu();
        }
    }
}
