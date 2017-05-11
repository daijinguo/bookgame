package android.dai.bookgame.activities.opengles3x.chap5;

import android.content.Context;
import android.dai.bookgame.activities.opengles3x.chap3.TriangleSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class Ch5OneSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0F / 320;

    private float mPreX, mPreY;
    private SceneRender mSceneRender;

    public Ch5OneSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(3);

        mSceneRender = new SceneRender();
        setRenderer(mSceneRender);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                float dy = y - mPreY;
                float dx = x - mPreX;
                for (SixPointedStar h : mSceneRender.ha) {
                    h.yAngle += dx * TOUCH_SCALE_FACTOR;
                    h.xAngle += dy * TOUCH_SCALE_FACTOR;
                }
            }
        }

        mPreY = y;
        mPreX = x;
        return true;
    }

    private class SceneRender implements GLSurfaceView.Renderer {

        SixPointedStar[] ha = new SixPointedStar[6];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            for (int i = 0; i < ha.length; i++) {
                ha[i] = new SixPointedStar(Ch5OneSurfaceView.this.getContext(), 0.2f, 0.5f, -0.3f * i);
            }
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
            MatrixState.setCamera(
                    0, 0, 3f,
                    0, 0, 0f,
                    0f, 1.0f, 0.0f
            );
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            for (SixPointedStar h : ha) {
                h.drawSelf();
            }
        }
    }
}
