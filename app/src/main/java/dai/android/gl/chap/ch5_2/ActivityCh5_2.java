package dai.android.gl.chap.ch5_2;

import android.opengl.GLES30;
import android.os.Bundle;

import dai.android.gl.chap.ch5_1.ActivityCh5_1;
import dai.android.gl.chap.ch5_1.StarRenderer;
import dai.android.gl.chap.ch5_1.StarSurfaceView;
import dai.android.gl.utility.MatrixState;

public class ActivityCh5_2 extends ActivityCh5_1 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected StarSurfaceView createView() {
        return new StarSurfaceView(this) {
            @Override
            protected StarRenderer createRender() {
                return new StarRenderer(4, this) {
                    @Override
                    protected void surfaceChangeInner(int width, int height) {
                        // 设置视口大小与位置
                        GLES30.glViewport(0, 0, width, height);

                        //计算视口的宽高比
                        float ratio = (float) width / height;

                        //设置透视投影
                        MatrixState.setProjectFrustum(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1, 50);

                        //设置摄像机
                        MatrixState.setCamera(
                                0f, 0f, 6f,
                                0f, 0f, 0f,
                                0f, 1f, 0f
                        );
                    }

                    //  r     R      z
                    // 0.4f, 1.0f,-1.0f
                    @Override
                    protected float get_R() {
                        return 1.0f;
                    }

                    @Override
                    protected float get_r() {
                        return 0.4f;
                    }

                    @Override
                    protected float get_z() {
                        return -1.0f;
                    }
                };
            }
        };
    }
}