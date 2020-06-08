package dai.android.gl.chap.ch5_1;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class StarRenderer implements GLSurfaceView.Renderer {

    private final Star6Pointed[] stars_;
    private final WeakReference<StarSurfaceView> ref;

    public StarRenderer(int starNum, StarSurfaceView view) {
        stars_ = new Star6Pointed[starNum];
        ref = new WeakReference<>(view);
    }

    protected abstract void surfaceChangeInner(int width, int height);

    protected abstract float get_R();

    protected abstract float get_r();

    protected abstract float get_z();

    final Star6Pointed[] getStars() {
        return stars_;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置屏幕背景颜色 RGBA
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        // 创建六角星的各个角星
        for (int i = 0; i < stars_.length; i++) {
            stars_[i] = new Star6Pointed(ref.get().getContext(), get_r(), get_R(), get_z() * i);
            //stars_[i] = new Star6Pointed(ref.get().getContext(), 0.1f, 0.5f, -0.4f * i);
        }

        // 打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        surfaceChangeInner(width, height);
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
