package android.dai.bookgame.activities.opengles3x.chap3;

import android.dai.bookgame.activities.opengles3x.utility.SharderUtilityV1;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {
    public static float[] mProjectMatrix = new float[16];
    public static float[] mVMatrix = new float[16];
    public static float[] mMVPMatrix;

    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;

    String mVertexShader;
    String mFragmentShader;

    static float[] mMMatrix = new float[16];

    FloatBuffer mVextexBuffer;
    FloatBuffer mColorBuffer;

    int vCount = 0;
    float xAngle = 0;

    public Triangle(TriangleSurfaceView view) {
        initVetexData();
        initShader(view);
    }

    private void initVetexData() {
        vCount = 3;

        {
            final float UNIT_SIZE = 0.2F;
            float vertices[] = new float[]{
                    -4 * UNIT_SIZE, 0, 0,
                    0, -4 * UNIT_SIZE, 0,
                    4 * UNIT_SIZE, 0, 0
            };

            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            mVextexBuffer = vbb.asFloatBuffer();
            mVextexBuffer.put(vertices);
            mVextexBuffer.position(0);
        }

        {

            float colors[] = new float[]{
                    1, 1, 1,
                    0, 0, 0,
                    1, 0, 0,
                    1, 0, 0
            };

            ByteBuffer vbb = ByteBuffer.allocateDirect(colors.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            mColorBuffer = vbb.asFloatBuffer();
            mColorBuffer.put(colors);
            mColorBuffer.position(0);
        }
    }

    private void initShader(TriangleSurfaceView view) {
        mVertexShader = SharderUtilityV1.LoadResourceFromAssetsFile(
                "chap3/simple_triangle_vertex.shader", view.getResources());

        mFragmentShader = SharderUtilityV1.LoadResourceFromAssetsFile(
                "chap3/simple_triangle_fragment.shader", view.getResources());

        mProgram = SharderUtilityV1.CreateProgram(mVertexShader, mFragmentShader);

        // get the position at file chap3/simple_triangle_vertex.shader:
        // aPosition
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");

        // get the position at file chap3/simple_triangle_vertex.shader:
        // aColor
        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");

        // get the position at file chap3/simple_triangle_vertex.shader:
        // uMVPMatrix
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    private static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);

        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getFinalMatrix(mMMatrix), 0);
        GLES30.glVertexAttribPointer(maPositionHandle, 3,
                GLES30.GL_FLOAT, false, 3 * 4, mVextexBuffer);
        GLES30.glVertexAttribPointer(maColorHandle, 4,
                GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
