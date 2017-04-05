package android.dai.bookgame;


import android.app.Activity;
import android.content.Intent;
import android.dai.bookgame.activities.opengles3x.chap3.SimpleTriangle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Random;

class MainItemViewsUtility {
    private Activity mActivity;
    private ArrayDeque<TextView> mViews = new ArrayDeque<>();

    private static Random msRandom = new Random();
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

    MainItemViewsUtility(Activity activity) {
        mActivity = activity;
        mViews.clear();

        testData();
        postViews();
    }

    ArrayDeque<TextView> getViews() {
        return mViews;
    }

    private TextView createView(String desc, View.OnClickListener listener) {
        TextView view = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.linear_item_view, null);
        if (null != view) {
            view.setText(desc);
            view.setOnClickListener(listener);
            final int I = msRandom.nextInt(viewColors.length);
            view.setBackgroundColor(mActivity.getResources().getColor(viewColors[I]));
        }
        return view;
    }

    private void testData() {
        mViews.add(createView("exit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation translate = new TranslateAnimation(-v.getWidth() / 4, v.getWidth() / 4, 0, 0);
                translate.setDuration(500);
                translate.setRepeatCount(4);
                translate.setRepeatMode(Animation.REVERSE);
                translate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mActivity.finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                v.startAnimation(translate);
            }
        }));

        mViews.add(createView("click start an animation", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation translate = new TranslateAnimation(-v.getWidth() / 4, v.getWidth() / 4, 0, 0);
                translate.setDuration(500);
                translate.setRepeatCount(2);
                translate.setRepeatMode(Animation.REVERSE);

                v.startAnimation(translate);
            }
        }));

        mViews.add(createView("start camera", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mActivity.startActivity(intent);
            }
        }));

        mViews.add(createView("empty demo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
    }

    private void postViews() {
        // the follow activity for book
        // OpenGL ES 3.x game developer

        /**
         * chap3 simple triangle
         */
        mViews.add(createView(mActivity.getResources().getString(R.string.str_chap3_1),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, SimpleTriangle.class);
                        mActivity.startActivity(intent);
                    }
                }));
    }
}
