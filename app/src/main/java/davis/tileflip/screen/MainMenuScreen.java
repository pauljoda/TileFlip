package davis.tileflip.screen;

import android.view.Menu;
import android.widget.RelativeLayout;
import davis.tileflip.GameScreen;
import davis.tileflip.R;

public class MainMenuScreen extends Screen implements IScreen {
    public MainMenuScreen(GameScreen activity) {
        super(activity);
    }

    @Override
    public void onCreate() {
        game.setContentView(R.layout.main_menu);
        RelativeLayout layout = (RelativeLayout)game.findViewById(R.id.mainMenu);
        layout.setBackgroundColor(getColorForTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
