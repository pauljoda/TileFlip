package davis.tileflip.screen;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import davis.tileflip.GameScreen;
import davis.tileflip.R;
import davis.tileflip.customviews.TileButton;

public class MenuScreen extends Screen {

    public MenuScreen(GameScreen screen) {
        super(screen);
    }

    @Override
    public void onCreate() {
        game.setContentView(R.layout.main_menu);
        AdView mAdView = (AdView) game.findViewById(R.id.main_menu_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TileButton beginButton = (TileButton)game.findViewById(R.id.playButton);
        beginButton.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.setCurrentScreen(new ClassicModeScreen(game));
            }
        });

        ImageButton google = (ImageButton)game.findViewById(R.id.imageButton);
        if(!game.mGoogleApiClient.isConnected())
            google.setVisibility(View.INVISIBLE);
        else {
            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRootLayout().setVisibility(View.INVISIBLE);
                        game.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(game.mGoogleApiClient, game.getResources().getString(R.string.leaderboard_all_time_high_score)), 0);
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode)
        {
        case 0 :
            Animation popBack = AnimationUtils.loadAnimation(game, R.anim.pop);
            popBack.setInterpolator(new OvershootInterpolator());
            popBack.setDuration(500);
            getRootLayout().setVisibility(View.VISIBLE);
            getRootLayout().startAnimation(popBack);
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        game.setCurrentScreen(new LandingScreen(game));
    }

    @Override
    public View getRootLayout() {
        return game.findViewById(R.id.main_menu_root);
    }
}
