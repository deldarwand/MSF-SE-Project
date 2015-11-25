package com.project.googlecardboard.gui;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Garrett on 19/11/2015.
 */
public class GUITexture {

    public static final float[] textureCoordinates = {
        // Front face
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    };

    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;
    private int id;
    private SurfaceTexture surfaceTexture;

    public GUITexture(){

    }

    public GUITexture(Bitmap bitmap){
        init(bitmap);
    }

    public void init(Bitmap bitmap){
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        this.id = texture[0];
        //GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, id);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        //this.surfaceTexture = new SurfaceTexture(id);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    public void loadBitmap(Bitmap bitmap){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    public int getID(){
        return id;
    }

    public void setFrameListener(SurfaceTexture.OnFrameAvailableListener listener){
        surfaceTexture.setOnFrameAvailableListener(listener);
    }

    public synchronized void updateTexture(float[] textureMatrix){
        surfaceTexture.updateTexImage();
        surfaceTexture.getTransformMatrix(textureMatrix);
    }

    public SurfaceTexture getSurfaceTexture(){
        return surfaceTexture;
    }

}
