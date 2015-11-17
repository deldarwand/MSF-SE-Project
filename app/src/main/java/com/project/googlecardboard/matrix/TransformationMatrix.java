package com.project.googlecardboard.matrix;

import android.opengl.Matrix;

/**
 * Created by Garrett on 17/11/2015.
 */
public class TransformationMatrix {

    private final float[] matrix;

    public TransformationMatrix(float x, float y, float z, float roll, float pitch, float yaw){
        this.matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix, 0, x, y, z);
        Matrix.rotateM(matrix, 0, roll, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(matrix, 0, pitch, 0.0f, 0.0f, 1.0f);
        Matrix.rotateM(matrix, 0, yaw, 0.0f, 1.0f, 0.0f);
    }

    public float[] getMatrix(){
        return matrix;
    }
}
