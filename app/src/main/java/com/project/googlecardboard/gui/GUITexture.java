package com.project.googlecardboard.gui;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

/**
 * Created by Garrett on 19/11/2015.
 */
public class GUITexture {

    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;
    private int id;
    private SurfaceTexture surfaceTexture;

    public GUITexture(){

    }

    public void init(){
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        this.id = texture[0];
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, id);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        this.surfaceTexture = new SurfaceTexture(id);
    }

    public int getID(){
        return id;
    }

    public void setFrameListener(SurfaceTexture.OnFrameAvailableListener listener){
        surfaceTexture.setOnFrameAvailableListener(listener);
    }

    public void bindTexture(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, id);
    }

    public synchronized void updateTexture(float[] textureMatrix){
        surfaceTexture.updateTexImage();
        surfaceTexture.getTransformMatrix(textureMatrix);
    }

    public SurfaceTexture getSurfaceTexture(){
        return surfaceTexture;
    }

}
