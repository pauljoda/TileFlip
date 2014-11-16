package davis.tileflip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import davis.tileflip.screen.LandingScreen;
import davis.tileflip.screen.MenuScreen;
import davis.tileflip.screen.Screen;

public class GameScreen extends Activity {
    public Screen currentScreen;
    public GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentScreen = new LandingScreen(this);
        currentScreen.onCreate();

    }

    public void setCurrentScreen(final Screen screen) {
        Animation dropOut = AnimationUtils.loadAnimation(this, R.anim.drop_out);
        dropOut.setInterpolator(new AccelerateInterpolator());
        dropOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                currentScreen = screen;
                currentScreen.onCreate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        View root = currentScreen.getRootLayout();
        root.startAnimation(dropOut);
    }

    @Override
    public void onBackPressed() {
        currentScreen.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return currentScreen.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(currentScreen instanceof MenuScreen) {
            MenuScreen screen = (MenuScreen)currentScreen;
            screen.onActivityResult(requestCode, resultCode, data);
        }
    }
}
