package android.dai.bookgame.activities.opengles3x.utility;


import android.content.res.Resources;
import android.opengl.GLES30;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SharderUtilityV1 {

    private static String TAG = "SharderUtilityV1";

    public static void ChechError(String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, op + ": failed, reason: " + error);

            throw new RuntimeException(op + ": failed, reason: " + error);
        }
    }

    public static int LoadShader(int shaderType, String source) {
        int shader = GLES30.glCreateShader(shaderType);
        if (0 != shader) {
            GLES30.glShaderSource(shader, source);
            GLES30.glCompileShader(shader);

            int[] complied = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, complied, 0);
            if (0 == complied[0]) {
                Log.e(TAG, "Can not complied shader with type:" + shaderType
                        + ", Reason:" + GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int CreateProgram(String vSrc, String fSrc) {
        int vertex = LoadShader(GLES30.GL_VERTEX_SHADER, vSrc);
        if (0 == vertex) return 0;

        int pixel = LoadShader(GLES30.GL_FRAGMENT_SHADER, fSrc);
        if (0 == pixel) return 0;

        int program = GLES30.glCreateProgram();
        if (0 != program) {
            GLES30.glAttachShader(program, vertex);
            ChechError("glAttachShader with vertex");

            GLES30.glAttachShader(program, pixel);
            ChechError("glAttachShader with fragment");

            GLES30.glLinkProgram(program);
            int[] linked = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linked, 0);
            if (GLES30.GL_TRUE != linked[0]) {
                Log.e(TAG, "Can not link program, Reason:"
                        + GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public static String LoadResourceFromAssetsFile(String name, Resources res) {
        String strResult = null;
        try {
            InputStream in = res.getAssets().open(name);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int ch = 0;
            while (-1 != (ch = in.read())) {
                out.write(ch);
            }
            byte[] buffer = out.toByteArray();
            in.close();
            out.close();

            strResult = new String(buffer, "utf-8");
            strResult = strResult.replaceAll("\\r\\n", "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }


}
