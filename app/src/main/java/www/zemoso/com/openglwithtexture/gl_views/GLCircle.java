package www.zemoso.com.openglwithtexture.gl_views;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import www.zemoso.com.openglwithtexture.gl_renderer.GLTextureView;

/**
 * Created by atif on 11/5/17.
 */

public class GLCircle {

    private FloatBuffer mVertexBuffer;
    private int mPositionHandle;
    private int mColorHandle;
    private int mProgram;

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;"+
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public static final int COORDS_PER_VERTEX = 3;
    public static final float circleCoords[] = new float[3*364];

    private int mMVPMatrixHandle;

    public float color[] = {0.345601f, 0.467123f, 0.712341f, 1.0f};

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private final int vertexCount = circleCoords.length / COORDS_PER_VERTEX;

    public GLCircle() {

        circleCoords[0] = 0;
        circleCoords[1] = 0;
        circleCoords[2] = 0;

        for(int i =1; i <364; i++){
            circleCoords[(i * 3)] = (float) (0.5 * Math.cos((3.14/180) * (float)i ));
            circleCoords[(i * 3)+ 1] = (float) (0.5 * Math.sin((3.14/180) * (float)i ));
            circleCoords[(i * 3)+ 2] = 0;
        }


        ByteBuffer bb = ByteBuffer.allocateDirect(circleCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(circleCoords);
        mVertexBuffer.position(0);

        int vertextShader = GLTextureView.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = GLTextureView.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertextShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix){
        GLES20.glUseProgram(mProgram);
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
