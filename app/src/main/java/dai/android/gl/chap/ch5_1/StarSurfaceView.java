package dai.android.gl.chap.ch5_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import dai.android.gl.utility.MatrixState;

public class StarSurfaceView extends GLSurfaceView {

    // 角度缩放比例
    private final float TOUCH_SCAL_FACTOR = 180.0F / 320;

    private StarRenderer renderer_;

    private float previousX;
    private float previousY;

    public StarSurfaceView(Context context) {
        this(context, null);
    }

    public StarSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        renderer_ = createRender();
        setRenderer(renderer_);

        //设置渲染模式为主动渲染
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                float dy = y - previousY;
                float dx = x - previousX;
                for (Star6Pointed star : renderer_.getStars()) {
                    star.yAngle += dx * TOUCH_SCAL_FACTOR;
                    star.xAngle += dy * TOUCH_SCAL_FACTOR;
                }
            }
        }
        previousX = x;
        previousY = y;
        return true;
    }

    protected StarRenderer createRender() {
        return new StarRenderer(5, this) {
            @Override
            protected void surfaceChangeInner(int width, int height) {
                // 设置视口的大小及位置
                GLES30.glViewport(0, 0, width, height);

                // 计算视口的宽高比
                float ratio = (float) width / height;
                // 设置正交投影
                MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);

                // 设置摄像机
                MatrixState.setCamera(
                        0f, 0f, 3.0f,
                        0f, 0f, 0f,
                        0f, 1.0f, 0.0f
                );
            }

            @Override
            protected float get_R() {
                return 0.5f;
            }

            @Override
            protected float get_r() {
                return 0.1f;
            }

            @Override
            protected float get_z() {
                return -0.4f;
            }
        };
    }
}
