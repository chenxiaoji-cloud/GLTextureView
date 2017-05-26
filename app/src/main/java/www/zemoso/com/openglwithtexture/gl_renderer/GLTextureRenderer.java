package www.zemoso.com.openglwithtexture.gl_renderer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import www.zemoso.com.openglwithtexture.gl_views.GLCircle;
import www.zemoso.com.openglwithtexture.gl_views.GlTriangle;

/**
 * Created by atif on 25/5/17.
 */

public class GLTextureRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = GLTextureRenderer.class.getSimpleName();
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private GlTriangle mGLTriangle;
    private GLCircle mGLCircle;
    private float[] mRotationMatrix = new float[16];

    //region Shader Helpers

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        Log.d(TAG, "surfaceCreated called");

        //set background color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //initialize the triangle
        mGLTriangle = new GlTriangle();

        //initialize the circle
        mGLCircle = new GLCircle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "sizeChanged called");

        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "drawFrame called");

        float[] scratch = new float[16];
        //redraw background
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        mGLSquare.draw();

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);


        mGLTriangle.draw(mMVPMatrix);
        mGLCircle.draw(mMVPMatrix);
    }

    //endregion

}
