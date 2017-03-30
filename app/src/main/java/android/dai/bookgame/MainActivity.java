package android.dai.bookgame;

import android.app.Activity;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private final static int viewColors[] = {
            R.color.colorItemView,
            R.color.colorItemView1,
            R.color.colorItemView2,
            R.color.colorItemView3,
            R.color.colorItemView4,
            R.color.colorItemView5,
            R.color.colorItemView6,
            R.color.colorItemView7,
            R.color.colorItemView8,
            R.color.colorItemView9
    };

    private static Random msRandom = new Random(viewColors.length);

    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainer = findViewById(R.id.mainer);
        // demo use setOutlineProvider address
        // http://blog.csdn.net/u013210620/article/details/50075533
        mainer.setClipToOutline(true);
        mainer.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //outline.setOval(0, 0, view.getWidth(), view.getHeight());
                int radius = Math.min(view.getWidth(), view.getHeight()) / 20;
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });

        mContainer = (LinearLayout) findViewById(R.id.container);


        addView(createView("0001", null));
        addView(createView("00011", null));
        addView(createView("000111", null));
        addView(createView("0001111", null));
        addView(createView("00011111", null));
        addView(createView("00011", null));
        addView(createView("00011", null));
        addView(createView("000111", null));
        addView(createView("00101", null));
        addView(createView("01001", null));
        addView(createView("00101", null));
    }

    private void addView(View view) {
        if (null != view && null != mContainer) {
            final int N = mContainer.getChildCount();
            for (int i = 0; i < N; ++i) {
                if (view == mContainer.getChildAt(i))
                    return;
            }

            mContainer.addView(view);
        }
    }

    private TextView createView(String desc, View.OnClickListener listener) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.linear_item_view, null);
        if (null != view) {
            view.setText(desc);
            view.setOnClickListener(listener);
            final int I = msRandom.nextInt(viewColors.length);
            view.setBackgroundColor(getResources().getColor(viewColors[I]));
        }
        return view;
    }


}
