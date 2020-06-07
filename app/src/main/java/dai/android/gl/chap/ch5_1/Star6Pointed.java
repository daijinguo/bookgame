package dai.android.gl.chap.ch5_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import dai.android.gl.utility.MatrixState;
import dai.android.gl.utility.Shader;

public class Star6Pointed {

    private int program_;
    private int handle_uMVPMatrix;  // 总变换矩阵 handle
    private int handle_aPosition;   // 顶点位置属性 handle
    private int handle_aColor;      // 顶点颜色属性 handle

    private float[] MMatrix_ = new float[16];  // 具体物体3D变换矩阵(旋转 平移 缩放)

    private FloatBuffer vertexBuffer_;
    private FloatBuffer colorBuffer_;

    int vCount = 0;
    float yAngle = 0; // 绕Y轴 旋转角度
    float xAngle = 0; // 绕X轴 旋转角度

    final float UNIT_SIZE = 1;

    public Star6Pointed(Context ctx, float r, float R, float z) {
        //初始化顶点数据
        initVertexData(R, r, z);

        //初始化着色器
        initShader(ctx);
    }

    public void drawSelf() {
        GLES30.glUseProgram(program_);

        // 初始化变换矩阵
        Matrix.setRotateM(MMatrix_, 0, 0, 0, 1, 0);
        // 设置沿 Z 轴 正
        Matrix.translateM(MMatrix_, 0, 0, 0, 1);
        // 设置沿 Y 轴 旋转
        Matrix.rotateM(MMatrix_, 0, yAngle, 0, 1, 0);
        // 设置沿 X 轴 旋转
        Matrix.rotateM(MMatrix_, 0, xAngle, 1, 0, 0);
        // 最终变幻矩阵转入渲染管线
        GLES30.glUniformMatrix4fv(handle_uMVPMatrix, 1, false, MatrixState.getFinalMatrix(MMatrix_), 0);
        // 将顶点位置数据传入渲染管线
        GLES30.glVertexAttribPointer(
                handle_aPosition,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                vertexBuffer_
        );
        // 将顶点颜色数据传入渲染管线
        GLES30.glVertexAttribPointer(
                handle_aColor,
                4,
                GLES30.GL_FLOAT,
                false,
                4 * 4,
                colorBuffer_
        );
        // 启用顶点位置数据
        GLES30.glEnableVertexAttribArray(handle_aPosition);
        // 启用顶点颜色数据数组
        GLES30.glEnableVertexAttribArray(handle_aColor);
        // 绘制 六角星
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }

    private void initVertexData(float R, float r, float z) {
        List<Float> floatList = new ArrayList<>();
        final float tempAngle = 60F; // 360F / 6F;

        float RR = R * UNIT_SIZE;
        float rr = r * UNIT_SIZE;

        for (float angle = 0F; angle < 360F; angle += tempAngle) {
            // X  Y  Z
            // 第一个三角形(第一个点)
            floatList.add(0F);
            floatList.add(0F);
            floatList.add(z);

            // 第一个三角形(第二个点)
            floatList.add((float) (RR * Math.cos(Math.toRadians(angle))));
            floatList.add((float) (RR * Math.sin(Math.toRadians(angle))));
            floatList.add(z);

            // 第一个三角形(第三个点)
            final float rrx = (float) (rr * Math.cos(Math.toRadians(angle + tempAngle / 2)));
            final float rry = (float) (rr * Math.sin(Math.toRadians(angle + tempAngle / 2)));
            floatList.add(rrx);
            floatList.add(rry);
            floatList.add(z);


            // 第二个三角行(第一个点)
            floatList.add(0F);
            floatList.add(0F);
            floatList.add(z);

            // 第二个三角行(第二个点)
            floatList.add(rrx);
            floatList.add(rry);
            floatList.add(z);

            // 第二个三角行(第三个点)
            floatList.add((float) (RR * Math.cos((Math.toRadians(angle + tempAngle)))));
            floatList.add((float) (RR * Math.sin((Math.toRadians(angle + tempAngle)))));
            floatList.add(z);
        }

        vCount = floatList.size() / 3;
        float[] vertexArray = new float[floatList.size()];
        for (int i = 0; i < vCount; i++) {
            final int N0 = i * 3;
            final int N1 = N0 + 1;
            final int N2 = N0 + 2;
            vertexArray[N0] = floatList.get(N0);
            vertexArray[N1] = floatList.get(N1);
            vertexArray[N2] = floatList.get(N2);
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer_ = vbb.asFloatBuffer();
        vertexBuffer_.put(vertexArray);
        vertexBuffer_.position(0);

        // 顶点着色器颜色数据
        float[] colorArray = new float[vCount * 4];
        for (int i = 0; i < vCount; ++i) {
            // 中心点白色  RGBA [1, 1, 1, 0]
            if (i % 3 == 0) {
                colorArray[i * 4] = 1;
                colorArray[i * 4 + 1] = 1;
                colorArray[i * 4 + 2] = 1;
                colorArray[i * 4 + 3] = 0;
            }
            // 边上点为蓝色 RGBA [0.45, 0.75, 0.75, 0]
            else {
                colorArray[i * 4] = 0.45F;
                colorArray[i * 4 + 1] = 0.75F;
                colorArray[i * 4 + 2] = 0.75F;
                colorArray[i * 4 + 3] = 0;
            }
        }
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer_ = cbb.asFloatBuffer();
        colorBuffer_.put(colorArray);
        colorBuffer_.position(0);
    }

    private void initShader(Context ctx) {
        String shaderSrcVertex = Shader.loadFromAssetsFile("chap/ch05_01_vertex.sh", ctx.getResources());
        String shaderSrcFragment = Shader.loadFromAssetsFile("chap/ch05_01_fragment.sh", ctx.getResources());

        program_ = Shader.createProgram(shaderSrcVertex, shaderSrcFragment);

        handle_aPosition = GLES30.glGetAttribLocation(program_, "aPosition");
        handle_aColor = GLES30.glGetAttribLocation(program_, "aColor");
        handle_uMVPMatrix = GLES30.glGetUniformLocation(program_, "uMVPMatrix");
    }

}
