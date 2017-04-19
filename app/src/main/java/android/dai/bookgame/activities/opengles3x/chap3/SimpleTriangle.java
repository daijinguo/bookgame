package android.dai.bookgame.activities.opengles3x.chap3;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class SimpleTriangle extends Activity {

    TriangleSurfaceView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mView = new TriangleSurfaceView(this);
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
