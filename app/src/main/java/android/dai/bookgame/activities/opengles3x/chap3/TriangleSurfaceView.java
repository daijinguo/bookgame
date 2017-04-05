package android.dai.bookgame.activities.opengles3x.chap3;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class TriangleSurfaceView extends GLSurfaceView {

    final float ANGLE_SPAN = 0.375F;

    SceneRenderer mRenderer;
    RotateThread mThread;

    public TriangleSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(3);

        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0, 0, 0, 0f);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);

            mThread = new RotateThread();
            mThread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT);
        }
    }

    private class RotateThread extends Thread {
        public boolean Flag = true;

        @Override
        public void run() {
            while (Flag) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
