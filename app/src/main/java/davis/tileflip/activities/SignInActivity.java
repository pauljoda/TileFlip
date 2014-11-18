package davis.tileflip.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import davis.tileflip.R;
import davis.tileflip.customviews.TileButton;
import davis.tileflip.helpers.GoogleApiHelper;
import davis.tileflip.tile.Tile;
import davis.tileflip.tile.TileStates;

import java.util.Random;

public class SignInActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = false;
    private boolean mSignInClicked = false;
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleApiHelper.setApiClient(new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API).addScope(Games.SCOPE_GAMES).build());

        SignInButton button = (SignInButton)findViewById(R.id.sign_in_button);
        button.setOnClickListener(new SignInClicker());

        TileButton skipButton = (TileButton)findViewById(R.id.skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMainMenu();
            }
        });

        final Random r = new Random();
        final Tile tile = (Tile)findViewById(R.id.loadingTile);
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
        Intent loadMainMenu = new Intent(this, MainMenuActivity.class);
        startActivity(loadMainMenu);
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
            if (!BaseGameUtils.resolveConnectionFailure(this, GoogleApiHelper.getApiClient(), connectionResult, RC_SIGN_IN, getResources().getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            GoogleApiHelper.getApiClient().connect();
        }
    }

    public class SignInClicker implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.sign_in_button) {
                // start the asynchronous sign in flow
                mSignInClicked = true;
                GoogleApiHelper.getApiClient().connect();
            }
        }
    }
}
