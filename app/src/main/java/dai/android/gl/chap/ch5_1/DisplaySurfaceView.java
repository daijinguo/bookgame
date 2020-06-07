package dai.android.gl.chap.ch5_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dai.android.gl.utility.MatrixState;

public class DisplaySurfaceView extends GLSurfaceView {

    // 角度缩放比例
    private final float TOUCH_SCAL_FACTOR = 180.0F / 320;

    private SceneRenderer renderer_;

    private float previousX;
    private float previousY;

    public DisplaySurfaceView(Context context) {
        this(context, null);
    }

    public DisplaySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        renderer_ = new SceneRenderer(this);
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
                for (Star6Pointed star : renderer_.stars_) {
                    star.yAngle += dx * TOUCH_SCAL_FACTOR;
                    star.xAngle += dy * TOUCH_SCAL_FACTOR;
                }
            }
        }
        previousX = x;
        previousY = y;
        return true;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private static class SceneRenderer implements GLSurfaceView.Renderer {

        private final Star6Pointed[] stars_ = new Star6Pointed[3];

        private final WeakReference<DisplaySurfaceView> ref;

        SceneRenderer(DisplaySurfaceView view) {
            ref = new WeakReference<>(view);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // 设置屏幕背景颜色 RGBA
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

            // 创建六角星的各个角星
            for (int i = 0; i < stars_.length; i++) {
                stars_[i] = new Star6Pointed(ref.get().getContext(), 0.1f, 0.5f, -0.4f * i);
            }

            // 打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
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
        public void onDrawFrame(GL10 gl) {
            // 清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            // 循环绘画各个六角星
            for (Star6Pointed star : stars_) {
                star.drawSelf();
            }
        }
    }


}
