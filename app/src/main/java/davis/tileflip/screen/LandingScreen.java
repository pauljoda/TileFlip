package davis.tileflip.screen;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import davis.tileflip.GameScreen;
import davis.tileflip.R;
import davis.tileflip.tile.Tile;
import davis.tileflip.tile.TileStates;

import java.util.Random;

public class LandingScreen extends Screen implements IScreen, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    public LandingScreen(GameScreen parent) {
        super(parent);
    }

    @Override
    public void onCreate() {
        game.setContentView(R.layout.activity_landing);

        game.mGoogleApiClient = new GoogleApiClient.Builder(game)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        View view = game.findViewById(R.id.landingLayout);
        ValueAnimator colorChange = ObjectAnimator.ofInt(view, "backgroundColor", view.getResources().getColor(R.color.DayBackground), getColorForTime());
        colorChange.setDuration(3000);
        colorChange.setEvaluator(new ArgbEvaluator());
        colorChange.start();

        SignInButton button = (SignInButton)game.findViewById(R.id.sign_in_button);
        button.setOnClickListener(new SignInClicker());

        Button button1 = (Button)game.findViewById(R.id.skip);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMainMenu();
            }
        });

        final Random r = new Random();
        final Tile tile = (Tile)game.findViewById(R.id.loadingTile);
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
        tile.flipTile();
    }

    public void loadMainMenu() {
        game.setCurrentScreen(new MainMenuScreen(game));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        loadMainMenu();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }
        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(game, game.mGoogleApiClient, connectionResult, RC_SIGN_IN, game.getResources().getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }
    }

    public class SignInClicker implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.sign_in_button) {
                // start the asynchronous sign in flow
                mSignInClicked = true;
                game.mGoogleApiClient.connect();
            }
        }
    }
}
