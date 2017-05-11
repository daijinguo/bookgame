package android.dai.bookgame.activities.opengles3x.chap5;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Ch5OneActivity extends Activity {

    private Ch5OneSurfaceView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mView = new Ch5OneSurfaceView(this);
        mView.requestFocus();
        mView.setFocusableInTouchMode(true);

        setContentView(mView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }
}
