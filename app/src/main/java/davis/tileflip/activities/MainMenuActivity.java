package davis.tileflip.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import davis.tileflip.customviews.ImageLoadTask;
import davis.tileflip.R;
import davis.tileflip.customviews.TileButton;
import davis.tileflip.helpers.GoogleApiHelper;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        AdView mAdView = (AdView) findViewById(R.id.main_menu_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TileButton beginButton = (TileButton)findViewById(R.id.playButton);
        beginButton.setClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadClassicMode();
            }
        });

        ImageButton google = (ImageButton)findViewById(R.id.googleButton);
        if(!(GoogleApiHelper.getApiClient() != null) && !GoogleApiHelper.getApiClient().isConnected())
            google.setVisibility(View.INVISIBLE);
        else {
            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.main_menu_root).setVisibility(View.INVISIBLE);
                    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(GoogleApiHelper.getApiClient(), getResources().getString(R.string.leaderboard_all_time_high_score)), 0);                }
            });
            if (Plus.PeopleApi.getCurrentPerson(GoogleApiHelper.getApiClient()) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(GoogleApiHelper.getApiClient());
                if(currentPerson.hasImage()) {
                    String image = currentPerson.getImage().getUrl();
                    new ImageLoadTask(image, google).execute();
                }
            }
        }
    }

    public void loadClassicMode() {
        Intent classicMode = new Intent(this, ClassicModeActivity.class);
        startActivity(classicMode);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode)
        {
        case 0 :
            Animation popBack = AnimationUtils.loadAnimation(this, R.anim.pop);
            popBack.setInterpolator(new OvershootInterpolator());
            popBack.setDuration(500);
            findViewById(R.id.main_menu_root).setVisibility(View.VISIBLE);
            findViewById(R.id.main_menu_root).startAnimation(popBack);
            break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
