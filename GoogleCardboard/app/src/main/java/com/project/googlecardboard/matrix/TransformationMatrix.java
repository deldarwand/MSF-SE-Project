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
        /* Method 1
            Translates, and then rotates
            This positions boxes in a cylindrical way
         */
        Matrix.translateM(matrix, 0, x, y, z);
        Matrix.rotateM(matrix, 0, yaw, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(matrix, 0, pitch, 0.0f, 0.0f, 1.0f);
        Matrix.rotateM(matrix, 0, roll, 1.0f, 0.0f, 0.0f);
    }

    public TransformationMatrix(float radius, float roll, float pitch, float yaw){
        this.matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        /* Method 2
            Rotates, and then translates
            This positions boxes in a spherical way
         */
        Matrix.rotateM(matrix, 0, yaw, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(matrix, 0, pitch, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(matrix, 0, roll, 0.0f, 0.0f, 1.0f);
        Matrix.translateM(matrix, 0, 0, 0, radius);
        Matrix.scaleM(matrix, 0, 3.0f, 3.0f, 3.0f);
    }

    public float[] getMatrix(){
        return matrix;
    }
}
