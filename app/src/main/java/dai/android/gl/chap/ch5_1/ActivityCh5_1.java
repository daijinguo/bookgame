package dai.android.gl.chap.ch5_1;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityCh5_1 extends AppCompatActivity {

    private DisplaySurfaceView surfaceView_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        surfaceView_ = new DisplaySurfaceView(this);
        setContentView(surfaceView_);

        surfaceView_.requestFocus();
        surfaceView_.setFocusableInTouchMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView_.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView_.onPause();
    }
}
