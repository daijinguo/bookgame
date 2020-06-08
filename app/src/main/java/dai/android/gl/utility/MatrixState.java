package dai.android.gl.utility;

import android.opengl.Matrix;

public class MatrixState {
    private static float[] programMatrix_ = new float[16];  // 4x4  投影
    private static float[] vMatrix_ = new float[16];        // 摄像机位置朝向参数

    // 设置摄像机的方法
    public static void setCamera(
            float cX, float cY, float cZ,
            float tX, float tY, float tZ,
            float upX, float upY, float upZ
    ) {
        Matrix.setLookAtM(
                vMatrix_,    // 存储生成矩阵内容
                0,            // 其实偏移量
                cX, cY, cZ,   // 摄像机位置坐标
                tX, tY, tZ,   // 观察目标点坐标
                upX, upY, upZ // UP 向量
        );
    }

    // 设置正交投影
    public static void setProjectOrtho(
            float left, float right,
            float bottom, float top,
            float near, float far
    ) {
        Matrix.orthoM(
                programMatrix_,  // 存储生成矩阵元素
                0,               // 填充起始偏移量
                left, right,     // near面 left right
                bottom, top,     // near面 bottom top
                near, far        // near面  far面 与视点间距离
        );
    }

    // 设置透视投影
    public static void setProjectFrustum(
            float left, float right,  // near面 left right
            float bottom, float top,  // near面 bottom top
            float near, float far     // near面 far面与视点距离
    ) {
        Matrix.frustumM(programMatrix_, 0,
                left, right,
                bottom, top,
                near, far
        );
    }

    // 获取具体物体总体变换矩阵
    public static float[] getFinalMatrix(float[] spec) {
        // 存储最终变换矩阵
        // 最终变换矩阵
        float[] mvpMatrix = new float[16];

        // 摄像机矩阵 x 变化矩阵
        Matrix.multiplyMM(mvpMatrix, 0, vMatrix_, 0, spec, 0);

        // 投影矩阵 x 上一步结果 --> 最终变幻矩阵
        Matrix.multiplyMM(mvpMatrix, 0, programMatrix_, 0, mvpMatrix, 0);

        return mvpMatrix;
    }
}
