package com.project.googlecardboard.matrix;

import android.opengl.Matrix;

import com.google.vrtoolkit.cardboard.Eye;

/**
 * Created by Garrett on 17/11/2015.
 */
public class ViewMatrix {

    private static final float CAMERA_Z = 0.01f;
    private final float[] matrix;

    public ViewMatrix(Eye eye){
        this.matrix = new float[16];
        float[] camera = new float[16];
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(matrix, 0, eye.getEyeView(), 0, camera, 0);
    }

    public float[] getMatrix(){
        return matrix;
    }

}
