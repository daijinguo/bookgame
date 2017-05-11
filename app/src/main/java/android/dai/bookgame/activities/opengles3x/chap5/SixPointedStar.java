package android.dai.bookgame.activities.opengles3x.chap5;

import android.content.Context;
import android.dai.bookgame.activities.opengles3x.utility.SharderUtilityV1;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class SixPointedStar {

    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;

    String mVertexShader, mFragmentShader;

    static float[] mMMatrix = new float[16];

    FloatBuffer mVertexBuffer, mColorBuffer;

    int vCount = 0;
    float xAngle = 0, yAngle = 0;
    final float UNIT_SIZE = 1.0f;

    public SixPointedStar(Context context, float r, float R, float z) {
        initVertexData(R, r, z);

        initShader(context);
    }

    private void initVertexData(float R, float r, float z) {
        List<Float> fList = new ArrayList<>();
        float tmpAngle = 360F / 6F;
        for (float angle = 0F; angle < 360F; angle += tmpAngle) {
            fList.add(0F);
            fList.add(0F);
            fList.add(z);

            fList.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle))));
            fList.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle))));
            fList.add(z);

            fList.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle + tmpAngle / 2))));
            fList.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle + tmpAngle / 2))));
            fList.add(z);


            fList.add(0f);
            fList.add(0f);
            fList.add(z);

            fList.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle + tmpAngle / 2))));
            fList.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle + tmpAngle / 2))));
            fList.add(z);

            fList.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle + tmpAngle))));
            fList.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle + tmpAngle))));
            fList.add(z);
        }
        vCount = fList.size() / 3;
        float[] vertexArray = new float[fList.size()];
        for (int i = 0; i < vCount; ++i) {
            vertexArray[i * 3] = fList.get(i * 3);
            vertexArray[i * 3 + 1] = fList.get(i * 3 + 1);
            vertexArray[i * 3 + 2] = fList.get(i * 3 + 2);
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);


        float[] colorArray = new float[vCount * 4];
        for (int I = 0; I < vCount; ++I) {
            if (I % 3 == 0) {
                colorArray[I * 4] = 1F;
                colorArray[I * 4 + 1] = 1F;
                colorArray[I * 4 + 2] = 1F;
                colorArray[I * 4 + 3] = 0F;
            } else {
                colorArray[I * 4] = 0.45F;
                colorArray[I * 4 + 1] = 0.75F;
                colorArray[I * 4 + 2] = 0.75F;
                colorArray[I * 4 + 3] = 0F;
            }
        }
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colorArray);
        mColorBuffer.position(0);
    }

    private void initShader(Context context) {
        mVertexShader = SharderUtilityV1.LoadResourceFromAssetsFile(
                "chap3/simple_triangle_vertex.shader", context.getResources());

        mFragmentShader = SharderUtilityV1.LoadResourceFromAssetsFile(
                "chap3/simple_triangle_fragment.shader", context.getResources());

        mProgram = SharderUtilityV1.CreateProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        Matrix.rotateM(mMMatrix, 0, yAngle, 0, 1, 0);
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0);

        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer);

        GLES30.glVertexAttribPointer(
                maColorHandle,
                4,
                GLES30.GL_FLOAT,
                false,
                4 * 4,
                mColorBuffer);

        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }

}
