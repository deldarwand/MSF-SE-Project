package com.project.googlecardboard.gui;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Garrett on 19/11/2015.
 */
public class GUITexture {

    private static final float[] textureCoordinates = {
        // Front face
        0.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 0.0f,
        1.0f, 1.0f,
        0.0f, 1.0f,
        1.0f, 0.0f
    };
    private int id;
    public GUITexture() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        this.id = texture[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
    }

    public GUITexture(Bitmap bitmap){
        loadBitmap(bitmap);
    }

    public void loadBitmap(Bitmap bitmap){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    public int getID(){
        return id;
    }

    public float[] getTextureCoordinates(){
        return textureCoordinates;
    }
}
