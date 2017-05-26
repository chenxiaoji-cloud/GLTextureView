package www.zemoso.com.openglwithtexture.custom_views;

import android.content.Context;
import android.util.AttributeSet;

import www.zemoso.com.openglwithtexture.gl_renderer.GLTextureRenderer;

/**
 * Created by atif on 25/5/17.
 */

public class MyGLTextureView extends SampleGLTextureView {

    private GLTextureRenderer mSampleRenderer;

    public MyGLTextureView(Context context) {
        super(context);
        initializeViews();
    }

    public MyGLTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews();
    }

    private void initializeViews() {
        mSampleRenderer = new GLTextureRenderer();
        //set the renderer for drawing on the surface view
        setRenderer(mSampleRenderer);
    }

}
