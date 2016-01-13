package com.project.googlecardboard.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Garrett on 07/01/2016.
 */
public class OpenGL {

    public static final void checkGLError(String tag, String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(tag, label + ": glError " + error);
        }
    }
}
