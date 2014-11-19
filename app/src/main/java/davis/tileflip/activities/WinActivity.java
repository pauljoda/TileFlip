package davis.tileflip.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.games.Games;
import davis.tileflip.R;
import davis.tileflip.helpers.GoogleApiHelper;

public class WinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        TextView winningText = (TextView)findViewById(R.id.winningText);
        Intent gameResults = getIntent();
        int score = gameResults.getIntExtra("Score", -1);

        TextView scoreText = (TextView)findViewById(R.id.scoreView);
        scoreText.setText("Score: " + score);

        Games.Leaderboards.submitScore(GoogleApiHelper.getApiClient(), getString(R.string.leaderboard_all_time_high_score), score);
    }

    @Override
    public void onBackPressed() {

    }
}
