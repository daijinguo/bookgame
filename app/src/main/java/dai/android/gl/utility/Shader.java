package dai.android.gl.utility;

import android.content.res.Resources;
import android.opengl.GLES30;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Shader {
    private static final String TAG = "shader";

    public static void checkGLError(String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "{" + op + "} with error: " + error);
            throw new RuntimeException("opengles error with [" + op + "]: " + error);
        }
    }

    public static int loadShader(int shaderType, String source) {
        // create a new shader
        int shader = GLES30.glCreateShader(shaderType);

        // load the shader
        if (0 != shader) {
            // load the shader source
            GLES30.glShaderSource(shader, source);

            // compile the shader
            GLES30.glCompileShader(shader);

            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (0 == compiled[0]) {
                Log.e(TAG, "can not compile shader with type= " + shaderType);
                Log.e(TAG, GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }

        return shader;
    }

    // create shader program
    public static int createProgram(String srcVertex, String srcFragment) {
        // load the vertex shader
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, srcVertex);
        if (0 == vertexShader) {
            return 0;
        }

        // load the fragment shader
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, srcFragment);
        if (0 == fragmentShader) {
            return 0;
        }

        // create the program
        int program = GLES30.glCreateProgram();
        if (0 != program) {
            GLES30.glAttachShader(program, vertexShader);
            checkGLError("glAttachShader@vertex");

            GLES30.glAttachShader(program, fragmentShader);
            checkGLError("glAttachShader@fragment");

            GLES30.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES30.GL_TRUE) {
                Log.e(TAG, "can not link program");
                Log.e(TAG, GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }


    public static String loadFromAssetsFile(String name, Resources r) {
        String result = null;
        try {
            InputStream in = r.getAssets().open(name);
            int ch = 0;
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            while (-1 != (ch = in.read())) {
                byteOut.write(ch);
            }
            byte[] buffer = byteOut.toByteArray();
            byteOut.close();
            in.close();

            result = new String(buffer, StandardCharsets.UTF_8);
            result = result.replace("\\r\\n", "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
